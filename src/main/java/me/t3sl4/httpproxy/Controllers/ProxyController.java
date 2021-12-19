package me.t3sl4.httpproxy.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.t3sl4.httpproxy.HTTPProxy;
import me.t3sl4.httpproxy.Server.ServerHandler;
import me.t3sl4.httpproxy.utils.AbstractController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ProxyController extends AbstractController {

    public ServerSocket welcomeSocket;

    private Image popupIcon = new Image(getClass().getResourceAsStream("/images/icon.png"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    public void startServerAction() throws IOException {
        welcomeSocket = new ServerSocket(8080);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new ServerHandler(connectionSocket);
        }
    }

    public void stopServerAction() throws IOException {
        welcomeSocket.close();
    }

    public void exitProgram() {
        Platform.exit();
    }

    public void developedByAction() {
        showPopupWindow();
    }

    private void showPopupWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HTTPProxy.class.getResource("popup.fxml"));

        PopupController popupController = new PopupController();
        loader.setController(popupController);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            popupController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.getIcons().add(popupIcon);
            popupStage.setResizable(false);
            popupStage.setTitle("Developed by");
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "FXML dosyası yüklenemedi.").show();
        }
    }
}