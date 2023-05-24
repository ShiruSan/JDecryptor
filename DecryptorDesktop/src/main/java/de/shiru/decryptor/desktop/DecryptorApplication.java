package de.shiru.decryptor.desktop;

import de.shiru.decryptor.desktop.controllers.MainController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.util.List;
import java.util.Objects;

public class DecryptorApplication extends Application {
    public static final Image ICON = new Image(Objects.requireNonNull(DecryptorApplication.class.getResourceAsStream("/images/icon.ico")));
    public ThemeManager themeManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        new ThemeManager();
        var loader = new FXMLLoader(this.getClass().getResource("/fxml/Decryptor.fxml"));
        VBox container = loader.load();
        MainController mainController = loader.getController();
        mainController.stage = primaryStage;
        mainController.init();
        var scene = new Scene(container, container.getPrefWidth(), container.getPrefHeight());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(ICON);
        primaryStage.setTitle("Decryptor 1.4");
        themeManager = new ThemeManager();
        themeManager.setOnThemeChange((theme) -> {
            container.getStylesheets().clear();
            container.getStylesheets().add(String.valueOf(theme.getURL()));
        });
        themeManager.setup();
        var splashStage = new Stage(StageStyle.UNDECORATED);
        splashStage.initModality(Modality.NONE);
        var splashScreen = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/splash-screen.png")));
        splashStage.setScene(new Scene(new Group(List.of(new ImageView(splashScreen))), splashScreen.getWidth(), splashScreen.getHeight()));
        splashStage.setAlwaysOnTop(true);
        splashStage.getIcons().add(ICON);
        splashStage.setOnShown((x) -> {
            var transition = new PauseTransition(Duration.seconds(1.5));
            transition.setOnFinished((y) -> splashStage.close());
            transition.play();
        });
        splashStage.showAndWait();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
