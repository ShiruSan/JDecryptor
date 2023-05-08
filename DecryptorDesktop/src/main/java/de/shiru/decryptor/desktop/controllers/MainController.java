package de.shiru.decryptor.desktop.controllers;

import de.shiru.decryptor.core.Encrypting;
import de.shiru.decryptor.desktop.DecryptorApplication;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Objects;

public class MainController {
    public Stage stage;
    public TextArea inputTextArea;
    public TextArea outputTextArea;

    public void init() {
        stage.setOnCloseRequest((x)-> {
            onQuitClicked();
            x.consume();
        });
    }

    public void onQuitClicked() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.getDialogPane().setHeaderText(null);
        Label label = new Label("Are you sure you want to quit ?");
        label.setAlignment(Pos.CENTER);
        label.setLayoutY(20);
        label.setFont(Font.font(14));
        alert.getDialogPane().contentProperty().set(label);
        alert.initOwner(stage);
        var result = alert.showAndWait();
        if(result.get() == ButtonType.OK) stage.close();
    }

    public void onEncryptClicked() throws IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeyException {
        if(inputTextArea.getText().equals(""))  {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The input can't be empty!");
            alert.setTitle("Error");
            alert.initOwner(stage);
            alert.show();
            return;
        }
        var loader = new FXMLLoader(this.getClass().getResource("/fxml/EncryptionChooseDialog.fxml"));
        VBox box = loader.load();
        EncryptionChooseController controller = loader.getController();
        var stage = new Stage(StageStyle.UNIFIED);
        stage.initOwner(this.stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(DecryptorApplication.ICON);
        controller.stage = stage;
        stage.setScene(new Scene(box, box.getPrefWidth(), box.getPrefHeight()));
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Encryption type");
        controller.label.setText("Which encryption do you want to use ?");
        stage.showAndWait();
        if(controller.choice == null) return;
        var type = Encrypting.Type.getByName(controller.choice);
        switch (Objects.requireNonNull(type)) {
            case Internal:
                outputTextArea.setText(Encrypting.encryptSaikoC(inputTextArea.getText()));
                break;

            case Caesar:
                outputTextArea.setText(Encrypting.encryptCaesar(inputTextArea.getText()));
                break;

            case AES:
                var dialog = new TextInputDialog();
                dialog.getDialogPane().setHeaderText("Password for AES Encryption");
                dialog.setTitle("Enter a password for the encryption");
                dialog.setContentText("Please enter a password for your AES encryption:");
                var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
                dialogStage.getIcons().add(DecryptorApplication.ICON);
                dialog.showAndWait();
                if(dialog.getEditor().getText().equals("")) return;
                String psw = dialog.getEditor().getText();
                if(psw == null) return;
                outputTextArea.setText(Encrypting.encryptAES(inputTextArea.getText(), psw));
                break;
        }
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Encryption done !");
        alert.setTitle("Encryption");
        alert.initOwner(stage);
        alert.show();
    }

    public void onDecryptClicked() throws IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, InvalidKeyException {
        if(inputTextArea.getText().equals(""))  {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The input can't be empty!");
            alert.setTitle("Error");
            alert.initOwner(stage);
            alert.show();
            return;
        }
        var loader = new FXMLLoader(this.getClass().getResource("/fxml/EncryptionChooseDialog.fxml"));
        VBox box = loader.load();
        EncryptionChooseController controller = loader.getController();
        var stage = new Stage(StageStyle.UNIFIED);
        stage.initOwner(this.stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(DecryptorApplication.ICON);
        controller.stage = stage;
        stage.setScene(new Scene(box, box.getPrefWidth(), box.getPrefHeight()));
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Decryption type");
        controller.label.setText("Which decryption do you want to use ?");
        stage.showAndWait();
        if(controller.choice == null) return;
        var type = Encrypting.Type.getByName(controller.choice);
        switch (Objects.requireNonNull(type)) {
            case Internal:
                outputTextArea.setText(Encrypting.decryptSaikoC(inputTextArea.getText()));
                break;

            case Caesar:
                outputTextArea.setText(Encrypting.decryptCaesar(inputTextArea.getText()));
                break;

            case AES:
                var dialog = new TextInputDialog();
                dialog.getDialogPane().setHeaderText("Password for AES Decryption");
                dialog.setTitle("Enter a password for the decryption");
                dialog.setContentText("Please enter a password for your AES decryption:");
                var dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
                dialogStage.getIcons().add(DecryptorApplication.ICON);
                dialog.showAndWait();
                if(dialog.getEditor().getText().equals("")) return;
                String psw = dialog.getEditor().getText();
                if(psw == null) return;
                String decrypted = Encrypting.decryptAES(inputTextArea.getText(), psw);
                if(decrypted == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error while decrypting !\nProbably caused by wrong password.");
                    alert.setTitle("Error while decrypting");
                    alert.initOwner(stage);
                    alert.show();
                    return;
                }
                outputTextArea.setText(decrypted);
                break;
        }
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Decryption done !");
        alert.setTitle("Decryption");
        alert.initOwner(stage);
        alert.show();
    }

}
