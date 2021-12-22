package me.t3sl4.httpproxy.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import me.t3sl4.httpproxy.utils.AbstractController;

import java.net.URL;
import java.util.ResourceBundle;

public class FilteredHostsController extends AbstractController implements Initializable {
    private Stage stage = null;

    @FXML
    private TextArea filteredHosts;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(!ProxyController.filteredHosts.isEmpty()) {
            filteredHosts.setText(ProxyController.filteredHosts.toString());
        } else {
            filteredHosts.setText("You have not done any filtering yet.");
        }
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
