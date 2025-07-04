package com.dinus;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class JadwalController implements Initializable {
    @FXML private TableView<Jadwal> tbJadwal;
    @FXML private TableColumn<Jadwal, String> kodeMk, namaMk, kelas, hari, jam, ruang, kodeDosen;
    @FXML private TextField tfKodematkul, tfNmMatkul, tfKelas, tfHari, tfJam, tfRuang, tfKodeDosen, tfCari;
    @FXML private Button btnAdd, btnEdit, btnDelete, btnUpdate, btnCancel, btnPilih;
    @FXML private Label lblInfo;
    @FXML private ProgressIndicator progressIndicator;

    private ObservableList<Jadwal> jadwalList = FXCollections.observableArrayList();
    private FilteredList<Jadwal> filterData;
    private Jadwal selectedJadwal;
    private boolean isEditMode = false;

    private static final Logger LOGGER = Logger.getLogger(JadwalController.class.getName());

    private static final String DB_URL = "jdbc:mysql://localhost:3306/dbuniv?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    private static final String SELECT_JADWAL_QUERY =
        "SELECT j.kode_mk, COALESCE(m.nama_mk, 'N/A') as nama_mk, j.kelas, j.hari, j.jam, j.ruang, j.kode_dsn AS kode_dosen " +
        "FROM jadwal j LEFT JOIN matakuliah m ON j.kode_mk = m.kode_mk ORDER BY j.kode_mk, j.kelas";
    
    private static final String INSERT_JADWAL_QUERY =
        "INSERT INTO jadwal (kode_mk, kelas, hari, jam, ruang, kode_dsn) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_JADWAL_QUERY =
        "UPDATE jadwal SET hari=?, jam=?, ruang=?, kode_dsn=? WHERE kode_mk=? AND kelas=?";
    
    private static final String DELETE_JADWAL_QUERY =
        "DELETE FROM jadwal WHERE kode_mk=? AND kelas=?";
    
    private static final String CHECK_JADWAL_EXISTS_QUERY_FOR_EDIT =
        "SELECT COUNT(*) FROM jadwal WHERE kode_mk = ? AND kelas = ? AND NOT (kode_mk = ? AND kelas = ?)";
    
    private static final String CHECK_JADWAL_EXISTS_QUERY_FOR_INSERT =
        "SELECT COUNT(*) FROM jadwal WHERE kode_mk = ? AND kelas = ?";

    private static final String SELECT_MATAKULIAH_QUERY =
        "SELECT kode_mk, nama_mk FROM matakuliah ORDER BY kode_mk";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        setupEventHandlers();
        loadDataAsync();
        setFilter();
        setFormState(false); // Initial state
        
        if (progressIndicator != null) {
            progressIndicator.setVisible(false);
        }
        
        lblInfo.setText("Aplikasi siap digunakan.");
    }

    private void setupTableColumns() {
        kodeMk.setCellValueFactory(new PropertyValueFactory<>("kodeMk"));
        namaMk.setCellValueFactory(new PropertyValueFactory<>("namaMk"));
        kelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        hari.setCellValueFactory(new PropertyValueFactory<>("hari"));
        jam.setCellValueFactory(new PropertyValueFactory<>("jam"));
        ruang.setCellValueFactory(new PropertyValueFactory<>("ruang"));
        kodeDosen.setCellValueFactory(new PropertyValueFactory<>("kodeDosen"));
    }

    private void setupEventHandlers() {
        // Fixed: Hanya simpan selectedJadwal, jangan langsung isi form
        tbJadwal.setOnMouseClicked(event -> {
            selectedJadwal = tbJadwal.getSelectionModel().getSelectedItem();
            if (selectedJadwal != null) {
                // Hanya simpan selectedJadwal, jangan isi form
                // Form hanya akan diisi ketika tombol Edit ditekan
                
                // Double click untuk edit (opsional)
                if (event.getClickCount() == 2 && !btnEdit.isDisabled()) {
                    edit();
                }
            }
        });
    }

    private void loadDataAsync() {
        if (progressIndicator != null) {
            progressIndicator.setVisible(true);
        }
        
        Task<ObservableList<Jadwal>> task = new Task<ObservableList<Jadwal>>() {
            @Override
            protected ObservableList<Jadwal> call() throws Exception {
                return loadDataFromDatabase();
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    jadwalList.setAll(getValue());
                    updateTableData();
                    
                    if (progressIndicator != null) {
                        progressIndicator.setVisible(false);
                    }
                    
                    lblInfo.setText("Data berhasil dimuat. Total: " + jadwalList.size() + " jadwal.");
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    if (progressIndicator != null) {
                        progressIndicator.setVisible(false);
                    }
                    
                    Throwable exception = getException();
                    LOGGER.log(Level.SEVERE, "Error loading data", exception);
                    showAlert("Error Database", "Gagal memuat data: " + exception.getMessage(), Alert.AlertType.ERROR);
                    lblInfo.setText("Gagal memuat data dari database.");
                });
            }
        };
        
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private ObservableList<Jadwal> loadDataFromDatabase() throws SQLException {
        ObservableList<Jadwal> data = FXCollections.observableArrayList();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_JADWAL_QUERY)) {

            while (rs.next()) {
                data.add(new Jadwal(
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getString("kelas"),
                        rs.getString("hari"),
                        rs.getString("jam"),
                        rs.getString("ruang"),
                        rs.getString("kode_dosen")
                ));
            }
        }
        
        return data;
    }

    private void updateTableData() {
        if (filterData != null) {
            filterData = new FilteredList<>(jadwalList, b -> true);
            setFilter();
        } else {
            tbJadwal.setItems(jadwalList);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void setFilter() {
        filterData = new FilteredList<>(jadwalList, b -> true);
        
        tfCari.textProperty().addListener((obs, oldVal, newVal) -> {
            filterData.setPredicate(jadwal -> {
                if (newVal == null || newVal.trim().isEmpty()) {
                    return true;
                }
                
                String searchText = newVal.toLowerCase().trim();
                
                return containsIgnoreCase(jadwal.getKodeMk(), searchText)
                        || containsIgnoreCase(jadwal.getNamaMk(), searchText)
                        || containsIgnoreCase(jadwal.getKelas(), searchText)
                        || containsIgnoreCase(jadwal.getHari(), searchText)
                        || containsIgnoreCase(jadwal.getJam(), searchText)
                        || containsIgnoreCase(jadwal.getRuang(), searchText)
                        || containsIgnoreCase(jadwal.getKodeDosen(), searchText);
            });
        });
        
        SortedList<Jadwal> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(tbJadwal.comparatorProperty());
        tbJadwal.setItems(sortedData);
    }

    private boolean containsIgnoreCase(String source, String target) {
        return source != null && source.toLowerCase().contains(target.toLowerCase());
    }
    
    private void populateForm(Jadwal jadwal) {
        tfKodematkul.setText(jadwal.getKodeMk());
        tfNmMatkul.setText(jadwal.getNamaMk());
        tfKelas.setText(jadwal.getKelas());
        tfHari.setText(jadwal.getHari());
        tfJam.setText(jadwal.getJam());
        tfRuang.setText(jadwal.getRuang());
        tfKodeDosen.setText(jadwal.getKodeDosen() != null ? jadwal.getKodeDosen() : "");
    }

    @FXML 
    private void add() {
        clearForm();
        isEditMode = false;
        setFormState(true);
        
        tfKodematkul.requestFocus();
        lblInfo.setText("Mode: Menambah jadwal baru. Isi semua field yang diperlukan.");
    }

    @FXML 
    private void edit() {
        selectedJadwal = tbJadwal.getSelectionModel().getSelectedItem();
        if (selectedJadwal == null) {
            lblInfo.setText("Pilih jadwal yang ingin diedit dari tabel terlebih dahulu.");
            return;
        }
        
        populateForm(selectedJadwal);
        isEditMode = true;
        setFormState(true);
        
        // Primary key fields tidak bisa diedit dalam mode edit
        tfKodematkul.setEditable(false); 
        tfKelas.setEditable(false);       
        
        tfHari.requestFocus();
        lblInfo.setText("Mode: Mengedit jadwal " + selectedJadwal.getKodeMk() + " - " + selectedJadwal.getKelas());
    }

    @FXML 
    private void update() {
        if (!validateForm()) {
            return;
        }
        
        String kode = tfKodematkul.getText().trim();
        String kelasStr = tfKelas.getText().trim();
        String hariStr = tfHari.getText().trim();
        String jamStr = tfJam.getText().trim();
        String ruangStr = tfRuang.getText().trim();
        String dosen = tfKodeDosen.getText().trim();

        try (Connection conn = getConnection()) {
            
            if (isEditMode) {
                updateExistingJadwal(conn, kode, kelasStr, hariStr, jamStr, ruangStr, dosen);
            } else {
                insertNewJadwal(conn, kode, kelasStr, hariStr, jamStr, ruangStr, dosen);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during update operation", e);
            showAlert("Error Database", "Gagal menyimpan data: " + e.getMessage(), Alert.AlertType.ERROR);
            lblInfo.setText("Error: " + e.getMessage());
            return;
        }

        loadDataAsync();
        clearForm();
        setFormState(false);
    }

    private void updateExistingJadwal(Connection conn, String kodeBaru, String kelasBaru, String hari, String jam, String ruang,
                                      String dosen) throws SQLException {
        if (!kodeBaru.equals(selectedJadwal.getKodeMk()) || !kelasBaru.equals(selectedJadwal.getKelas())) {
            if (isJadwalExistsForEdit(conn, kodeBaru, kelasBaru, selectedJadwal.getKodeMk(), selectedJadwal.getKelas())) {
                showAlert("Error", "Kombinasi Kode Matakuliah dan Kelas baru sudah ada! Tidak dapat memperbarui.", Alert.AlertType.ERROR);
                return;
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_JADWAL_QUERY)) {
            ps.setString(1, hari);
            ps.setString(2, jam);
            ps.setString(3, ruang);
            ps.setString(4, dosen.isEmpty() ? null : dosen);
            ps.setString(5, selectedJadwal.getKodeMk());
            ps.setString(6, selectedJadwal.getKelas());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Sukses", "Jadwal berhasil diperbarui!", Alert.AlertType.INFORMATION);
                lblInfo.setText("Jadwal " + selectedJadwal.getKodeMk() + " - " + selectedJadwal.getKelas() + " berhasil diperbarui.");
            } else {
                showAlert("Error", "Tidak ada data yang diperbarui. Periksa kembali data atau pastikan ada perubahan.", Alert.AlertType.ERROR);
            }
        }
    }

    private void insertNewJadwal(Connection conn, String kode, String kelas, String hari,
                                  String jam, String ruang, String dosen) throws SQLException {
        if (isJadwalExistsForInsert(conn, kode, kelas)) {
            showAlert("Error", "Kombinasi Kode Matakuliah dan Kelas sudah ada! Gunakan mode 'Edit' untuk mengubah data yang sudah ada.", Alert.AlertType.ERROR);
            return;
        }
        
        try (PreparedStatement ps = conn.prepareStatement(INSERT_JADWAL_QUERY)) {
            ps.setString(1, kode);
            ps.setString(2, kelas);
            ps.setString(3, hari);
            ps.setString(4, jam);
            ps.setString(5, ruang);
            ps.setString(6, dosen.isEmpty() ? null : dosen);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Sukses", "Jadwal baru berhasil ditambahkan!", Alert.AlertType.INFORMATION);
                lblInfo.setText("Jadwal " + kode + " - " + kelas + " berhasil ditambahkan.");
            } else {
                showAlert("Error", "Gagal menambahkan jadwal baru. Tidak ada baris yang terpengaruh.", Alert.AlertType.ERROR);
            }
        }
    }
    
    private boolean isJadwalExistsForInsert(Connection conn, String kodeMk, String kelas) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(CHECK_JADWAL_EXISTS_QUERY_FOR_INSERT)) {
            ps.setString(1, kodeMk);
            ps.setString(2, kelas);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    private boolean isJadwalExistsForEdit(Connection conn, String kodeMk, String kelas, String originalKode, String originalKelas) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(CHECK_JADWAL_EXISTS_QUERY_FOR_EDIT)) {
            ps.setString(1, kodeMk);
            ps.setString(2, kelas);
            ps.setString(3, originalKode);
            ps.setString(4, originalKelas);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @FXML 
    private void delete() {
        selectedJadwal = tbJadwal.getSelectionModel().getSelectedItem();
        if (selectedJadwal == null) {
            lblInfo.setText("Pilih jadwal yang ingin dihapus dari tabel terlebih dahulu.");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Penghapusan");
        confirmAlert.setHeaderText("Hapus Jadwal");
        confirmAlert.setContentText("Apakah Anda yakin ingin menghapus jadwal:\n" + 
                                   "Kode MK: " + selectedJadwal.getKodeMk() + "\n" +
                                   "Mata Kuliah: " + selectedJadwal.getNamaMk() + "\n" +
                                   "Kelas: " + selectedJadwal.getKelas() + "?");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(DELETE_JADWAL_QUERY)) {
                
                ps.setString(1, selectedJadwal.getKodeMk());
                ps.setString(2, selectedJadwal.getKelas());
                
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert("Sukses", "Jadwal berhasil dihapus!", Alert.AlertType.INFORMATION);
                    lblInfo.setText("Jadwal " + selectedJadwal.getKodeMk() + " - " + selectedJadwal.getKelas() + " berhasil dihapus.");
                    
                    clearForm();
                    selectedJadwal = null;
                    loadDataAsync();
                } else {
                    showAlert("Error", "Tidak ada data yang dihapus. Data mungkin sudah tidak ada di database.", Alert.AlertType.ERROR);
                }
                
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database error during delete operation", e);
                showAlert("Error Database", "Gagal menghapus data: " + e.getMessage(), Alert.AlertType.ERROR);
                lblInfo.setText("Gagal menghapus jadwal: " + e.getMessage());
            }
        } else {
             lblInfo.setText("Penghapusan jadwal dibatalkan.");
        }
    }

    @FXML 
    private void cancel() {
        clearForm();
        selectedJadwal = null;
        isEditMode = false;
        setFormState(false);
        lblInfo.setText("Operasi dibatalkan.");
    }

    private void clearForm() {
        tfKodematkul.clear();
        tfNmMatkul.clear();
        tfKelas.clear();
        tfHari.clear();
        tfJam.clear();
        tfRuang.clear();
        tfKodeDosen.clear();
    }

    // Simplified form state management - sama seperti KrsController
    private void setFormState(boolean editing) {
        // Set text field editability
        tfKodematkul.setEditable(editing && !isEditMode); // Hanya bisa edit saat add mode
        tfKelas.setEditable(editing && !isEditMode);      // Hanya bisa edit saat add mode
        tfNmMatkul.setEditable(false);                    // Selalu read-only
        tfHari.setEditable(editing);
        tfJam.setEditable(editing);
        tfRuang.setEditable(editing);
        tfKodeDosen.setEditable(editing);

        // Set button states - sama persis seperti KrsController
        btnUpdate.setDisable(!editing);
        btnCancel.setDisable(!editing);
        
        btnAdd.setDisable(editing);
        btnEdit.setDisable(editing);
        btnDelete.setDisable(editing);
        
        // Tombol Pilih hanya aktif saat mode add
        btnPilih.setDisable(!(editing && !isEditMode));
    }
    
    private boolean validateForm() {
        return validateField(tfKodematkul, "Kode Matakuliah") &&
                validateField(tfNmMatkul, "Nama Matakuliah") &&
                validateField(tfKelas, "Kelas") &&
                validateField(tfHari, "Hari") &&
                validateField(tfJam, "Jam") &&
                validateField(tfRuang, "Ruang");
    }

    private boolean validateField(TextField field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            showAlert("Validasi", fieldName + " harus diisi!", Alert.AlertType.WARNING);
            field.requestFocus();
            return false;
        }
        return true;
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void pilihMatkul() {
        if (isEditMode) { 
            showAlert("Info", "Tombol 'Pilih Matakuliah' hanya tersedia saat menambah jadwal baru.", Alert.AlertType.INFORMATION);
            return;
        }
        showMatkulSelectionDialog();
    }
    
    private void showMatkulSelectionDialog() {
        try {
            ObservableList<String> matkulList = FXCollections.observableArrayList();
            
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SELECT_MATAKULIAH_QUERY)) {
                
                while (rs.next()) {
                    String kode = rs.getString("kode_mk");
                    String nama = rs.getString("nama_mk");
                    matkulList.add(kode + " - " + nama);
                }
            }
            
            if (matkulList.isEmpty()) {
                showAlert("Info", "Tidak ada data matakuliah yang tersedia di database.", Alert.AlertType.INFORMATION);
                return;
            }
            
            ChoiceDialog<String> dialog = new ChoiceDialog<>(matkulList.isEmpty() ? null : matkulList.get(0), matkulList);
            dialog.setTitle("Pilih Matakuliah");
            dialog.setHeaderText("Pilih Matakuliah dari daftar:");
            dialog.setContentText("Matakuliah:");
            
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(selected -> {
                String[] parts = selected.split(" - ", 2);
                if (parts.length >= 2) {
                    String kodeMk = parts[0].trim();
                    String namaMk = parts[1].trim();
                    
                    tfKodematkul.setText(kodeMk);
                    tfNmMatkul.setText(namaMk);
                    
                    lblInfo.setText("Matakuliah " + kodeMk + " (" + namaMk + ") telah dipilih.");
                    tfKelas.requestFocus();
                }
            });
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error loading matakuliah for selection", e);
            showAlert("Error Database", "Gagal memuat data matakuliah untuk pemilihan: " + e.getMessage(), Alert.AlertType.ERROR);
            lblInfo.setText("Error memuat matakuliah: " + e.getMessage());
        }
    }

    public void refreshData() {
        loadDataAsync();
    }
}