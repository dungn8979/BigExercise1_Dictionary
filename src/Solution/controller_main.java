package Solution;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Solution.Word;

import java.io.IOException;

public class controller_main {
    private double xOffset = 0;
    private double yOffset = 0;
    Stage stage = new Stage();

    @FXML
    TextField textField_target;
    @FXML
    Label label_target;

    @FXML
    private void event_search(MouseEvent event) {
        if (textField_target.getText().trim() != null) {
            search(textField_target.getText().trim());
        }
    }

    @FXML
    public void event_new(MouseEvent event) throws IOException {
        // load du lieu option.fxml vao
        Parent root = FXMLLoader.load(getClass().getResource("option.fxml"));

        // di chuyen man hinh theo chuot
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void event_edit(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("option.fxml"));

        // di chuyen man hinh theo chuot
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void event_delete(MouseEvent event) {

    }

    @FXML
    public void event_exit(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public Word search(String target) {

        return new Word();
    }
}
