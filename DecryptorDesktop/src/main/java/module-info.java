module de.shiru.decryptor.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires JDecryptor.DecryptorCore.main;
                        
    opens de.shiru.decryptor.desktop to javafx.fxml;
    exports de.shiru.decryptor.desktop;
    exports de.shiru.decryptor.desktop.controllers;
}