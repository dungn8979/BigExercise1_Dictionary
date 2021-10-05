package Solution;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Solution.Word;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class controller_main implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    Stage stage;
    DictionaryManagement dic_management;

    @FXML
    TextField textField_target;
    @FXML
    Label label_target;
    @FXML
    TableView<Word> tableView;
    @FXML
    TableColumn<Word, String> col_target;
    @FXML
    ObservableList<Word> obs_list_word;

    @FXML
    private void event_search(MouseEvent event) {
        if (textField_target.getText().trim() != null) {
            lookup(textField_target.getText().trim());
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

    public Word lookup(String target) {

        return new Word();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dic_management.insertFromFile();
        } catch (Exception exp) {
            System.out.println("dic_management.insertFromFile() ERROR");
        }

        init_load_table();
    }

    void init_load_table() {
        // init
        // init column
        col_target.setCellValueFactory(new PropertyValueFactory<>("word_target"));

        // load data to ObservableList
        obs_list_word = FXCollections.observableArrayList();
        obs_list_word.add(new Word("English", "Tiếng Việt"));

        // load Observable to tableView
        tableView.setItems(obs_list_word);
    }
}
