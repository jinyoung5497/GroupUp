module com.example.groupupcab302 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.controlsfx.controls;


    opens com.example.groupupcab302 to javafx.fxml;
    exports com.example.groupupcab302;
    exports com.example.groupupcab302.mockDAO;
}