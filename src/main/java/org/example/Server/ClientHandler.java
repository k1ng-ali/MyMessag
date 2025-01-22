package org.example.Server;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.shared.Data;
import org.example.shared.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.example.shared.JsonUtill.fromJson;
import static org.example.shared.JsonUtill.toJson;

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private User user;
    private Server server;
    private VBox vBox;
    private Text ClientCount;

    public ClientHandler(Socket socket, Server server, VBox vBox,
                         Text text) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.ClientCount = text;
    }

    @Override
    public synchronized void run() {
        new Thread(()-> {
            try {
                String message;
                Data data;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received message: " + message);
                    data = fromJson(message);
                    if (data.getRequest().equals("REGISTR")) {
                        user = data.getMessage().getSender();
                        System.out.println("User " + user.getNickName() + " connected");
                        if (server.addClient(user.getNickName(), this)) {
                            System.out.println("User " + user.getNickName() + " added");
                            out.println("OK");
                            ClientContent clientContent = new ClientContent(user.getNickName(), this, vBox);
                            ClientCount.setText(server.getSizedClientHandlers());
                        } else {
                            out.println("NO");
                        }
                    } else if (data.getRequest().equals("SEARCH")) {
                        User resiver = data.getMessage().getReceiver();
                        if (server.getClientHandler(resiver.getNickName()) != null &&
                                !resiver.getNickName().equals(user.getNickName())) {
                            out.println("OK");
                        } else {
                            out.println("NO");
                        }
                    } else if (data.getRequest().equals("CHAT")) {
                        User receiver = data.getMessage().getReceiver();
                        if (server.getClientHandler(receiver.getNickName()) != null) {
                            server.getClientHandler(receiver.getNickName()).sendMessage(message);
                            data.setRequest("OK");
                            sendMessage(toJson(data));
                        } else {
                            out.println("NO");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public synchronized void sendMessage(String message) {
        new Thread(()-> {
            out.println(message);
            System.out.println("Send message: " + message);
        }).start();
    }

    /*public Data getData() {
        try {
            return fromJson(in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    public void stopConnection() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }
}
