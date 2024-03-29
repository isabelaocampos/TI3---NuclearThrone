package com.example.nuclearthrone.model.menus;

import java.util.ArrayList;
import java.util.List;

import com.example.nuclearthrone.model.KeyboardControl;
import com.example.nuclearthrone.model.entity.Avatar;
import com.example.nuclearthrone.model.entity.Entity;
import com.example.nuclearthrone.model.entity.MovableEntity;
import com.example.nuclearthrone.model.entity.ammo.Bullet;
import com.example.nuclearthrone.model.entity.ammo.EnemyBullet;
import com.example.nuclearthrone.model.entity.enemy.Enemy;
import com.example.nuclearthrone.model.entity.enviroment.Decoration;
import com.example.nuclearthrone.model.entity.enviroment.Wall;
import com.example.nuclearthrone.model.level.Level;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

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
    private ImageView heartThree;

    @FXML
    private ImageView heartTwo;

    @FXML
    private ProgressBar reloadBar;

    @FXML
    private ImageView weaponImage;

    @FXML
    void goToMenu(ActionEvent event) {
        canvas.getScene().getWindow().hide();
    }

    @FXML
    void playAgain(MouseEvent event) {
        System.out.println("BBB");
        menuBtn.setDisable(true);
        playAgainBtn.setDisable(true);
        gameOver.setVisible(false);
        Avatar.resetAvatar();
        Level.resetLevels();
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
                    checkAvatarWinAlive();
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
    private boolean allEnemiesEliminated = false;

    public void bindHUD(){

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
        graphicsContext = canvas.getGraphicsContext2D();
        Rectangle2D rect = Screen.getPrimary().getBounds();
        canvas.setWidth(rect.getWidth());
        canvas.setHeight(rect.getHeight());
    }

    public void paintEntities(Level current) {
        for (Wall wall : current.walls) {
            wall.draw(graphicsContext);
        }
        for (Decoration decoration : current.decorations) {
            decoration.draw(graphicsContext);
        }
        for (int i = 0; i < current.items.size(); i++) {
            current.items.get(i).draw(graphicsContext);
        }

        bulletsInteraction(current);
        Avatar.getInstance().draw(graphicsContext);
        for (MovableEntity entity : current.entities) {
            entity.draw(graphicsContext);
        }

        // Verificar si todos los enemigos han sido eliminados
        if (current.entities.isEmpty()) {
            allEnemiesEliminated = true;
        }
    }


    public void bulletsInteraction(Level currentLevel) {
        for (int i = 0; i < currentLevel.bullets.size(); i++) {
            Bullet currentB = currentLevel.bullets.get(i);
            currentB.draw(graphicsContext);
            if (currentB instanceof EnemyBullet) {
                if (currentB.intersects(Avatar.getInstance())) {
                    Avatar.getInstance().takeDamage(currentB);
                    currentLevel.bullets.remove(i);
                    i--;
                    continue;
                }
            }
            ArrayList<Entity> intersected = currentB.intersectsAny(currentLevel.walls, currentLevel.entities);
            for (Entity entity : intersected) {
                entity.takeDamage(currentB);
                break;
            }
            if (!intersected.isEmpty()) {
                currentB.health--;
                if (currentB.health <= 0) {
                    currentLevel.bullets.remove(i);
                    i--;
                }
            }
        }
    }

    public void checkAvatarAlive(){
        if (!Avatar.getInstance().isAlive ) {
            // El jugador ha ganado
            gameOver.setVisible(true);
            playAgainBtn.setDisable(false);
            menuBtn.setDisable(false);
        }
    }
    public void checkAvatarWinAlive(){
        if (Avatar.getInstance().isAlive && !enemiesLeft(Level.levels) && allEnemiesEliminated == true) {
            // El jugador ha ganado
            gameOver.setVisible(true);
            playAgainBtn.setDisable(false);
            menuBtn.setDisable(false);
        }
    }

    public static boolean enemiesLeft(List<Level> levels) {
        for(Level level : levels){
            for(MovableEntity entity : level.entities){
                if(entity instanceof Enemy){
                    return true;
                }
            }
        }
        return false;
    }

    public static int msRate() {
        return 16;
    }
}
