package org.example.Client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.shared.*;

import java.io.IOException;

import static org.example.shared.JsonUtill.toJson;

public class MessageHandler {
    private Client client;
    private User sender;
    private User receiver;
    private Message message;
    private Data data;

    public MessageHandler(Client client) {
        this.client = client;
    }

    public synchronized void registrHandle(TextField textField, Text text,
                                           AnchorPane RegistrPane, AnchorPane SearchPane){
        if (!textField.getText().equals("")){
            sender = new User(textField.getText());
            message = new Message(null, sender, null);
            data = new Data("REGISTR", message);
            String jsonData = toJson(data);
            try{
                client.sendData(jsonData);
                System.out.println("Отправлено");
                text.setText("Ожидание ответ от сервера...");
                text.setStyle("-fx-fill: green");
                text.setVisible(true);
                String response = client.receiveData();
                System.out.println(response);
                if (response.equals("OK")){
                    RegistrPane.setVisible(false);
                    SearchPane.setVisible(true);
                } else {
                    text.setText("Пользователь с таким именем уже существует");
                    text.setStyle("-fx-fill: red");
                    text.setVisible(true);
                }
            } catch (Exception e){
                text.setText("Ошибка подключения к серверу");
                text.setStyle("-fx-fill: red");
                text.setVisible(true);
            }
        }
        else {
            text.setText("Введите ваш Никнейм");
            text.setStyle("-fx-fill: red");
            text.setVisible(true);
        }
    }

    public synchronized boolean SearchHandle(TextField textField, Text text,
                                          AnchorPane SearchPane, AnchorPane ChatPane,
                                              Text connectionError){
        if (!textField.getText().equals("")){
            receiver = new User(textField.getText());
            message = new Message(null, sender, receiver);
            data = new Data("SEARCH", message);
            String jsonData = toJson(data);
            try{
                client.sendData(jsonData);
                text.setText("Ожидание ответ от сервера...");
                text.setStyle("-fx-fill: green");
                text.setVisible(true);
                String response = client.receiveData();
                if (response.equals("OK")){
                    SearchPane.setVisible(false);
                    ChatPane.setVisible(true);
                    ChatPane.setDisable(false);
                    checkConnect(ChatPane, connectionError);
                    return true;
                } else {
                    text.setText("Пользователь не найден");
                    text.setStyle("-fx-fill: red");
                    text.setVisible(true);
                }
            } catch (Exception e){
                text.setText("Ошибка подключения к серверу");
                text.setStyle("-fx-fill: red");
                text.setVisible(true);
            }
        }
        else {
            text.setText("Введите Никнейм ползователя");
            text.setStyle("-fx-fill: red");
            text.setVisible(true);
        }
        return false;
    }

    public synchronized void chatHandle(VBox ChatVBox, TextField ChatTextField) {
        if (!ChatTextField.getText().equals("")) {
            message = new Message(ChatTextField.getText(), sender, receiver);
            data = new Data("CHAT", message);
            String jsonData = toJson(data);
            new Thread(() -> {
            try {
                client.sendData(jsonData);
                ChatTextField.clear();
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Text text = new Text("Ошибка подключения к серверу");
                    ChatVBox.getChildren().add(text);
                });
            }
        }).start();
        }
    }


    public synchronized void responseHandle(VBox ChatVBox, ScrollPane scrollPane, AnchorPane ChatPane,
                                            Text connectionError, boolean isRunnable){
        new Thread(() ->{
        while (isRunnable){
            String response = null;
            try {
                response = client.receiveData();
            } catch (Exception e) {
                Platform.runLater(()->{
                    connectionError.setText("Ошибка подключения к серверу");
                    connectionError.setStyle("-fx-fill: #f1a0a0");
                    connectionError.setVisible(true);
                    ChatPane.setDisable(true);
                    System.out.println("Ошибка подключения к серверу");
                });
                break;
            }
            if(response != null){
                String finalResponse = response;
                System.out.println(finalResponse);
                Platform.runLater(() -> {
                    if (!finalResponse.equals("OK") && !finalResponse.equals("ERROR")) {
                        Data responseData = JsonUtill.fromJson(finalResponse);
                        if (responseData.getRequest().equals("CHAT")) {
                            Message responseMessage = responseData.getMessage();
                            Text text = new Text(responseMessage.getSender().getNickName() + ": " + responseMessage.getMessage());
                            text.setStyle("-fx-fill: #f0f0f0;");
                            StackPane stackPane = new StackPane(text);
                            stackPane.setStyle("-fx-fill: #f0f0f0; -fx-font-family: 'Georgia Pro Semibold'; -fx-background-color: #2c2c37; -fx-background-radius: 5px");
                            stackPane.setPadding(new Insets(10));
                            HBox hBox = new HBox(stackPane);
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            HBox.setMargin(stackPane, new Insets(3));
                            ChatVBox.getChildren().add(hBox);
                            scrollPane.layout(); // Убедимся, что макет обновлён
                            scrollPane.setVvalue(1.0);
                        }
                        else if(responseData.getRequest().equals("OK")) {
                            Message responseMessage = responseData.getMessage();
                            Text text = new Text("Вы: " + responseMessage.getMessage());
                            text.setStyle("-fx-fill: #f0f0f0;");
                            StackPane stackPane = new StackPane(text);
                            stackPane.setStyle("-fx-fill: #f0f0f0; -fx-font-family: 'Georgia Pro Semibold'; -fx-background-color: #17554d; -fx-background-radius: 5px");
                            stackPane.setPadding(new Insets(10));
                            HBox hBox = new HBox(stackPane);
                            hBox.setAlignment(Pos.CENTER_RIGHT);
                            HBox.setMargin(stackPane, new Insets(3));
                            ChatVBox.getChildren().add(hBox);
                            scrollPane.layout(); // Убедимся, что макет обновлён
                            scrollPane.setVvalue(1.0);
                        } else if (responseData.getRequest().equals("NOTUSER")) {
                            connectionError.setText("Пользователь отключился от сервера!");
                            connectionError.setVisible(true);
                            ChatPane.setDisable(true);
                        } else if (responseData.getRequest().equals("USERISONLINE")) {
                            connectionError.setText(receiver.getNickName() + " в сети");
                            connectionError.setStyle("-fx-fill: #76ffc5");
                            connectionError.setVisible(true);
                            System.out.println(receiver.getNickName() + " в сети");
                        } else if (responseData.getRequest().equals("USERISOFFLINE")) {
                            connectionError.setText(receiver.getNickName() + " не в сети");
                            connectionError.setStyle("-fx-fill: #f1a0a0");
                            connectionError.setVisible(true);
                            System.out.println(receiver.getNickName() + " не в сети");
                        } else {
                            System.out.println("Ошибка");
                        }
                    }
                });
            }
        }
        }).start();
    }

    public synchronized void checkConnect(AnchorPane chatPane, Text connectionError){
        new Thread(()->{
            while (true){
                checkUser(chatPane, connectionError);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public synchronized void checkUser(AnchorPane chatPane, Text connectionError){
        message = new Message(null, sender, receiver);
        data = new Data("CHECK", message);
        String jsonData = toJson(data);
        new Thread(()->{
            try {
                client.sendData(jsonData);
                System.out.println("Отправлено:" + jsonData);
            } catch (Exception e) {
                Platform.runLater(()->{
                    connectionError.setText("Ошибка подключения к серверу");
                    connectionError.setVisible(true);
                    chatPane.setDisable(true);
                    System.out.println("Ошибка подключения к серверу");
                });
            }
        }).start();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
