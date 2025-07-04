package com.dinus;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DosenController implements Initializable {

    ObservableList<Dosen> listDosen;
    boolean flagEdit = false;
    String kodeLama = "";

    @FXML private TextField tfKode, tfNama, tfCariDosen;
    @FXML private TableView<Dosen> tbDosen;
    @FXML private TableColumn<Dosen, String> colKode, colNama;
    @FXML private Button btnAdd, btnEdit, btnDel, btnUpdate, btnCancel;
    @FXML private Label lblErr;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listDosen = FXCollections.observableArrayList();
        initTabel();
        loadData();
        setFilter();
        formMode(false);
    }

    void initTabel() {
        colKode.setCellValueFactory(new PropertyValueFactory<>("kode"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
    }

    void loadData() {
        listDosen = AksesDB.getDataDosen();
        tbDosen.setItems(listDosen);
    }

    void setFilter() {
        FilteredList<Dosen> filtered = new FilteredList<>(listDosen, b -> true);
        tfCariDosen.textProperty().addListener((obs, oldVal, newVal) -> {
            filtered.setPredicate(d -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lc = newVal.toLowerCase();
                return d.getKode().toLowerCase().contains(lc) || d.getNama().toLowerCase().contains(lc);
            });
        });
        SortedList<Dosen> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tbDosen.comparatorProperty());
        tbDosen.setItems(sorted);
    }

    void formMode(boolean editMode) {
        tfKode.setEditable(editMode);
        tfNama.setEditable(editMode);
        btnUpdate.setDisable(!editMode);
        btnCancel.setDisable(!editMode);
        btnAdd.setDisable(editMode);
        btnEdit.setDisable(editMode);
        btnDel.setDisable(editMode);
    }

    void clearForm() {
        tfKode.clear();
        tfNama.clear();
        lblErr.setText("");
    }

    @FXML void add(ActionEvent e) {
        clearForm();
        formMode(true);
        flagEdit = false;
        tfKode.requestFocus();
    }

    @FXML void edit(ActionEvent e) {
        Dosen selected = tbDosen.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblErr.setText("Pilih data yang ingin diedit!");
            return;
        }
        tfKode.setText(selected.getKode());
        tfNama.setText(selected.getNama());
        kodeLama = selected.getKode();
        flagEdit = true;
        formMode(true);
    }

    @FXML void delete(ActionEvent e) {
        Dosen selected = tbDosen.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblErr.setText("Pilih data yang ingin dihapus!");
            return;
        }
        if (new Alert(Alert.AlertType.CONFIRMATION, "Hapus data ini?", ButtonType.YES, ButtonType.NO)
                .showAndWait().get() == ButtonType.YES) {
            AksesDB.delDataDosen(selected.getKode());
            loadData();
            lblErr.setText("Data berhasil dihapus.");
        }
    }

    @FXML void update(ActionEvent e) {
        String kode = tfKode.getText().trim();
        String nama = tfNama.getText().trim();

        if (kode.isEmpty() || nama.isEmpty()) {
            lblErr.setText("Kode dan Nama wajib diisi!");
            return;
        }

        if (!flagEdit) {
            // Tambah data baru
            for (Dosen d : listDosen) {
                if (d.getKode().equalsIgnoreCase(kode)) {
                    lblErr.setText("Kode dosen sudah digunakan!");
                    return;
                }
            }
            AksesDB.addDataDosen(kode, nama);
            lblErr.setText("Data dosen berhasil ditambahkan.");
        } else {
            // Edit data
            if (!kode.equals(kodeLama)) {
                for (Dosen d : listDosen) {
                    if (d.getKode().equalsIgnoreCase(kode)) {
                        lblErr.setText("Kode baru sudah dipakai dosen lain!");
                        return;
                    }
                }
            }
            AksesDB.updateDataDosen(kode, nama, kodeLama);
            lblErr.setText("Data dosen berhasil diperbarui.");
        }

        loadData();
        clearForm();
        formMode(false);
    }

    @FXML void cancel(ActionEvent e) {
        clearForm();
        formMode(false);
    }
}
