package com.dinus;

import java.util.ResourceBundle;

//import com.sun.jdi.Value;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import java.net.URL;
//import java.sql.Connection;
//import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class MhsController implements Initializable {
    ObservableList<Mhs> listMhs ;
    boolean flagEdit;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDel;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnUpdate;


    @FXML
    private Label lblErr;

    @FXML
    private TextField tfAlamat;

    @FXML
    private TextField tfNama;

    @FXML
    private TextField tfNim;

    @FXML
    private TextField tfCariNama;

    @FXML
    private TableColumn<Mhs, String> alamat;

    @FXML
    private TableColumn<Mhs, String> nama;

    @FXML
    private TableColumn<Mhs, String> nim;

    @FXML
    private TableView<Mhs> tvMhs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listMhs = FXCollections.observableArrayList() ;        
        initTabel();
        loadData();
        setFilter();
        buttonAktif(false);
        teksAktif(false);
        flagEdit=false;
    } 

    @FXML
    void add(ActionEvent event) {
        flagEdit=false;
        teksAktif(true);
        buttonAktif(true);
        tfNim.requestFocus();
    }
    @FXML
    void cancel(ActionEvent event) {
        teksAktif(false);
        clearTeks();
        buttonAktif(false);  
    }
    @FXML
    void delete(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "hapus data ?", ButtonType.YES,  ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            int idx=tvMhs.getSelectionModel().getSelectedIndex();
            String nim=tvMhs.getItems().get(idx).getNim();
            AksesDB.delDataMhs(nim);

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Delete Data Sukses..");
            alert2.show();
            loadData();                       
        }
    }
    @FXML
    void edit(ActionEvent event) {        
        buttonAktif(true);
        teksAktif(true);
        flagEdit=true;			
        int idx=tvMhs.getSelectionModel().getSelectedIndex();
        tfNim.setText(tvMhs.getItems().get(idx).getNim());
        tfNama.setText(tvMhs.getItems().get(idx).getNama());
        tfAlamat.setText(tvMhs.getItems().get(idx).getAlamat());        
        tfNim.requestFocus();
    }
    @FXML
    void update(ActionEvent event) {
        String nim,nama,alamat,nim_lama;
        nim=tfNim.getText();
        nama=tfNama.getText();	
        alamat=tfAlamat.getText();        
        if (flagEdit==false){
            AksesDB.addDataMhs(nim,nama,alamat);	
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Insert Data Sukses..");
            alert.show();			
        }else {
            int idx=tvMhs.getSelectionModel().getSelectedIndex();
            nim_lama=tvMhs.getItems().get(idx).getNim();
            //tvMhs.getItems().set(idx,m);
            AksesDB.updateDataMhs(nim,nama,nim_lama,alamat);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Update Data Berhasil");
            alert.show();
        }
        loadData();
        flagEdit=false;
        teksAktif(false);
        clearTeks();
        buttonAktif(false);        
    }     
	public void buttonAktif(boolean nonAktif){
		btnAdd.setDisable(nonAktif);
		btnEdit.setDisable(nonAktif);
		btnDel.setDisable(nonAktif);
		btnUpdate.setDisable(!nonAktif);
		btnCancel.setDisable(!nonAktif);
	}     
	public void teksAktif(boolean aktif){
		tfNim.setEditable(aktif);
		tfNama.setEditable(aktif);
		tfAlamat.setEditable(aktif);
	}
	public void clearTeks(){
		tfNim.setText("");
		tfNama.setText("");
		tfAlamat.setText("");
	}    
    void initTabel(){
            nim.setCellValueFactory(new PropertyValueFactory<Mhs,String>("nim"));
            nama.setCellValueFactory(new PropertyValueFactory<Mhs,String>("nama"));
            alamat.setCellValueFactory(new PropertyValueFactory<Mhs,String>("alamat"));
    }
    void loadData(){
        listMhs=AksesDB.getDataMhs();
        tvMhs.setItems(listMhs);
    }
     void setFilter(){
        FilteredList<Mhs> filterData= new FilteredList<>(listMhs,b->true);
       tfCariNama.textProperty().addListener((observable,oldValue,newValue)->{
       filterData.setPredicate(Mhs->{
          if (newValue.isEmpty()  || newValue==null){
             return true;
           }
          String searchKeyword=newValue.toLowerCase();
          if (Mhs.getNama().toLowerCase().indexOf(searchKeyword)> -1){
              return true;
          }else if (Mhs.getNim().toLowerCase().indexOf(searchKeyword)>-1){
              return true;
          }else
              return false;
       });           
       });
       SortedList<Mhs> sortedData = new SortedList<>(filterData);
       sortedData.comparatorProperty().bind(tvMhs.comparatorProperty());
       tvMhs.setItems(sortedData);
    }
     
}
