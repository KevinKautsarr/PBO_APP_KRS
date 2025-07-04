package com.dinus;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MatkulController implements Initializable {
    ObservableList<Matakuliah> listMatakuliah = FXCollections.observableArrayList();
    FilteredList<Matakuliah> filterData;
    boolean flagEdit = false;

    @FXML private TableView<Matakuliah> tvMatkul;
    @FXML private TableColumn<Matakuliah, String> kodeMk;
    @FXML private TableColumn<Matakuliah, String> namaMk;
    @FXML private TableColumn<Matakuliah, Integer> sks;
    @FXML private TextField tfKodeMk, tfNamaMk, tfSks, tfCariMatkul;
    @FXML private Button btnAdd, btnEdit, btnDel, btnUpdate, btnCancel;
    @FXML private Label lblErr;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kodeMk.setCellValueFactory(new PropertyValueFactory<>("kodeMk"));
        namaMk.setCellValueFactory(new PropertyValueFactory<>("namaMk"));
        sks.setCellValueFactory(new PropertyValueFactory<>("sks"));

        tvMatkul.setItems(listMatakuliah);
        loadDataFromDatabase();
        setFilter();

        buttonAktif(false);
        teksAktif(false);
        lblErr.setText("");
    }

    private void loadDataFromDatabase() {
        listMatakuliah.clear();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/dbuniv", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM matakuliah")) {
            while (rs.next()) {
                listMatakuliah.add(new Matakuliah(
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getInt("sks")
                ));
            }
            setFilter();
        } catch (SQLException e) {
            lblErr.setText("Gagal load data: " + e.getMessage());
        }
    }

    private void setFilter() {
        filterData = new FilteredList<>(listMatakuliah, b -> true);

        tfCariMatkul.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(matkul -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String keyword = newValue.toLowerCase();
                return matkul.getNamaMk().toLowerCase().contains(keyword)
                        || matkul.getKodeMk().toLowerCase().contains(keyword);
            });
        });

        SortedList<Matakuliah> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(tvMatkul.comparatorProperty());
        tvMatkul.setItems(sortedData);
    }

    @FXML
    void add(ActionEvent event) {
        flagEdit = false;
        teksAktif(true);
        buttonAktif(true);
        tfKodeMk.requestFocus();
    }

    @FXML
    void edit(ActionEvent event) {
        int idx = tvMatkul.getSelectionModel().getSelectedIndex();
        if (idx == -1) {
            lblErr.setText("Pilih data yang akan diedit!");
            return;
        }
        flagEdit = true;
        teksAktif(true);
        buttonAktif(true);
        Matakuliah m = tvMatkul.getItems().get(idx);
        tfKodeMk.setText(m.getKodeMk());
        tfNamaMk.setText(m.getNamaMk());
        tfSks.setText(String.valueOf(m.getSks()));
        tfKodeMk.setEditable(false);
        tfNamaMk.requestFocus();
    }

    @FXML
    void delete(ActionEvent event) {
        int idx = tvMatkul.getSelectionModel().getSelectedIndex();
        if (idx == -1) {
            lblErr.setText("Pilih data yang akan dihapus!");
            return;
        }
        Matakuliah m = tvMatkul.getItems().get(idx);
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/dbuniv", "root", "");
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM matakuliah WHERE kode_mk = ?")) {
            ps.setString(1, m.getKodeMk());
            ps.executeUpdate();
            listMatakuliah.remove(m);
            lblErr.setText("Data berhasil dihapus.");
        } catch (SQLException e) {
            lblErr.setText("Gagal hapus data: " + e.getMessage());
        }
        clearTeks();
    }

    @FXML
    void update(ActionEvent event) {
        String vKodeMk = tfKodeMk.getText().trim();
        String vNamaMk = tfNamaMk.getText().trim();
        int vSks;
        try {
            vSks = Integer.parseInt(tfSks.getText().trim());
        } catch (NumberFormatException e) {
            lblErr.setText("SKS harus berupa angka!");
            return;
        }

        if (vKodeMk.isEmpty() || vNamaMk.isEmpty()) {
            lblErr.setText("Kode MK dan Nama MK harus diisi!");
            return;
        }

        if (!flagEdit) {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dbuniv", "root", "");
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO matakuliah (kode_mk, nama_mk, sks) VALUES (?, ?, ?)")) {
                ps.setString(1, vKodeMk);
                ps.setString(2, vNamaMk);
                ps.setInt(3, vSks);
                ps.executeUpdate();
                listMatakuliah.add(new Matakuliah(vKodeMk, vNamaMk, vSks));
                lblErr.setText("Data berhasil ditambah.");
            } catch (SQLException e) {
                lblErr.setText("Gagal tambah data: " + e.getMessage());
            }
        } else {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dbuniv", "root", "");
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE matakuliah SET nama_mk=?, sks=? WHERE kode_mk=?")) {
                ps.setString(1, vNamaMk);
                ps.setInt(2, vSks);
                ps.setString(3, vKodeMk);
                ps.executeUpdate();
                int idx = tvMatkul.getSelectionModel().getSelectedIndex();
                Matakuliah m = listMatakuliah.get(idx);
                m.setNamaMk(vNamaMk);
                m.setSks(vSks);
                tvMatkul.refresh();
                lblErr.setText("Data berhasil diupdate.");
            } catch (SQLException e) {
                lblErr.setText("Gagal update data: " + e.getMessage());
            }
        }
        clearTeks();
        teksAktif(false);
        buttonAktif(false);
    }

    @FXML
    void cancel(ActionEvent event) {
        clearTeks();
        teksAktif(false);
        buttonAktif(false);
        lblErr.setText("");
    }

    public void buttonAktif(boolean nonAktif) {
        btnAdd.setDisable(nonAktif);
        btnEdit.setDisable(nonAktif);
        btnDel.setDisable(nonAktif);
        btnUpdate.setDisable(!nonAktif);
        btnCancel.setDisable(!nonAktif);
    }

    public void teksAktif(boolean aktif) {
        tfKodeMk.setEditable(aktif);
        tfNamaMk.setEditable(aktif);
        tfSks.setEditable(aktif);
    }

    public void clearTeks() {
        tfKodeMk.setText("");
        tfNamaMk.setText("");
        tfSks.setText("");
        tfKodeMk.setEditable(true);
    }
}
