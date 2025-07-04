package com.dinus;

public class Jadwal {
    private String kodeMk, namaMk, kelas, hari, jam, ruang, kodeDosen;

    public Jadwal(String kodeMk, String namaMk, String kelas, String hari, String jam, String ruang, String kodeDosen) {
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.kelas = kelas;
        this.hari = hari;
        this.jam = jam;
        this.ruang = ruang;
        this.kodeDosen = kodeDosen;
    }

    public String getKodeMk() { return kodeMk; }
    public String getNamaMk() { return namaMk; }
    public String getKelas() { return kelas; }
    public String getHari() { return hari; }
    public String getJam() { return jam; }
    public String getRuang() { return ruang; }
    public String getKodeDosen() { return kodeDosen; }

    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }
    public void setKelas(String kelas) { this.kelas = kelas; }
    public void setHari(String hari) { this.hari = hari; }
    public void setJam(String jam) { this.jam = jam; }
    public void setRuang(String ruang) { this.ruang = ruang; }
    public void setKodeDosen(String kodeDosen) { this.kodeDosen = kodeDosen; }
}
