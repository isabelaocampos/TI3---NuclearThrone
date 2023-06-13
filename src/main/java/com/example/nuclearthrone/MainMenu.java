package com.example.nuclearthrone;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import com.example.nuclearthrone.model.entity.Avatar;
import com.example.nuclearthrone.model.level.Level;

import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainMenu extends Application {
    
    static Stage gameStage;

    @FXML
    private Button playButton;

    @FXML
    private Button quitButton;

    public static void main(String[] args) {
        launch(args);
    }

    private Canvas canvas;


    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Main Menu");

        // Load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getView("main-menu"));
        AnchorPane root = fxmlLoader.load();



        // Buttons references
        playButton = (Button) fxmlLoader.getNamespace().get("playButton");
        quitButton = (Button) fxmlLoader.getNamespace().get("quitButton");

        playButton.setOnAction(e -> {
            showLoadingScreen();
        });

        quitButton.setOnAction(e -> {
            primaryStage.close();
        });

        VBox vBox = new VBox(20, playButton, quitButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setTranslateX(260);
        vBox.setTranslateY(200);

        root.getChildren().addAll(vBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.onCloseRequestProperty().set(event -> {
        });

    }

    public static void openWindow(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getView(fxml));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Nuclear Throne");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
    public static void initGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getView("game"));
            Scene scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());

            ImageCursor cursor = new ImageCursor(new Image(getFile("cursor.png").getPath()),30,30);
            scene.setCursor(cursor);

            if(gameStage == null){
                gameStage = new Stage();
                gameStage.setWidth(1280);
                gameStage.setHeight(720);
                gameStage.setTitle("Nuclear Throne");
                gameStage.resizableProperty().set(false);
            }
            
            gameStage.setScene(scene);
            gameStage.show();
            gameStage.setOnCloseRequest(event->{
                Avatar.resetAvatar();
                Level.resetLevels();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showLoadingScreen() {
        Stage loadingStage = new Stage();
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("windows/loading.fxml"));
        try {
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            loadingStage.setScene(scene);
            loadingStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            loadingStage.close();
            initGame();
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public static URL getView(String name) {
        return MainMenu.class.getResource("windows/" + name + ".fxml");
    }

    public static File getFile(String fileName) {
        return new File(Objects.requireNonNull(MainMenu.class.getResource(fileName)).getPath());
    }

    public static double getWidth() {
        return 1280;
    }

    public static double getHeight() {
        return 720;
    }

    public static int msRate() {
        return 16;
    }
}
