package org.example.Server;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ServerSocket serverSocket;
    private static Map<String, ClientHandler> clientHandlers = new ConcurrentHashMap<>();
    private VBox vBox;
    private Text ClientCount;

    public Server(VBox vBox, Text text) {
        this.vBox = vBox;
        this.ClientCount = text;
    }

    public void start(int PORT) {
        try  {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chat server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this, vBox, ClientCount);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message, ClientHandler excludeUser) {
        for (ClientHandler clientHandler : clientHandlers.values()) {
            if (clientHandler != excludeUser) {
                clientHandler.sendMessage(message);
            }
        }
    }

    public synchronized boolean addClient(String nickName, ClientHandler clientHandler) {
        if (clientHandlers.containsKey(nickName)) {
            return false;
        }else {
            clientHandlers.put(nickName, clientHandler);
            return true;
        }
    }

    public synchronized String getSizedClientHandlers() {
        return String.valueOf(clientHandlers.size());
    }

    public synchronized ClientHandler getClientHandler(String nickName) {
        return clientHandlers.get(nickName);
    }

    public synchronized void removeClient(String nickName) {
        clientHandlers.remove(nickName);
    }

    public synchronized Map<String, ClientHandler> getClientHandlers() {
        return clientHandlers;
    }
}

