package com.example.lesson_2_7;

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

    public void onSubmitButton() {
        final String inputValue = messengerInputField.getText();

        if (inputValue != null && !inputValue.isEmpty()) {
            messengerTextArea.appendText(inputValue + "\n");
            messengerInputField.clear();
        }
    }

    public void onExitClick(ActionEvent actionEvent) {
        System.exit(0);
    }
}