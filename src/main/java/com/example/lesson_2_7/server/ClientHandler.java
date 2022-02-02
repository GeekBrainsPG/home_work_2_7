package com.example.lesson_2_7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler {

    private final Socket socket;
    private final ChatServer chatServer;
    private final DataInputStream in;
    private final DataOutputStream out;


    private String nick;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.nick = "";
            this.socket = socket;
            this.chatServer = chatServer;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    authenticate();
                    readMessage();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        private void closeConnection() {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                    chatServer.unsubscribe(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    private void readMessage() {
        while (true) {
            try {
                final String message = in.readUTF();
                if ("/end".equals(message)) {
                    break;
                }
                chatServer.broadcast(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void authenticate() {
        while (true) {
            try {
                final String message = in.readUTF();

                if (message.startsWith("/auth")) {
                    final String[] split = message.split(" ");
                    String login = split[1];
                    String password = split[2];

                    String nick = chatServer.getAuthService().getNickByLoginAndPassword(login, password);

                    if (nick != null) {
                        if (chatServer.isNickBusy(nick)) {
                            sendMessage("User already authorized");
                            continue;
                        }
                        sendMessage("/authok " + nick);
                        this.nick = nick;
                        chatServer.broadcast("User " + nick + " is online");
                        chatServer.subscribe(this);

                        break;
                    } else {
                        sendMessage("Incorrect login and password");
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            if (message.startsWith("/authok")) {
                out.writeUTF(message);
            } else {
                out.writeUTF(LocalDateTime.now() + " / " + nick + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}
