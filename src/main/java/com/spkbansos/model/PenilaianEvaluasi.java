package com.spkbansos.model;

public class PenilaianEvaluasi {
    private int id;
    private int idWarga;
    private int idKriteria;
    private double nilai;
    private String periode;
    private String createdAt;

    public PenilaianEvaluasi() {}

    public PenilaianEvaluasi(int id, int idWarga, int idKriteria, double nilai, String periode, String createdAt) {
        this.id = id;
        this.idWarga = idWarga;
        this.idKriteria = idKriteria;
        this.nilai = nilai;
        this.periode = periode;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdWarga() { return idWarga; }
    public void setIdWarga(int idWarga) { this.idWarga = idWarga; }

    public int getIdKriteria() { return idKriteria; }
    public void setIdKriteria(int idKriteria) { this.idKriteria = idKriteria; }

    public double getNilai() { return nilai; }
    public void setNilai(double nilai) { this.nilai = nilai; }

    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
