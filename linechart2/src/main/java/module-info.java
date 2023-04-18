module com.example.linechart2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.linechart2 to javafx.fxml;
    exports com.example.linechart2;
}