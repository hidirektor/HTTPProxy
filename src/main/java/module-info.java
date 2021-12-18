module me.t3sl4.httpproxy {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.t3sl4.httpproxy to javafx.fxml;
    exports me.t3sl4.httpproxy;
    exports me.t3sl4.httpproxy.Controllers;
    opens me.t3sl4.httpproxy.Controllers to javafx.fxml;
}