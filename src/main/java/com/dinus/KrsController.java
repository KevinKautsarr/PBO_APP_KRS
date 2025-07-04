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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class KrsController implements Initializable {
    @FXML private TableView<Krs> tvKrs;
    @FXML private TableColumn<Krs, String> colKodeMk, colKelas, colNim, colStatus;
    @FXML private TextField tfKodeMk, tfKelas, tfNim, tfStatus, tfCari;
    @FXML private Button btnAdd, btnDel, btnEdit, btnUpdate, btnCancel;
    @FXML private Label lblInfo;

    private final ObservableList<Krs> listKrs = FXCollections.observableArrayList();
    private FilteredList<Krs> filterData;
    private Krs selectedKrs;
    private boolean isEditMode = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKodeMk.setCellValueFactory(new PropertyValueFactory<>("kodeMk"));
        colKelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        colNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tvKrs.setItems(listKrs); 
        loadData(); 
        setFilter(); 
        setFormState(false);
    }

    private void loadData() {
        listKrs.clear(); 
        String query = "SELECT * FROM krs";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbuniv", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                listKrs.add(new Krs(
                        rs.getString("kode_mk"),
                        rs.getString("kelas"),
                        rs.getString("nim"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            lblInfo.setText("Error loading data: " + e.getMessage()); 
            System.err.println("SQL Error: " + e.getMessage()); 
        }
    }

    private void setFilter() {
        filterData = new FilteredList<>(listKrs, b -> true);

        tfCari.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(krs -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String keyword = newValue.toLowerCase();
                return krs.getNim().toLowerCase().contains(keyword)
                        || krs.getKodeMk().toLowerCase().contains(keyword);
            });
        });

        SortedList<Krs> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(tvKrs.comparatorProperty());
        tvKrs.setItems(sortedData); 
    }

    @FXML
    private void addKrs() {
        clearForm(); 
        isEditMode = false; 
        setFormState(true); 
        lblInfo.setText("Mode: Tambah data baru.");
        tfKodeMk.requestFocus(); 
    }

    @FXML
    private void editKrs() {
        selectedKrs = tvKrs.getSelectionModel().getSelectedItem(); 
        if (selectedKrs == null) {
            lblInfo.setText("Pilih data yang akan diedit."); 
            return;
        }

        tfKodeMk.setText(selectedKrs.getKodeMk());
        tfKelas.setText(selectedKrs.getKelas());
        tfNim.setText(selectedKrs.getNim());
        tfStatus.setText(selectedKrs.getStatus());


        tfKodeMk.setEditable(false);
        tfKelas.setEditable(false);
        tfNim.setEditable(false);

        isEditMode = true; 
        setFormState(true);
        lblInfo.setText("Mode: Edit data.");
        tfStatus.requestFocus(); 
    }

    @FXML
    private void updateKrs() {
        String kodeMk = tfKodeMk.getText().trim();
        String kelas = tfKelas.getText().trim();
        String nim = tfNim.getText().trim();
        String status = tfStatus.getText().trim();

        if (kodeMk.isEmpty() || kelas.isEmpty() || nim.isEmpty()) {
            lblInfo.setText("Semua field (Kode MK, Kelas, NIM) wajib diisi.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbuniv", "root", "")) {
            if (isEditMode) {
                String sql = "UPDATE krs SET status=? WHERE kode_mk=? AND kelas=? AND nim=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, status);
                    ps.setString(2, kodeMk);
                    ps.setString(3, kelas);
                    ps.setString(4, nim);
                    ps.executeUpdate();
                    lblInfo.setText("Data berhasil diupdate.");
                }
            } else {
                String sql = "INSERT INTO krs (kode_mk, kelas, nim, status) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, kodeMk);
                    ps.setString(2, kelas);
                    ps.setString(3, nim);
                    ps.setString(4, status.isEmpty() ? "baru" : status); 
                    ps.executeUpdate();
                    lblInfo.setText("Data berhasil ditambahkan.");
                }
            }
        } catch (SQLException e) {
            lblInfo.setText("Database error: " + e.getMessage());
            System.err.println("SQL Error on Update: " + e.getMessage());
        }

        loadData(); 
        clearForm(); 
        setFormState(false); 
    }

    @FXML
    private void deleteKrs() {
        Krs selected = tvKrs.getSelectionModel().getSelectedItem(); 
        if (selected == null) {
            lblInfo.setText("Pilih data yang akan dihapus.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbuniv", "root", "");
             PreparedStatement ps = conn.prepareStatement("DELETE FROM krs WHERE kode_mk=? AND kelas=? AND nim=?")) {
            ps.setString(1, selected.getKodeMk());
            ps.setString(2, selected.getKelas());
            ps.setString(3, selected.getNim());
            ps.executeUpdate();
            lblInfo.setText("Data berhasil dihapus.");
            loadData(); 
        } catch (SQLException e) {
            lblInfo.setText("Gagal hapus data: " + e.getMessage());
            System.err.println("SQL Error on Delete: " + e.getMessage());
        }
    }

    @FXML
    private void cancelAction() {
        clearForm();
        setFormState(false); 
        lblInfo.setText("Dibatalkan.");
    }

    private void clearForm() {
        tfKodeMk.clear();
        tfKelas.clear();
        tfNim.clear();
        tfStatus.clear();

    }

    private void setFormState(boolean editing) {

        tfKodeMk.setEditable(editing);
        tfKelas.setEditable(editing);
        tfNim.setEditable(editing);
        tfStatus.setEditable(editing);

        btnUpdate.setDisable(!editing); 
        btnCancel.setDisable(!editing);

        btnAdd.setDisable(editing);   
        btnEdit.setDisable(editing);
        btnDel.setDisable(editing);
    }
}
