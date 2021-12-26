package me.t3sl4.httpproxy.Controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.t3sl4.httpproxy.HTTPProxy;
import me.t3sl4.httpproxy.Server.ServerHandler;
import me.t3sl4.httpproxy.utils.AbstractController;
import me.t3sl4.httpproxy.utils.ClipboardUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProxyController extends AbstractController implements Initializable {
    private Image popupIcon = new Image(getClass().getResourceAsStream("/images/icon.png"));
    private Image serverStartIcon = new Image(getClass().getResourceAsStream("/images/on.png"));
    private Image serverStopIcon = new Image(getClass().getResourceAsStream("/images/off.png"));
    private ServerSocket welcomeSocket;
    public static List filteredHosts = new ArrayList<>();

    @FXML
    private ImageView serverStatusImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverStatusImage.setImage(serverStopIcon);
    }

    public void startServerAction() {
        Task<Void> startServerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                welcomeSocket = new ServerSocket(8080);
                while (true) {
                    Socket connectionSocket = welcomeSocket.accept();
                    new ServerHandler(connectionSocket);
                }
            }
        };

        Thread startServerThread = new Thread(startServerTask);
        startServerThread.start();
        serverStatusImage.setImage(serverStartIcon);
    }

    public void stopServerAction() {
        Task<Void> stopServerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                welcomeSocket.close();
                return null;
            }
        };

        Thread stopServerThread = new Thread(stopServerTask);
        stopServerThread.start();
        serverStatusImage.setImage(serverStopIcon);
    }

    public void showReportPopup() {
        showReportWindow();
    }

    public void testGETMethod() {
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"curl -v -H \"Connection: close\" -x http://localhost:8080 http://www.yandex.com.tr\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testPOSTMethod() {
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"curl -v -H \"Connection: close\" -x http://localhost:8080 http://www.yandex.com.tr\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testHEADMethod() {
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"curl -v -H \"Connection: close\" -x http://localhost:8080 http://www.yandex.com.tr\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testCONNECTMethod() {
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"curl -v -H \"Connection: close\" -x http://localhost:8080 http://www.yandex.com.tr\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addHostToFilter() {
        showAddFilterWindow();
    }

    public void displayFilteredHosts() {
        showFilteredHostsWindow();
    }

    public void copyTestCode() {
        String testCmd = "curl -v -H \"Connection: close\" -x http://localhost:8080 http://www.yandex.com.tr";
        ClipboardUtils.copyToClipboardText(testCmd);
    }

    public void exitProgram() {
        Platform.exit();
    }

    public void developedByAction() {
        showDevelopedByWindow();
    }

    private void showDevelopedByWindow() {
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

    private void showAddFilterWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HTTPProxy.class.getResource("AddHostToFilter.fxml"));

        AddHostToFilterController addHostToFilterController = new AddHostToFilterController();
        loader.setController(addHostToFilterController);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            addHostToFilterController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.getIcons().add(popupIcon);
            popupStage.setResizable(false);
            popupStage.setTitle("Filter Host");
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "FXML dosyası yüklenemedi.").show();
        }
    }

    private void showFilteredHostsWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HTTPProxy.class.getResource("FilteredHosts.fxml"));

        FilteredHostsController filteredHostsController = new FilteredHostsController();
        loader.setController(filteredHostsController);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            filteredHostsController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.getIcons().add(popupIcon);
            popupStage.setResizable(false);
            popupStage.setTitle("Filtered Hosts");
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "FXML dosyası yüklenemedi.").show();
        }
    }

    private void showReportWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HTTPProxy.class.getResource("Report.fxml"));

        ReportController reportController = new ReportController();
        loader.setController(reportController);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            reportController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.getIcons().add(popupIcon);
            popupStage.setResizable(false);
            popupStage.setTitle("Report");
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "FXML dosyası yüklenemedi.").show();
        }
    }
}