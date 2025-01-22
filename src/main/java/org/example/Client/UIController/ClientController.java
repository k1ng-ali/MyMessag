package org.example.Client.UIController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.Client.Client;
import org.example.Client.MessageHandler;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientController {
    private Client client;
    private MessageHandler messageHandler;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button ChatButton;

    @FXML
    private AnchorPane ChatPane;

    @FXML
    private ScrollPane ChatScrollPane;

    @FXML
    private TextField ChatTextField;

    @FXML
    private VBox ChatVBox;

    @FXML
    private Text Logo;

    @FXML
    private VBox RegistrBox;

    @FXML
    private Button RegistrButton;

    @FXML
    private AnchorPane RegistrPane;

    @FXML
    private Text RegistrText;

    @FXML
    private Text ConnectionError;

    @FXML
    private Text RegistrErrorText;

    @FXML
    private Text SearchErrorText;

    @FXML
    private TextField RegistrTextField;

    @FXML
    private VBox SearchBox;

    @FXML
    private Button SearchButton;

    @FXML
    private AnchorPane SearchPane;

    @FXML
    private Text SearchText;

    @FXML
    private TextField SearchTextField;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void initialize(){
        SearchPane.setVisible(false);
        ChatPane.setVisible(false);
        RegistrPane.setVisible(true);
        client = new Client();
        AtomicBoolean isRunnable = new AtomicBoolean(true);
        try {
            client.connect("localhost");
            messageHandler = new MessageHandler(client);
        } catch (Exception e){
            System.out.println("Error connecting to server");
        }
        RegistrButton.setOnMouseClicked(event -> {
            messageHandler.registrHandle(RegistrTextField, RegistrErrorText, RegistrPane, SearchPane);
        });
        SearchButton.setOnMouseClicked(event -> {
            if (messageHandler.SearchHandle(SearchTextField, SearchErrorText, SearchPane, ChatPane, ConnectionError)){
                isRunnable.set(true);
                 new Thread (()->messageHandler.responseHandle(ChatVBox, ChatScrollPane, ChatPane, ConnectionError, isRunnable.get())).start();
            };
        });
        ChatButton.setOnMouseClicked(event -> {
           messageHandler.chatHandle(ChatVBox, ChatTextField);
        });
        Logo.setOnMouseClicked(event -> {
            SearchPane.setVisible(false);
            ChatPane.setVisible(false);
            isRunnable.set(false);
            RegistrPane.setVisible(true);
            try {
                client.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            client = new Client();
            try {
                client.connect("localhost");
                messageHandler = null;
                messageHandler = new MessageHandler(client);
            } catch (Exception e){
                System.out.println("Error connecting to server");
            }
        });
    }

}