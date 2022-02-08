package com.example.lesson_2_7.client;

import com.example.lesson_2_7.MessengerController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private MessengerController controller;

    public ChatClient(MessengerController controller) {
        this.controller = controller;

        try {
            socket = new Socket("127.0.0.1", 4000);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        String authMessage = in.readUTF();

                        if (authMessage.startsWith("/authok")) {
                            String nick = authMessage.split(" ")[1];

                            controller.addMessage("User " + nick + " successfully authorized");
                            break;
                        }
                    }

                    while (true) {
                        final String message = in.readUTF();

                        if ("/end".equals(message)) {
                            break;
                        }

                        controller.addMessage(message);
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException exception) {
            exception.printStackTrace();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
