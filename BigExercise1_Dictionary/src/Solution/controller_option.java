package Solution;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class controller_option implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private AccessSQL accessSQL = new AccessSQL("dictionary", "root", "");

    @FXML
    public TextField texFie_target;
    @FXML
    public TextArea texAre_detail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void init(Word word) {
        System.out.println("into initialize");
        try{
            texFie_target.setText(word.getWord_target());
        } catch (Exception e) {
            System.out.println("initialize error");
        }
        System.out.println("finish initialize");
    }

    private String Lookup() throws SQLException {
        String str_target = texFie_target.getText().trim();
        if (str_target != null) {
            ResultSet resultSet = accessSQL.getDataBase("select * from tbl_edict where `word`='" + str_target + "'");
            if (resultSet.next()) {
                return resultSet.getString("detail").trim();
            }
        }
        return null;
    }

    @FXML
    public void Event_Save(MouseEvent event) throws IOException, SQLException {
        // lưu dữ liệu vào DataBase
        String save_target = texFie_target.getText().trim();
        String save_detail = texAre_detail.getText().trim();
        if (Lookup() != null) {
            accessSQL.setDataBase("DELETE FROM tbl_edict WHERE `word`='" + save_target + "'");
            accessSQL.setDataBase("INSERT INTO tbl_edict (`id`, `word`, `target`) VALUES ('0','"+save_target+"','"+save_detail+"'");
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        //Parent root = FXMLLoader.load(getClass().getResource("main.fxml")) -> không set controller_main được

        // set texFie_target, lab_target, lab_detail cho controller_main
        controller_main controller = loader.getController();
        controller.texFie_target.setText(texFie_target.getText().trim());
        controller.lab_target.setText(texFie_target.getText().trim());
        controller.lab_detail.setText(texAre_detail.getText().trim());

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
    public void Event_Cancel(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

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
}
