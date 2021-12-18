package me.t3sl4.httpproxy.utils;

import me.t3sl4.httpproxy.HTTPProxy;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController {

    protected HTTPProxy main;

    public void setMainApp(HTTPProxy main) {
        this.main = main;
    }

    public abstract void initialize(URL url, ResourceBundle rb);
}
