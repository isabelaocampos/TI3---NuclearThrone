package com.example.ti3.model.menus;

import com.example.ti3.model.KeyboardControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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
    void playAgain(MouseEvent){
        System.out.println("BBB");
        menuBtn.setDisable(true);
        playAgainBtn.setDisable(true);
        gameOver.setVisible(false);
        Avatar.resetAvatar();
        Level.resetLevels;
    }

    public void initKeyBoard() {
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(KeyboardControl::onKeyPressed);
        canvas.setOnKeyReleased(KeyboardControl::onKeyReleased);
        canvas.setOnMousePressed(KeyboardControl::onMousePressed);
    }



}
