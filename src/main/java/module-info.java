module com.example.lesson_2_7 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lesson_2_7 to javafx.fxml;
    exports com.example.lesson_2_7;
}