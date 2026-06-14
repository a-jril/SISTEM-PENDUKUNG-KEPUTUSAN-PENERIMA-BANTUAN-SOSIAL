package com.spkbansos.model;

import java.util.Map;

public class HasilSAW {
    private int peringkat;
    private int idWarga;
    private String nik;
    private String nama;
    private double skorV;
    private Map<Integer, Double> nilaiMentahPerKriteria;
    private Map<Integer, Double> nilaiNormalisasiPerKriteria;

    public HasilSAW(int idWarga, String nik, String nama) {
        this.idWarga = idWarga;
        this.nik = nik;
        this.nama = nama;
    }

    public int getPeringkat() { return peringkat; }
    public void setPeringkat(int peringkat) { this.peringkat = peringkat; }

    public int getIdWarga() { return idWarga; }
    
    public String getNik() { return nik; }
    
    public String getNama() { return nama; }
    
    public double getSkorV() { return skorV; }
    public void setSkorV(double skorV) { this.skorV = skorV; }

    public Map<Integer, Double> getNilaiMentahPerKriteria() { return nilaiMentahPerKriteria; }
    public void setNilaiMentahPerKriteria(Map<Integer, Double> nilaiMentahPerKriteria) {
        this.nilaiMentahPerKriteria = nilaiMentahPerKriteria;
    }

    public Map<Integer, Double> getNilaiNormalisasiPerKriteria() { return nilaiNormalisasiPerKriteria; }
    public void setNilaiNormalisasiPerKriteria(Map<Integer, Double> nilaiNormalisasiPerKriteria) {
        this.nilaiNormalisasiPerKriteria = nilaiNormalisasiPerKriteria;
    }
}
