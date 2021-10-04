module BigExercise1_Dictionary_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires freetts;

    opens Solution to javafx.fxml;
    exports Solution;
}