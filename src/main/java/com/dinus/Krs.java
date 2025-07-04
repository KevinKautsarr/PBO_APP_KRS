package com.dinus;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Krs {
    private final StringProperty kodeMk;
    private final StringProperty kelas;
    private final StringProperty nim;
    private final StringProperty status;

    public Krs(String kodeMk, String kelas, String nim, String status) {
        this.kodeMk = new SimpleStringProperty(kodeMk);
        this.kelas = new SimpleStringProperty(kelas);
        this.nim = new SimpleStringProperty(nim);
        this.status = new SimpleStringProperty(status);
    }

    public String getKodeMk() { return kodeMk.get(); }
    public StringProperty kodeMkProperty() { return kodeMk; }

    public String getKelas() { return kelas.get(); }
    public StringProperty kelasProperty() { return kelas; }

    public String getNim() { return nim.get(); }
    public StringProperty nimProperty() { return nim; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
}
