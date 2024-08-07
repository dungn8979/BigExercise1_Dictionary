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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Controller_Main implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    Stage stage;
    DictionaryManagement dictionaryManagement;
    AccessSQL accessSQL;

    @FXML
    public TextField texFie_target;
    @FXML
    Label lab_target;
    @FXML
    Label lab_explain;
    @FXML
    public Label lab_detail;
    @FXML
    TableView<Word> tab_view;
    @FXML
    TableColumn<Word, String> col_target;
    @FXML
    ObservableList<Word> obs_list_word;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictionaryManagement = new DictionaryManagement();
        accessSQL = new AccessSQL("dictionary", "root", "");
        System.out.println("init controller main");
        LoadDataTable();
    }

    public void RefreshDisplay(String target, String explain, String detail) {
        if (target != null) {
            lab_target.setText(target);
            lab_explain.setText(explain);
            lab_detail.setText(detail);
        } else {
            lab_target.setText(null);
            lab_explain.setText(null);
            lab_detail.setText(null);
        }
    }

    /**
     * Load data to display Dictionary List
     */
    void LoadDataTable() {
        // init
        // init column
        obs_list_word = FXCollections.observableArrayList();
        col_target.setCellValueFactory(new PropertyValueFactory<>("word_target"));

        // load data to ObservableList
        ResultSet resultSet = accessSQL.getDataBase("SELECT * FROM `tbl_edict`");
        while (true) {
            try {
                if (!resultSet.next()) break;
                obs_list_word.add(new Word(resultSet.getString("word").trim(), resultSet.getString("detail").trim()));
            } catch (SQLException e) {
                e.printStackTrace();
                break;
            }
        }

        if (obs_list_word != null) Collections.sort(obs_list_word,Word::compareTo);
        // load Observable to tableView
        tab_view.setItems(obs_list_word);
    }

    /**
     * Lookup in DataBase by SQL
     * @param target
     * @return detail
     * @throws SQLException
     */
    private String Lookup(String target) throws SQLException {
        if (target != null) {
            ResultSet resultSet = accessSQL.getDataBase("SELECT * FROM `tbl_edict` WHERE `word`='" + target + "'");
            if (resultSet.next()) {
                return resultSet.getString("detail").trim();
            }
        }
        return null;
    }

    public void Search() {
        String target = texFie_target.getText().trim();
        if (!target.isEmpty()) {
            // Search, update tableView
            List<Word> list_match = new ArrayList<>();
            list_match.add(new Word("into", "vao"));
            ObservableList<Word> obs_match_word = FXCollections.observableArrayList(
                    dictionaryManagement.dictionarySearch(target, (List<Word>) obs_list_word));

            // load Observable to tableView
            if (obs_match_word != null) {
                System.out.println("obs null");
                Collections.sort(obs_match_word,Word::compareTo);
                tab_view.setItems(obs_match_word);
            }
        } else tab_view.setItems(obs_list_word);
    }

    @FXML
    private void EventKeyPress_TextField(KeyEvent event) throws SQLException, IOException {
        // Lookup
        if (event.getCode().equals(KeyCode.ENTER)) {
            RefreshDisplay(texFie_target.getText().trim(),
                    GGTranslateAPI.translate(texFie_target.getText().trim()),
                    Lookup(texFie_target.getText().trim()));
        } else {
            Search();
        }
    }

    @FXML
    private void Event_Search(MouseEvent event) throws SQLException, IOException {
        RefreshDisplay(texFie_target.getText().trim(),
                GGTranslateAPI.translate(texFie_target.getText().trim()),
                Lookup(texFie_target.getText().trim()));
    }

    @FXML
    public void Event_New(MouseEvent event) throws IOException, SQLException {
        // Open Display Option.fxml
        Parent root = FXMLLoader.load(getClass().getResource("Option.fxml"));

        // Move Display follow Mouse
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        // Load new Display
        accessSQL.Close();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void Event_Edit(MouseEvent event) throws IOException, SQLException {
        // Open Display Option.fxml
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Option.fxml"));
        Parent root = loader.load();

        // Transmit data between scenes
        Controller_Option controller = loader.getController();
        controller.texFie_target.setText(texFie_target.getText().trim());
        controller.texAre_detail.setText(lab_detail.getText().trim());

        // Move Display follow Mouse
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        // Load new Display
        accessSQL.Close();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Event_Delete(MouseEvent event) throws SQLException, IOException {
        // Create window Alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notify");
        alert.setHeaderText(null);
        alert.setContentText("Do you want delete \"" + texFie_target.getText().trim() + "\"");

        ButtonType btnYes = new ButtonType("Yes",ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("No",ButtonBar.ButtonData.NO);

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(btnYes, btnNo);

        Optional<ButtonType> result = alert.showAndWait();

        // Confirm optional
        if (result.get() == btnYes) {
            accessSQL.setDataBase("DELETE FROM `tbl_edict` WHERE `word`='" + texFie_target.getText().trim() + "'");

            LoadDataTable();

            RefreshDisplay(null, null, null);
        }
    }

    @FXML
    public void Event_Exit(MouseEvent event) throws SQLException {
        accessSQL.Close();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void Event_SelectItemProperty_TableView(MouseEvent event) throws IOException {
        Word wordSelect = tab_view.getSelectionModel().getSelectedItem();
        if (wordSelect != null) {
            texFie_target.setText(wordSelect.getWord_target());
            lab_target.setText(wordSelect.getWord_target());
            lab_explain.setText(GGTranslateAPI.translate(wordSelect.getWord_target()));
            lab_detail.setText(wordSelect.getWord_explain());
        }
    }

    @FXML
    public void Event_SpeakUS(MouseEvent event) {
        if (!lab_target.getText().trim().isEmpty()) {
            dictionaryManagement.speakEnglish(lab_target.getText().trim());
        }
    }
}
