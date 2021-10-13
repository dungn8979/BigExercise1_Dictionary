module BigExercise1_Dictionary_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires freetts;
    requires java.sql;

    opens Solution to javafx.fxml;
    exports Solution;
}