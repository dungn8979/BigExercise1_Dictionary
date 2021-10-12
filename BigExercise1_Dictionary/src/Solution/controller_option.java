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

public class Controller_Option {
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private AccessSQL accessSQL = new AccessSQL("dictionary", "root", "");

    @FXML
    public TextField texFie_target;
    @FXML
    public TextArea texAre_detail;

    /**
     * Lookup in DataBase by SQL
     * @param target
     * @return detail
     * @throws SQLException
     */
    private String Lookup(String target) throws SQLException {
        if (target != null) {
            ResultSet resultSet = accessSQL.getDataBase("SELECT * FROM tbl_edict WHERE `word`='" + target + "'");
            if (resultSet.next()) {
                return resultSet.getString("detail").trim();
            }
        }
        return null;
    }

    @FXML
    public void Event_Save(MouseEvent event) throws IOException, SQLException {
        // Save data into DataBase
        String save_target = texFie_target.getText().trim();
        String save_detail = texAre_detail.getText().trim();
        if (Lookup(texFie_target.getText().trim()) != null) {
            accessSQL.setDataBase("DELETE FROM tbl_edict WHERE `word`='" + save_target + "'");
            accessSQL.setDataBase("INSERT INTO tbl_edict (`id`, `word`, `target`) VALUES ('0','"+save_target+"','"+save_detail+"'");
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Main.fxml"));
        Parent root = loader.load();

        // Transmit data between scenes
        Controller_Main controller = loader.getController();
        controller.texFie_target.setText(texFie_target.getText().trim());
        controller.lab_target.setText(texFie_target.getText().trim());
        controller.lab_detail.setText(texAre_detail.getText().trim());

        // move Display follow Mouse
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        // Load stage
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void Event_Cancel(MouseEvent event) throws IOException {
        // back to Display Main.fxml
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

        // Move Display follow Mouse
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
