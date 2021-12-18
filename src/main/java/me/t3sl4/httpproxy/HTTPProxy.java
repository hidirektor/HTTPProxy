package me.t3sl4.httpproxy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HTTPProxy extends Application {
    private Stage stage;

    private Image proxyIcon = new Image(getClass().getResourceAsStream("/images/icon.png"));

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HTTPProxy.class.getResource("proxy-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 400);
        stage.setResizable(false);
        stage.setTitle("HTTPProxy !");
        stage.getIcons().add(proxyIcon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public Stage getPrimaryStage() {
        return stage;
    }
}