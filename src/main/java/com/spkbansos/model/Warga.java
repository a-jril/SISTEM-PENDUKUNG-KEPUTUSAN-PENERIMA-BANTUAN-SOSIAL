package com.spkbansos.model;

public class Warga {
    private int id;
    private String nik;
    private String nama;
    private String alamat;
    private String rtRw;
    private String kelurahan;
    private String kecamatan;
    private String jenisKelamin;
    private String tglLahir;
    private String noTelp;

    public Warga() {}

    public Warga(int id, String nik, String nama, String alamat, String rtRw, 
                 String kelurahan, String kecamatan, String jenisKelamin, 
                 String tglLahir, String noTelp) {
        this.id = id;
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.rtRw = rtRw;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.jenisKelamin = jenisKelamin;
        this.tglLahir = tglLahir;
        this.noTelp = noTelp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    
    public String getRtRw() { return rtRw; }
    public void setRtRw(String rtRw) { this.rtRw = rtRw; }
    
    public String getKelurahan() { return kelurahan; }
    public void setKelurahan(String kelurahan) { this.kelurahan = kelurahan; }
    
    public String getKecamatan() { return kecamatan; }
    public void setKecamatan(String kecamatan) { this.kecamatan = kecamatan; }
    
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    
    public String getTglLahir() { return tglLahir; }
    public void setTglLahir(String tglLahir) { this.tglLahir = tglLahir; }
    
    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
}
