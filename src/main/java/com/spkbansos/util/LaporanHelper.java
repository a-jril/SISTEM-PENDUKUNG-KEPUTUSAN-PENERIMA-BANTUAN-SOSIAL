package com.spkbansos.util;

import com.spkbansos.dao.DatabaseHelper;
import com.spkbansos.dao.KriteriaDAO;
import com.spkbansos.dao.WargaDAO;
import com.spkbansos.model.HasilSAW;
import com.spkbansos.model.KriteriaSAW;
import com.spkbansos.model.Warga;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class untuk generate dan menampilkan laporan PDF menggunakan
 * JasperReports.
 * Semua laporan di-compile dari .jrxml yang ada di resources/laporan/.
 */
public class LaporanHelper {

    private static final String NAMA_DESA = "Desa Sukarame";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy",
            new java.util.Locale("id", "ID"));

    // =========================================================================
    // 1. Laporan Buku Induk Data Warga
    // =========================================================================

    public static void cetakLaporanWarga() throws Exception {
        WargaDAO wargaDAO = new WargaDAO();
        List<Warga> listWarga = wargaDAO.getAll();

        Map<String, Object> params = new HashMap<>();
        params.put("NAMA_DESA", NAMA_DESA);
        params.put("TANGGAL_CETAK", LocalDate.now().format(DATE_FORMATTER));
        params.put("TOTAL_WARGA", String.valueOf(listWarga.size()));

        JasperPrint jasperPrint = fillReport("laporan_warga", params,
                new JRBeanCollectionDataSource(listWarga));

        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setTitle("Buku Induk Data Warga - " + NAMA_DESA);
        viewer.setVisible(true);
    }

    // =========================================================================
    // 2. Laporan Daftar Kriteria SAW
    // =========================================================================

    public static void cetakLaporanKriteria() throws Exception {
        KriteriaDAO kriteriaDAO = new KriteriaDAO();
        List<KriteriaSAW> listKriteria = kriteriaDAO.getAll();

        // Bungkus ke bean khusus agar field 'statusAktif' (String) tersedia
        List<KriteriaReportBean> beans = new ArrayList<>();
        double totalBobot = 0;
        for (KriteriaSAW k : listKriteria) {
            beans.add(new KriteriaReportBean(k));
            if (k.getIsAktif() == 1)
                totalBobot += k.getBobot();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("NAMA_DESA", NAMA_DESA);
        params.put("TANGGAL_CETAK", LocalDate.now().format(DATE_FORMATTER));
        params.put("TOTAL_BOBOT", String.format("%.1f", totalBobot));

        JasperPrint jasperPrint = fillReport("laporan_kriteria", params,
                new JRBeanCollectionDataSource(beans));

        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setTitle("Daftar Kriteria SAW - " + NAMA_DESA);
        viewer.setVisible(true);
    }

    // =========================================================================
    // 3. Laporan Rekomendasi Penerima Bansos
    // =========================================================================

    public static void cetakLaporanRekomendasi(List<HasilSAW> hasilList,
            String periode,
            String operatorNama) throws Exception {
        // Bungkus ke bean agar field 'rekomendasi' (String) tersedia
        List<HasilReportBean> beans = new ArrayList<>();
        long totalLayak = 0;
        long totalTidakLayak = 0;
        for (HasilSAW h : hasilList) {
            String rek = h.getSkorV() >= 0.6 ? "Layak" : "Tidak Layak";
            beans.add(new HasilReportBean(h, rek));
            if (rek.equals("Layak"))
                totalLayak++;
            else
                totalTidakLayak++;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("NAMA_DESA", NAMA_DESA);
        params.put("TANGGAL_CETAK", LocalDate.now().format(DATE_FORMATTER));
        params.put("PERIODE", periode);
        params.put("THRESHOLD", "0.6000");
        params.put("TOTAL_LAYAK", String.valueOf(totalLayak));
        params.put("TOTAL_TIDAK_LAYAK", String.valueOf(totalTidakLayak));
        params.put("OPERATOR_NAMA", operatorNama != null ? operatorNama : "Administrator");

        JasperPrint jasperPrint = fillReport("laporan_rekomendasi", params,
                new JRBeanCollectionDataSource(beans));

        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setTitle("Laporan Rekomendasi Bansos - Periode " + periode);
        viewer.setVisible(true);
    }

    // =========================================================================
    // Private helper: Load, compile, dan fill template JRXML
    // =========================================================================

    private static JasperPrint fillReport(String templateName,
            Map<String, Object> params,
            JRDataSource dataSource) throws JRException {
        String resourcePath = "/laporan/" + templateName + ".jrxml";
        InputStream stream = LaporanHelper.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new JRException("Template laporan tidak ditemukan: " + resourcePath);
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(stream);
        return JasperFillManager.fillReport(jasperReport, params, dataSource);
    }

    // =========================================================================
    // Bean wrapper untuk KriteriaSAW (agar field 'statusAktif' tersedia)
    // =========================================================================

    public static class KriteriaReportBean {
        private final KriteriaSAW k;

        public KriteriaReportBean(KriteriaSAW k) {
            this.k = k;
        }

        public String getKode() {
            return k.getKode();
        }

        public String getNama() {
            return k.getNama();
        }

        public String getAtribut() {
            return k.getAtribut();
        }

        public Double getBobot() {
            return k.getBobot();
        }

        public String getKeterangan() {
            return k.getKeterangan() != null ? k.getKeterangan() : "-";
        }

        public String getStatusAktif() {
            return k.getIsAktif() == 1 ? "Aktif" : "Nonaktif";
        }
    }

    // =========================================================================
    // Bean wrapper untuk HasilSAW (agar field 'rekomendasi' tersedia)
    // =========================================================================

    public static class HasilReportBean {
        private final HasilSAW h;
        private final String rekomendasi;

        public HasilReportBean(HasilSAW h, String rekomendasi) {
            this.h = h;
            this.rekomendasi = rekomendasi;
        }

        public Integer getPeringkat() {
            return h.getPeringkat();
        }

        public String getNik() {
            return h.getNik();
        }

        public String getNama() {
            return h.getNama();
        }

        public Double getSkorV() {
            return h.getSkorV();
        }

        public String getRekomendasi() {
            return rekomendasi;
        }
    }
}
