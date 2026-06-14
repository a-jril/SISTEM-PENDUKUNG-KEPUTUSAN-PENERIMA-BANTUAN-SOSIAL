package com.spkbansos.controller;

import com.spkbansos.dao.DatabaseHelper;
import com.spkbansos.dao.KriteriaDAO;
import com.spkbansos.model.HasilSAW;
import com.spkbansos.model.KriteriaSAW;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SAWController {

    private final KriteriaDAO kriteriaDAO;

    public SAWController() {
        this.kriteriaDAO = new KriteriaDAO();
    }

    /**
     * Exception kustom untuk dilempar jika terjadi validasi yang gagal.
     */
    public static class SAWValidationException extends Exception {
        public SAWValidationException(String message) {
            super(message);
        }
    }

    /**
     * Hitung SAW untuk periode tertentu.
     */
    public List<HasilSAW> hitung(String periode) throws SAWValidationException {
        // 1. Ambil Kriteria Aktif
        List<KriteriaSAW> kriteriaList = kriteriaDAO.getAktif();
        if (kriteriaList.isEmpty()) {
            throw new SAWValidationException("Tidak ada kriteria aktif untuk dihitung.");
        }

        // Cek total bobot
        double totalBobot = 0;
        for (KriteriaSAW k : kriteriaList) {
            totalBobot += k.getBobot();
        }
        // Gunakan toleransi floating point
        if (Math.abs(totalBobot - 100.0) > 0.001) {
            throw new SAWValidationException("Total bobot kriteria aktif tidak sama dengan 100%.\nTotal saat ini: " + totalBobot + "%");
        }

        // 2. Ambil Data Mentah (Matriks Keputusan) dan Data Warga
        Map<Integer, Map<Integer, Double>> matriksKeputusan = new HashMap<>();
        Map<Integer, HasilSAW> dataWarga = new HashMap<>();
        
        String query = "SELECT pe.id_warga, w.nik, w.nama, pe.id_kriteria, pe.nilai " +
                       "FROM penilaian_evaluasi pe " +
                       "JOIN warga w ON pe.id_warga = w.id " +
                       "JOIN kriteria_saw ks ON pe.id_kriteria = ks.id " +
                       "WHERE pe.periode = ? AND ks.is_aktif = 1";
                       
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, periode);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idWarga = rs.getInt("id_warga");
                    String nik = rs.getString("nik");
                    String nama = rs.getString("nama");
                    int idKriteria = rs.getInt("id_kriteria");
                    double nilai = rs.getDouble("nilai");
                    
                    if (!dataWarga.containsKey(idWarga)) {
                        dataWarga.put(idWarga, new HasilSAW(idWarga, nik, nama));
                    }
                    
                    matriksKeputusan.computeIfAbsent(idWarga, k -> new HashMap<>()).put(idKriteria, nilai);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SAWValidationException("Terjadi kesalahan saat mengambil data dari database.");
        }

        if (matriksKeputusan.isEmpty()) {
            throw new SAWValidationException("Tidak ada data penilaian warga untuk periode " + periode);
        }

        // 3. Validasi Kelengkapan Data
        for (Map.Entry<Integer, Map<Integer, Double>> entry : matriksKeputusan.entrySet()) {
            int idWarga = entry.getKey();
            Map<Integer, Double> nilaiWarga = entry.getValue();
            
            if (nilaiWarga.size() != kriteriaList.size()) {
                String nama = dataWarga.get(idWarga).getNama();
                throw new SAWValidationException("Data penilaian warga " + nama + " belum lengkap!\nPastikan semua warga telah dinilai untuk seluruh kriteria aktif.");
            }
        }

        // 4. Cari Max dan Min per Kriteria
        Map<Integer, Double> maxPerKriteria = new HashMap<>();
        Map<Integer, Double> minPerKriteria = new HashMap<>();
        
        for (KriteriaSAW k : kriteriaList) {
            int idK = k.getId();
            double max = Double.MIN_VALUE;
            double min = Double.MAX_VALUE;
            
            for (Map<Integer, Double> nilaiWarga : matriksKeputusan.values()) {
                double val = nilaiWarga.get(idK);
                if (val > max) max = val;
                if (val < min) min = val;
            }
            
            maxPerKriteria.put(idK, max);
            minPerKriteria.put(idK, min);
        }

        // 5. Normalisasi
        Map<Integer, Map<Integer, Double>> matriksNormalisasi = new HashMap<>();
        
        for (Map.Entry<Integer, Map<Integer, Double>> entry : matriksKeputusan.entrySet()) {
            int idWarga = entry.getKey();
            Map<Integer, Double> nilaiMentahWarga = entry.getValue();
            Map<Integer, Double> normWarga = new HashMap<>();
            
            for (KriteriaSAW k : kriteriaList) {
                int idK = k.getId();
                double nilaiMentah = nilaiMentahWarga.get(idK);
                double normVal = 0;
                
                if (k.getAtribut().equalsIgnoreCase("benefit")) {
                    double max = maxPerKriteria.get(idK);
                    if (max == 0) {
                        throw new SAWValidationException("Nilai maksimum kriteria " + k.getNama() + " adalah 0. Tidak bisa melakukan normalisasi benefit.");
                    }
                    normVal = nilaiMentah / max;
                } else { // cost
                    double min = minPerKriteria.get(idK);
                    if (nilaiMentah == 0) {
                        throw new SAWValidationException("Nilai kriteria " + k.getNama() + " untuk warga " + dataWarga.get(idWarga).getNama() + " adalah 0. Tidak bisa melakukan pembagian cost.");
                    }
                    normVal = min / nilaiMentah;
                }
                normWarga.put(idK, normVal);
            }
            matriksNormalisasi.put(idWarga, normWarga);
        }

        // 6. Hitung Skor V dan Gabungkan Hasil
        List<HasilSAW> hasilList = new ArrayList<>();
        
        for (Map.Entry<Integer, Map<Integer, Double>> entry : matriksNormalisasi.entrySet()) {
            int idWarga = entry.getKey();
            Map<Integer, Double> normWarga = entry.getValue();
            
            double skorV = 0;
            for (KriteriaSAW k : kriteriaList) {
                int idK = k.getId();
                double bobotDesimal = k.getBobot() / 100.0;
                skorV += bobotDesimal * normWarga.get(idK);
            }
            
            HasilSAW hasil = dataWarga.get(idWarga);
            hasil.setSkorV(skorV);
            hasil.setNilaiMentahPerKriteria(matriksKeputusan.get(idWarga));
            hasil.setNilaiNormalisasiPerKriteria(normWarga);
            hasilList.add(hasil);
        }

        // 7. Urutkan dari tertinggi ke terendah dan assign peringkat
        hasilList.sort(Comparator.comparingDouble(HasilSAW::getSkorV).reversed());
        for (int i = 0; i < hasilList.size(); i++) {
            hasilList.get(i).setPeringkat(i + 1);
        }

        return hasilList;
    }
}
