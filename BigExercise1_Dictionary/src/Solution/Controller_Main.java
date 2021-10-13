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
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
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

    public void Sort_TableView() {
        String target = texFie_target.getText().trim();
        if (!target.isEmpty()) {
            System.out.println(target);
            ObservableList<Word> obs_match_word = FXCollections.observableArrayList();
            obs_match_word.addAll(obs_list_word.stream()
                    .filter(str -> str.getWord_target().contains(target))
                    .collect(Collectors.toList()));

            // load Observable to tableView
            if (obs_match_word != null) {
                Collections.sort(obs_match_word,Word::compareTo);
                tab_view.setItems(obs_match_word);
            }
        } else tab_view.setItems(obs_list_word);
    }

    @FXML
    private void EventKeyPress_TextField(KeyEvent event) throws SQLException {
        // Lookup
        if (event.getCode().equals(KeyCode.ENTER)) {
            String target = texFie_target.getText().trim();
            String detail = Lookup(target);
            if (target != null) {
                lab_target.setText(target);
            } else {
                lab_target.setText(null);
            }
            lab_detail.setText(detail);
        } else {
            Sort_TableView();
        }
    }

    @FXML
    private void Event_Search(MouseEvent event) throws SQLException {
        String target = texFie_target.getText().trim();
        String detail = Lookup(target);
        if (target != null) {
            lab_target.setText(target);
        } else {
            lab_target.setText(null);
        }
        lab_detail.setText(detail);
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
    public void Event_Delete(MouseEvent event) throws SQLException {
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

            lab_target.setText(null);
            lab_detail.setText(null);
        }
    }

    @FXML
    public void Event_Exit(MouseEvent event) throws SQLException {
        accessSQL.Close();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void Event_SelectItemProperty_TableView(MouseEvent event) {
        Word wordSelect = tab_view.getSelectionModel().getSelectedItem();
        texFie_target.setText(wordSelect.getWord_target());
        lab_target.setText(wordSelect.getWord_target());
        lab_detail.setText(wordSelect.getWord_explain());
    }

    @FXML
    public void Event_SpeakUS(MouseEvent event) {
        if (!lab_target.getText().trim().isEmpty()) {
            dictionaryManagement.speakEnglish(lab_target.getText().trim());
        }
    }
}
