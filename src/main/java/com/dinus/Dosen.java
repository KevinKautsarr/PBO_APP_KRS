package com.dinus;

public class Dosen {
    private String kode;
    private String nama;
    
    // constructor
    public Dosen(String kode, String nama) {
        this.kode = kode;
        this.nama = nama;
    }
    
    public String getKode() {
        return kode;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setKode(String kode) {
        this.kode = kode;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
}