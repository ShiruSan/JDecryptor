package de.shiru.decryptor.desktop.controllers;

import de.shiru.decryptor.core.Encrypting;
import de.shiru.decryptor.desktop.DecryptorApplication;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        SplitPane box = loader.load();
        EncryptionChooseController controller = loader.getController();
        var stage = new Stage(StageStyle.UNIFIED);
        stage.initOwner(this.stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(DecryptorApplication.ICON);
        stage.setScene(new Scene(box, box.getPrefWidth(), box.getPrefHeight()));
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Choose your encryption method");
        stage.showAndWait();
        var type = controller.getSelectedType();
        if(type == null) return;
        switch (Objects.requireNonNull(type)) {
            case Internal:
                var seedDialog = new TextInputDialog();
                seedDialog.getDialogPane().setHeaderText("Seed for Internal encryption");
                seedDialog.setTitle("Enter a text for internal shuffle generation");
                seedDialog.setContentText("Please enter a text for internal shuffle generation:");
                var seedDialogStage = (Stage) seedDialog.getDialogPane().getScene().getWindow();
                seedDialogStage.getIcons().add(DecryptorApplication.ICON);
                seedDialog.showAndWait();
                if(seedDialog.getEditor().getText().equals("")) return;
                outputTextArea.setText(Encrypting.encryptInternal(inputTextArea.getText(), seedDialog.getEditor().getText()));
                break;

            case Caesar:
                var caesarDialog = new TextInputDialog();
                caesarDialog.getDialogPane().setHeaderText("Number for Caesar displacement");
                caesarDialog.setTitle("Enter a number for Caesar displacement");
                caesarDialog.setContentText("Please enter a number for Caesar displacement:");
                var caesarDialogStage = (Stage) caesarDialog.getDialogPane().getScene().getWindow();
                caesarDialogStage.getIcons().add(DecryptorApplication.ICON);
                caesarDialog.showAndWait();
                if(caesarDialog.getEditor().getText().equals("")) return;
                String number = caesarDialog.getEditor().getText();
                try {
                    var s = Integer.parseInt(number);
                    outputTextArea.setText(Encrypting.encryptCaesar(inputTextArea.getText(), s));
                } catch (NumberFormatException ignored) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please enter a valid number.");
                    alert.setTitle("Error");
                    alert.initOwner(stage);
                    alert.show();
                    return;
                }
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
        SplitPane box = loader.load();
        EncryptionChooseController controller = loader.getController();
        var stage = new Stage(StageStyle.UNIFIED);
        stage.initOwner(this.stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(DecryptorApplication.ICON);
        stage.setScene(new Scene(box, box.getPrefWidth(), box.getPrefHeight()));
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Choose your decryption method");
        stage.showAndWait();
        var type = controller.getSelectedType();
        if(type == null) return;
        switch (Objects.requireNonNull(type)) {
            case Internal:
                var seedDialog = new TextInputDialog();
                seedDialog.getDialogPane().setHeaderText("Seed for Internal decryption");
                seedDialog.setTitle("Enter a text for internal shuffle generation");
                seedDialog.setContentText("Please enter a text for internal shuffle generation:");
                var seedDialogStage = (Stage) seedDialog.getDialogPane().getScene().getWindow();
                seedDialogStage.getIcons().add(DecryptorApplication.ICON);
                seedDialog.showAndWait();
                if(seedDialog.getEditor().getText().equals("")) return;
                outputTextArea.setText(Encrypting.decryptInternal(inputTextArea.getText(), seedDialog.getEditor().getText()));
                break;

            case Caesar:
                var caesarDialog = new TextInputDialog();
                caesarDialog.getDialogPane().setHeaderText("Number for Caesar displacement");
                caesarDialog.setTitle("Enter a number for Caesar displacement");
                caesarDialog.setContentText("Please enter a number for Caesar displacement:");
                var caesarDialogStage = (Stage) caesarDialog.getDialogPane().getScene().getWindow();
                caesarDialogStage.getIcons().add(DecryptorApplication.ICON);
                caesarDialog.showAndWait();
                if(caesarDialog.getEditor().getText().equals("")) return;
                String number = caesarDialog.getEditor().getText();
                try {
                    var key = Integer.parseInt(number);
                    outputTextArea.setText(Encrypting.decryptCaesar(inputTextArea.getText(), key));
                } catch (NumberFormatException ignored) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please enter a valid number.");
                    alert.setTitle("Error");
                    alert.initOwner(stage);
                    alert.show();
                    return;
                }
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
