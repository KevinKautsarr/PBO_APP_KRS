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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import java.net.URL;

//import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class MatkulSearchController implements Initializable {
    ObservableList<Matakuliah> listMatakuliah ;
    Stage stage;

    @FXML
    private Label lblErr;
    @FXML
    private Button btnPilih;
    @FXML
    private TextField tfCariNama;

    @FXML
    private TableColumn<Matakuliah, Integer> sks;

    @FXML
    private TableColumn<Matakuliah, String> namaMk;

    @FXML
    private TableColumn<Matakuliah, String> kodeMk;

    @FXML
    private TableView<Matakuliah> tvMatakuliah;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listMatakuliah = FXCollections.observableArrayList() ;        
        initTabel();
        loadData();
        //setFilter();
    } 

   
    void initTabel(){
            kodeMk.setCellValueFactory(new PropertyValueFactory<Matakuliah,String>("kodeMk"));
            namaMk.setCellValueFactory(new PropertyValueFactory<Matakuliah,String>("namaMk"));
            sks.setCellValueFactory(new PropertyValueFactory<Matakuliah,Integer>("sks"));
    }

    void loadData(){
        listMatakuliah=AksesDB.getDataMatakuliah();
        tvMatakuliah.setItems(listMatakuliah);
    }
     void setFilter(){
        FilteredList<Matakuliah> filterData= new FilteredList<>(listMatakuliah,b->true);
       tfCariNama.textProperty().addListener((observable,oldValue,newValue)->{
       filterData.setPredicate(Matakuliah->{
          if (newValue.isEmpty()  || newValue==null){
             return true;
           }
          String searchKeyword=newValue.toLowerCase();
          if (Matakuliah.getNamaMk().toLowerCase().indexOf(searchKeyword)> -1){
              return true;
          }else if (Matakuliah.getKodeMk().toLowerCase().indexOf(searchKeyword)>-1){
              return true;
          }else
              return false;
       });           
       });
       SortedList<Matakuliah> sortedData = new SortedList<>(filterData);
       sortedData.comparatorProperty().bind(tvMatakuliah.comparatorProperty());
       tvMatakuliah.setItems(sortedData);
    }     
    @FXML
    void pilih(ActionEvent event) {
        stage = (Stage) btnPilih.getScene().getWindow();
        Matakuliah m= tvMatakuliah.getSelectionModel().getSelectedItem();
        stage.setUserData(m);
        stage.close();        
    }    
}
