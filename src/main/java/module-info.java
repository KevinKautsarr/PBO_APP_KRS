module com.dinus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.dinus to javafx.fxml;
    exports com.dinus;

//    exports dinus.pbo.appperpus3;
//    exports dinus.pbo.appperpus3.controllers;
//    exports dinus.pbo.appperpus3.models;
//    opens dinus.pbo.appperpus3.controllers to javafx.fxml;

}
