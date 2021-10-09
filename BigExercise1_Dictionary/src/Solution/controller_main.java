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
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class controller_main implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    Stage stage;
    DictionaryManagement dic_management = new DictionaryManagement();
    AccessSQL accessSQL = new AccessSQL("dictionary", "root", "");

    @FXML
    TextField texFie_target;
    @FXML
    Label lab_target;
    @FXML
    Label lab_detail;
    @FXML
    TableView<Word> tab_view;
    @FXML
    TableColumn<Word, String> col_target;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResultSet resultSet = accessSQL.getDataBase("select * from tbl_edict");
        try {
            dic_management.insertFromFile("dictionary.txt");
        } catch (Exception exp) {
            System.out.println("dic_management.insertFromFile() ERROR");
            exp.printStackTrace();
        }

        init_load_table();
    }

    @FXML
    private void event_search(MouseEvent event) {
        if (texFie_target.getText().trim() != null) {
            lookup(texFie_target.getText().trim());
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

    void init_load_table() {
        // init
        // init column
        col_target.setCellValueFactory(new PropertyValueFactory<>("word_target"));

        // load data to ObservableList
        ObservableList<Word> obs_list_word = FXCollections.observableArrayList();
        obs_list_word.add(new Word("English", "Tiếng Việt"));

        // load Observable to tableView
        tab_view.setItems(obs_list_word);
    }
}
