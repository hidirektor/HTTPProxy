package me.t3sl4.httpproxy.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.t3sl4.httpproxy.Server.ServerHandler;
import me.t3sl4.httpproxy.utils.AbstractController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportController extends AbstractController {
    private Stage stage = null;

    @FXML
    private TextField ipTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    //
    public void showReportButton() throws IOException {
        if(ipTextField != null) {
            try {
                ProcessBuilder pb = new ProcessBuilder("Notepad.exe", System.getProperty("user.home") + "\\Desktop\\" + ipTextField.getText() + ".txt");
                pb.start();
            }catch(Exception ex) {
                ex.printStackTrace();
            }
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
