module de.shiru.decryptor.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires JDecryptor.DecryptorCore;
    requires net.sf.cssbox.jstyleparser;
                        
    opens de.shiru.decryptor.desktop to javafx.fxml, JDecryptor.DecryptorCore;
    exports de.shiru.decryptor.desktop;
    exports de.shiru.decryptor.desktop.controllers;
}