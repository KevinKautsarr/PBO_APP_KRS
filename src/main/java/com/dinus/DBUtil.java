package com.dinus;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DBUtil {
   public static void changeScene(ActionEvent event,String fxmlFile,String title,String userName) {
      Parent root = null;
      if (userName!=null) {
         try {
            FXMLLoader  loader = new FXMLLoader(DBUtil.class.getResource(fxmlFile));
            root = loader.load();
            MenuController menu = loader.getController();
            menu.setUserInfo(userName);
         } catch(IOException e)
         {
            e.printStackTrace();
         }
      } else {
         try {
            root = FXMLLoader.load(DBUtil.class.getResource(fxmlFile));
         }
         catch (IOException e){
            e.printStackTrace();
         }
          
      }
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setTitle(title);
      stage.setScene(new Scene(root,1100,800));
      stage.setMaximized(true);
      stage.show();     
   }
   

}
