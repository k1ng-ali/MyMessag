package org.example.Server;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ServerController {

    @FXML
    private Text ClientCount;

    @FXML
    private Text ConnectedClient;

    @FXML
    private Text Logo;

    @FXML
    private Text Logo2;

    @FXML
    private ScrollPane ScrollPane;

    @FXML
    private VBox VBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void initialize() {
        try {
            Server server = new Server(VBox, ClientCount);
            server.start(8080);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
