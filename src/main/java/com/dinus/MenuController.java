package com.dinus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MenuController implements Initializable {
    @FXML private Button btnSelesai;
    @FXML private Button btnHome;
    @FXML private Button btnLogout;
    @FXML private Button btnMhs;
    @FXML private Button btnMatkul;
    @FXML private Button btnJadwal;
    @FXML private Button btnKrs;
    @FXML private Button btnDosen;
    @FXML private StackPane contentArea;
    @FXML private Label lbTeks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bisa isi default load content di sini
    }

    public void logout(ActionEvent event) {
        DBUtil.changeScene(event, "fLogin.fxml", "Login", null);
    }

    private void resetButtonColors() {
        String defaultStyle = "-fx-background-color: DodgerBlue; -fx-text-fill: white;";
        btnHome.setStyle(defaultStyle);
        btnMhs.setStyle(defaultStyle);
        btnMatkul.setStyle(defaultStyle);
        btnJadwal.setStyle(defaultStyle);
        btnKrs.setStyle(defaultStyle);
        btnDosen.setStyle(defaultStyle);
        btnSelesai.setStyle(defaultStyle);
    }

    private void setActive(Button activeBtn) {
        resetButtonColors();
        activeBtn.setStyle("-fx-background-color: #003366; -fx-text-fill: white;");
    }

    @FXML public void homee() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("fHome.fxml"));
        contentArea.getChildren().setAll(fxml);
        setActive(btnHome);
    }

    @FXML public void mhs() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("fmhs.fxml"));
        contentArea.getChildren().setAll(fxml);
        setActive(btnMhs);
    }

    @FXML public void matkul() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("fMatkul.fxml"));
        contentArea.getChildren().setAll(fxml);
        setActive(btnMatkul);
    }

    @FXML public void jadwal() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("fJadwal.fxml"));
        contentArea.getChildren().setAll(fxml);
        setActive(btnJadwal);
    }

    @FXML public void krs() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("fKrs.fxml"));
        contentArea.getChildren().setAll(fxml);
        setActive(btnKrs);
    }

    @FXML public void dosen() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("fDosen.fxml"));
        contentArea.getChildren().setAll(fxml);
        setActive(btnDosen);
    }

    @FXML public void selesai(ActionEvent event) throws IOException {
        DBUtil.changeScene(event, "fLogin.fxml", "Login", null);
    }

    public void setUserInfo(String userName) {
        lbTeks.setText("Selamat Datang " + userName);
    }
}
