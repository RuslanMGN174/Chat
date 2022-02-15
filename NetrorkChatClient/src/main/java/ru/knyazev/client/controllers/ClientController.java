package ru.knyazev.client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.knyazev.client.ClientChat;
import ru.knyazev.client.dialogs.Dialogs;
import ru.knyazev.client.model.Network;
import ru.knyazev.client.model.ReadCommandListener;
import ru.knyazev.client.service.ChatHistory;
import ru.knyazev.clientserver.Command;
import ru.knyazev.clientserver.CommandType;
import ru.knyazev.clientserver.commands.ClientMessageCommandData;
import ru.knyazev.clientserver.commands.UpdateUserListCommandData;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;

public class ClientController {

    private static final int LAST_HISTORY_ROWS_NUMBER = 100;

    @FXML private TextArea textArea;
    @FXML private TextField textField;
    @FXML private Button sendButton;
    @FXML public ListView<String> userList;

    private ClientChat application;
    private ChatHistory chatHistoryService;

    public void sendMessage() {
        String message = textField.getText().trim();

        if (message.isEmpty()) {
            textField.clear();
            return;
        }

        String sender = null;
        if (!userList.getSelectionModel().isEmpty()) {
            sender = userList.getSelectionModel().getSelectedItem();
        }

        try {
            if (sender != null) {
                Network.getInstance().sendPrivateMessage(sender, message);
            } else {
                Network.getInstance().sendMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
            application.showErrorDialog("Ошибка передачи данных по сети");
        }

        appendMessageToChat("Я", message);
    }

    public void createChatHistory() {
        this.chatHistoryService = new ChatHistory(Network.getInstance().getCurrentUsername());
        chatHistoryService.init();
    }

    private void appendMessageToChat(String sender, String message) {
        String currentText = textArea.getText();

        textArea.appendText(DateFormat.getDateTimeInstance().format(new Date()));
        textArea.appendText(System.lineSeparator());

        if (sender != null) {
            textArea.appendText(sender + ":");
            textArea.appendText(System.lineSeparator());
        }

        textArea.appendText(message);
        textArea.appendText(System.lineSeparator());
        textArea.appendText(System.lineSeparator());
        textField.setFocusTraversable(true);
        textField.clear();

        String newMessage = textArea.getText(currentText.length(), textArea.getLength());
        chatHistoryService.appendText(newMessage);
    }


    public void setApplication(ClientChat application) {
        this.application = application;
    }

    public void initializeMessageHandler() {
        Network.getInstance().addReadMessageListener(new ReadCommandListener() {
            @Override
            public void processReceivedCommand(Command command) {
                if (chatHistoryService == null) {
                    createChatHistory();
                    loadChatHistory();
                }

                if (command.getType() == CommandType.CLIENT_MESSAGE) {
                    ClientMessageCommandData data = (ClientMessageCommandData) command.getData();
                    appendMessageToChat(data.getSender(), data.getMessage());
                } else if (command.getType() == CommandType.UPDATE_USER_LIST) {
                    UpdateUserListCommandData data = (UpdateUserListCommandData) command.getData();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            userList.setItems(FXCollections.observableList(data.getUsers()));
                        }
                    });
                }
            }
        });
    }

    public void closeChat(ActionEvent actionEvent) {
        chatHistoryService.close();
        ClientChat.INSTANCE.getChatStage().close();
    }

    public void changeUserName(ActionEvent actionEvent) {
        TextInputDialog editDialog = new TextInputDialog();
        editDialog.setTitle("Изменить юзернейм");
        editDialog.setHeaderText("Введите новый юзернейм");
        editDialog.setContentText("Username:");

        Optional<String> result = editDialog.showAndWait();
        if (result.isPresent()) {
            try {
                Network.getInstance().changeUsername(result.get());
            } catch (IOException e) {
                e.printStackTrace();
                Dialogs.NetworkError.SEND_MESSAGE.show();
            }

        }
    }

    private void loadChatHistory() {
//        String rows = chatHistoryService.loadLastRows(LAST_HISTORY_ROWS_NUMBER);
        String rows = chatHistoryService.loadLastRows2(LAST_HISTORY_ROWS_NUMBER);
        textArea.clear();
        textArea.setText(rows);
    }

    public void about(ActionEvent actionEvent) {
        Dialogs.AboutDialog.INFO.show();
    }
}