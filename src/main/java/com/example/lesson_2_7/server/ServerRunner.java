package com.example.lesson_2_7.server;

public class ServerRunner {

    public static void main(String[] args) {
        final ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}
