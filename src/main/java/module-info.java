module com.example.demo244 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.demo244 to javafx.fxml;
    exports com.example.demo244;
}