package me.t3sl4.httpproxy.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import me.t3sl4.httpproxy.utils.AbstractController;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupController extends AbstractController implements Initializable {
    private Stage stage = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}
