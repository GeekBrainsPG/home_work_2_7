package com.example.lesson_2_7;

import com.example.lesson_2_7.client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MessengerController {

    @FXML
    private TextArea messengerTextArea;

    @FXML
    private TextField messengerInputField;

    @FXML
    public Button submitButton;

    private final ChatClient client;

    public MessengerController() {
        client = new ChatClient(this);
    }

    public void onSubmitButton() {
        final String message = messengerInputField.getText();

        if (message != null && !message.isEmpty()) {
            client.sendMessage(message);
            messengerInputField.clear();
            messengerInputField.requestFocus();
        }
    }

    public void onExitClick(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void addMessage(String message) {
        messengerTextArea.appendText(message + "\n");
    }
}