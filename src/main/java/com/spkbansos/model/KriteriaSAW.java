package com.spkbansos.model;

public class KriteriaSAW {
    private int id;
    private String kode;
    private String nama;
    private String atribut; // benefit / cost
    private double bobot;
    private String keterangan;
    private int isAktif; // 1 = aktif, 0 = nonaktif

    public KriteriaSAW() {}

    public KriteriaSAW(int id, String kode, String nama, String atribut, double bobot, String keterangan, int isAktif) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.atribut = atribut;
        this.bobot = bobot;
        this.keterangan = keterangan;
        this.isAktif = isAktif;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getAtribut() { return atribut; }
    public void setAtribut(String atribut) { this.atribut = atribut; }
    
    public double getBobot() { return bobot; }
    public void setBobot(double bobot) { this.bobot = bobot; }
    
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    
    public int getIsAktif() { return isAktif; }
    public void setIsAktif(int isAktif) { this.isAktif = isAktif; }
}
