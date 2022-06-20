module com.example.javaf_phase4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.example.javaf_phase4 to javafx.fxml;
    exports com.example.javaf_phase4;
}