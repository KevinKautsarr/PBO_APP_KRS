package com.dinus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomeController {

    @FXML
    private Label label;

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        label.setText("Selamat Datang di Sistem KRS UDINUS!");
        label.setText("KRS UDINUS!");
        imageView.setImage(new Image(getClass().getResourceAsStream("/img/student1.jpg")));
        System.out.println(getClass().getResource("/img/student1.jpg")); 
    }
}
