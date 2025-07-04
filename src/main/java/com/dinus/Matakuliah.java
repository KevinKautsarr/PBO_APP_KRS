package com.dinus;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Matakuliah {
    private final StringProperty kodeMk;
    private final StringProperty namaMk;
    private final IntegerProperty sks;

    public Matakuliah(String kodeMk, String namaMk, int sks) {
        this.kodeMk = new SimpleStringProperty(kodeMk);
        this.namaMk = new SimpleStringProperty(namaMk);
        this.sks = new SimpleIntegerProperty(sks);
    }

    public String getKodeMk() { return kodeMk.get(); }
    public void setKodeMk(String value) { kodeMk.set(value); }
    public StringProperty kodeMkProperty() { return kodeMk; }

    public String getNamaMk() { return namaMk.get(); }
    public void setNamaMk(String value) { namaMk.set(value); }
    public StringProperty namaMkProperty() { return namaMk; }

    public int getSks() { return sks.get(); }
    public void setSks(int value) { sks.set(value); }
    public IntegerProperty sksProperty() { return sks; }
}