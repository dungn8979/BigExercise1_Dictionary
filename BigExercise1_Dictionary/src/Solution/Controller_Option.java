package Solution;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller_Option {
    private String optionMode = "NEW"; // NEW or EDIT; default is NEW
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
     * @return detail or null
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

    @FXML
    public void Event_Save(MouseEvent event) throws IOException, SQLException {
        // Create window Alert Information
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notify");
        alert.setHeaderText(null);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Controller_Main controller = loader.getController();

        // Save data into DataBase
        String save_target = texFie_target.getText().trim();
        String save_detail = texAre_detail.getText().trim();
        String wordHad_detail = Lookup(texFie_target.getText().trim());
        // Check word had in database
        if (wordHad_detail == null) {
            try {
                System.out.println(save_target);
                System.out.println(save_detail);
                accessSQL.setDataBase("INSERT INTO `tbl_edict`(`idx`, `word`, `detail`) VALUES ('0','" + save_target + "','" + save_target + "');");
                // Transmit data between scenes
                controller.texFie_target.setText(save_target);
                controller.lab_target.setText(save_target);
                controller.lab_detail.setText(save_detail);
            } catch (Exception exp) {
                alert.setContentText("Error");
            }

        } else {
            // Create window Alert Confirmation
            Alert alert_confirm = new Alert(Alert.AlertType.CONFIRMATION);
            alert_confirm.setTitle("Notify");
            alert_confirm.setHeaderText(null);
            if (optionMode != "NEW")
                alert_confirm.setContentText("This word had. Do you want fix it?");
            else
                alert_confirm.setContentText("Do you want fix it?");

            ButtonType btnYes = new ButtonType("Yes",ButtonBar.ButtonData.YES);
            ButtonType btnNo = new ButtonType("No",ButtonBar.ButtonData.NO);

            alert_confirm.getButtonTypes().clear();
            alert_confirm.getButtonTypes().addAll(btnYes, btnNo);

            Optional<ButtonType> result = alert_confirm.showAndWait();
            // Confirm optional
            if (result.get() == btnYes) {
                try {
                    accessSQL.setDataBase("DELETE FROM `tbl_edict` WHERE `word`='" + save_target + "';");
                    accessSQL.setDataBase("INSERT INTO `tbl_edict` (`idx`, `word`, `detail`) VALUES ('0','"+save_target+"','"+save_detail+"');");

                    // Transmit data between scenes
                    controller.texFie_target.setText(save_target);
                    controller.lab_target.setText(save_target);
                    controller.lab_detail.setText(save_detail);
                } catch (Exception exp) {
                    alert.setContentText("Error");
                }
            } else {
                // Transmit data between scenes
                controller.texFie_target.setText(save_target);
                controller.lab_target.setText(save_target);
                controller.lab_detail.setText(wordHad_detail);
            }
        }

        // move Display follow Mouse
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        alert.setContentText("success");
        alert.show();

        // Load new Display
        accessSQL.Close();
        controller.Sort_TableView();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void Event_Cancel(MouseEvent event) throws IOException, SQLException {
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

        // Load new Display
        accessSQL.Close();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
