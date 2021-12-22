package me.t3sl4.httpproxy.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.t3sl4.httpproxy.utils.AbstractController;

import java.net.URL;
import java.util.ResourceBundle;

public class AddHostToFilterController extends AbstractController implements Initializable {
    private Stage stage = null;
    Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    private TextField filteredHostTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    public void addToFilter() {
        if(filteredHostTextField.getText() != null) {
            ProxyController.filteredHosts.add(filteredHostTextField.getText());
            filteredHostTextField.setText(null);
        } else {
            alert.setTitle("HATA!");
            alert.setHeaderText("Filtering Error.");
            alert.setContentText("To filter, you must first specify a url.");
            alert.showAndWait();
        }
    }

    public void clearAllFilteredHosts() {
        ProxyController.filteredHosts.clear();
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
