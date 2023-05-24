package de.shiru.decryptor.desktop.controllers;

import de.shiru.decryptor.core.Encrypting;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EncryptionChooseController {
    public Stage stage;

    public TextArea encryptionDescriptionTextArea;
    public Label encryptionNameLabel;
    public ListView<String> encryptionList;
    public SplitPane dialogSplitPane;
    public SplitPane encryptionSplitPane;
    private Encrypting.Type selectedType;

    public void initialize() {
        encryptionList.getItems().setAll(Encrypting.Type.asStringList());
        encryptionList.getSelectionModel().selectedItemProperty().addListener(this::onEncryptionListSelectionChanged);
        encryptionList.getSelectionModel().select(0);
        dialogSplitPane.getDividers().get(0).positionProperty().addListener((x) -> dialogSplitPane.getDividers().get(0).setPosition(0.9));
        encryptionSplitPane.getDividers().get(0).positionProperty().addListener((x) -> encryptionSplitPane.getDividers().get(0).setPosition(0.3));
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) encryptionList.getScene().getWindow();
        }).start();
    }

    public void onEncryptionListSelectionChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(encryptionNameLabel.getText().equals(newValue)) return;
        encryptionNameLabel.setText(newValue);
        encryptionDescriptionTextArea.clear();
        var stream = this.getClass().getResourceAsStream("/text/" + newValue.toLowerCase() + ".desc");
        if(stream == null) return;
        var reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().forEach((line) -> encryptionDescriptionTextArea.appendText(line + "\n"));
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void applyButtonClicked(ActionEvent actionEvent) {
        selectedType = Encrypting.Type.getByName(encryptionList.getSelectionModel().getSelectedItem());
        stage.close();
    }

    public void abortButtonClicked(ActionEvent actionEvent) {
        stage.close();
    }

    public Encrypting.Type getSelectedType() {
        return selectedType;
    }

}
