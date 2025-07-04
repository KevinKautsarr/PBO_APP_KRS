package com.dinus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*
	Aplikasi KRS Sederhana dibuat untuk keperluan pengajaran matakuliah PBO di Fasilkom-UDINUS   mvn clean javafx:run 
    diharapkan mahasiswa dapatkan menambahkan fitur-fitur baru dan memperbaiki tampilan.	
    suprayogi@dsn.dinus.ac.id 
*/
public class App extends Application {
    double x,y;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        HBox root = (HBox)FXMLLoader.load(getClass().getResource("fLogin.fxml"));
        Scene scene = new Scene(root,600, 400);
        root.setOnMousePressed(e->{
            x= e.getSceneX();
            y= e.getSceneY();
        });
        root.setOnMouseDragged(e->{
            stage.setX(e.getScreenX() - x);
            stage.setY(e.getSceneX() -  y);
        });

        stage.setTitle("Login Sistem KRS!");
        stage.setScene(scene);
        stage.show();

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}