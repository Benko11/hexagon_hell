module com.example.terrible_fate {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.terrible_fate to javafx.fxml;
    exports com.example.terrible_fate;
}