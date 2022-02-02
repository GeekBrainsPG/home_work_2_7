package com.example.lesson_2_7.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {

    private final Map<String, ClientHandler> clients;
    private final AuthService authService;

    public ChatServer() {
        clients = new HashMap();
        authService = new InMemoryAuthService();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(4000)) {
            while (true) {
                System.out.println("Waiting for client connection");
                final Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
                System.out.println("Client get connected");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void subscribe(ClientHandler client) {
        clients.put(client.getNick(), client);
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client.getNick());
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }
}
