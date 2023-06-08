package com.example.ti3.model.menus;

import com.example.ti3.model.KeyboardControl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.canvas.GraphicsContext;

public class Game {

    @FXML
    AnchorPane gameOver;

    @FXML
    private Button menuBtn;

    @FXML
    private ImageView playAgainBtn;

    @FXML
    private AnchorPane winner;

    @FXML
    Canvas canvas;

    @FXML
    private ImageView heartOne;

    @FXML
    private ImageView heartTwo;

    @FXML
    private ImageView heartThree;

    @FXML
    private ProgressBar reloadBar;

    @FXML
    private ImageView weaponImage;

    @FXML
    void goToMenu(ActionEvent event){
        canvas.getScene().getWindow().hide();
    }

    @FXML
    void playAgain(MouseEvent event){
        System.out.println("BBB");
        menuBtn.setDisable(true);
        playAgainBtn.setDisable(true);
        gameOver.setVisible(false);
        Avatar.resetAvatar();
        Level.resetLevels;
    }

    @FXML
    public void initialize() {
        bindHUD();
        initKeyBoard();
        initBounds();
        Thread gameThread = new Thread(() -> {
            while (0 != 1) {
                Platform.runLater(() -> {
                    Level currentLevel = Level.currentLevel();
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.drawImage(currentLevel.background, 0, 0);
                    paintEntities(currentLevel);
                    checkAvatarAlive();
                });
                try {
                    Thread.sleep(msRate());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();
    }

    GraphicsContext graphicsContext;

    public void bindHUD(){
        Ball.winner = winner;
        Avatar.hearts = new ImageView[3];
        Avatar.hearts[0] = heartOne;
        Avatar.hearts[1] = heartTwo;
        Avatar.hearts[2] = heartThree;
        Avatar.hand = weaponImage;
        Avatar.reloadBar = reloadBar;
    }

    public void initKeyBoard() {
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(KeyboardControl::onKeyPressed);
        canvas.setOnKeyReleased(KeyboardControl::onKeyReleased);
        canvas.setOnMousePressed(KeyboardControl::onMousePressed);
    }

    public void initBounds() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Rectangle2D rect = Screen.getPrimary().getBounds();
        canvas.setWidth(rect.getWidth());
        canvas.setHeight(rect.getHeight());
    }

    public static int msRate() {
        return 16;
    }



}
