package org.example.Server;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

import static javafx.scene.layout.StackPane.setAlignment;

public class ClientContent {
    private String nickName;
    private ClientHandler clientHandler;
    private VBox vBox;

    public ClientContent(String nickName, ClientHandler clientHandler, VBox vBox) {
        this.nickName = nickName;
        this.clientHandler = clientHandler;
        this.vBox = vBox;

        Text text = new Text(nickName);
        Button button = new Button("Отключить");
        StackPane stackPane = new StackPane(text);
        stackPane.setStyle("-fx-fill: #f0f0f0; -fx-font-family: 'Georgia Pro Semibold'");
        Region region = new Region();
        region.setPrefWidth(Region.USE_COMPUTED_SIZE);
        HBox hBox = new HBox(stackPane,region, button);
        hBox.setAlignment(Pos.CENTER);
        HBox.setMargin(stackPane, new Insets(3));
        hBox.setStyle("-fx-background-color: #17554d; -fx-background-radius: 5px");
        vBox.getChildren().add(hBox);
        button.setOnAction(event -> {
            try {
                clientHandler.stopConnection();
                vBox.getChildren().remove(hBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

}
