package com.example.ti3;

import com.example.ti3.control.MainController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainMenu extends Application {

    static Stage gameStage;

    @FXML
    private Button playButton;

    @FXML
    private Button manualButton;

    @FXML
    private Button exitButton;

    public static void main(String[] args) {
        launch();
    }

    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Robots vs Bichos - Menu");

        FXMLLoader fxmlLoader = new FXMLLoader(getView("hello_view"));
        AnchorPane root = fxmlLoader.load();

        playButton = (Button)fxmlLoader.getNamespace().get("playButton");
        manualButton = (Button)fxmlLoader.getNamespace().get("manualButton");
        exitButton = (Button)fxmlLoader.getNamespace().get("exitButton");

        playButton.setOnAction(e -> {
            //showLoadingScreen();
        });

        manualButton.setOnAction(e -> {
            openWindow("manual");
        });

        exitButton.setOnAction(e -> {
            primaryStage.close();
        });

        VBox vBox = new VBox(20,playButton, manualButton, exitButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setTranslateX(260);
        vBox.setTranslateY(200);

        root.getChildren().addAll(background, vBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
            MainController controller = fxmlLoader.getController();
            controller.setRunning(false);

        });


    }

    public static void openWindow(String fxml){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getView(fxml));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Robots vs Bichos");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void initGame(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getView("game"));
            Scene scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());

            if(gameStage == null){
                gameStage = new Stage();
                gameStage.setWidth(1280);
                gameStage.setHeight(720);
                gameStage.setTitle("Robots vs Bichos");
                gameStage.resizableProperty().set(false);


            }

            gameStage.setScene(scene);
            gameStage.show();
            gameStage.setOnCloseRequest(event -> {
                Avatar.resetAvatar();
                Level.resetLevels();
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static URL getView(String name){
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

    public static Image getImage(String fileName) {
        return new Image(getFile(fileName).getPath());
    }
}