module com.example.trabalho3ed2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.trabalho3ed2 to javafx.fxml;
    exports com.example.trabalho3ed2;
}