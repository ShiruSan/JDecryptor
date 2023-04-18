package de.shiru.decryptor.desktop.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import de.shiru.decryptor.core.Encrypting;

public class EncryptionChooseController {
    public Stage stage;
    public ComboBox<String> combobox;
    public String choice;
    public Label label;

    public void initialize() {
        combobox.getItems().addAll(Encrypting.Type.asStringList());
    }

    public void onComboboxAction() {
        choice = combobox.selectionModelProperty().get().getSelectedItem();
        stage.close();
    }

}
