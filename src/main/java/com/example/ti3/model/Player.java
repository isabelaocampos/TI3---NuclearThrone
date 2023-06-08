package com.example.ti3.model;

import com.example.ti3.MainMenu;
import com.example.ti3.model.util.Vector;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class Player extends Entity {

    MainMenu ma;
    // Elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private ArrayList<Image> idleImages;
    private ArrayList<Image> runImages;
    private ArrayList<Image> attackImages;

    // referencias espaciales
    private double posX;
    private double posY;

    private Vector position;
    private Vector direction;

    // estado actual del personaje
    private int state;
    private int frame;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean isAttacking;

    public Player(Canvas canvas){
        this.state = 0;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();

        this.position = new Vector(100, 100);

        this.posX = 100;
        this.posY = 100;

        idleImages = new ArrayList<>();
        runImages = new ArrayList<>();
        attackImages = new ArrayList<>();


        for(int i = 1; i <= 3; i++){
            Image image = MainMenu.getImage("animations/hero/idle/idle_0"+i+".png");
            idleImages.add(image);
        }

        for(int i = 1; i <= 5; i++){
            Image image = MainMenu.getImage("animations/hero/run/run_0" +i+".png");
            runImages.add(image);
        }

        for(int i = 1; i <= 4; i++){
            Image image = MainMenu.getImage("animations/hero/shoot/shoot_0"+i+".png");
            attackImages.add(image);
        }

    }

    public void paint(){
        onMove();
        if (state == 0){
            graphicsContext.drawImage(idleImages.get(frame%3), position.getX(), position.getY());
            frame++;
        }
        else if(state == 1) {
            graphicsContext.drawImage(runImages.get(frame%5), position.getX(), position.getY());
            frame++;
        }
        else if (state == 2) {
            graphicsContext.drawImage(attackImages.get(frame%4), position.getX(), position.getY());
            frame++;
        }
    }

    public void onKeyPressed(KeyEvent event){
        switch (event.getCode()){
            case W:
                state = 1;
                upPressed = true;
                break;
            case S:
                state = 1;
                downPressed = true;
                break;
            case D:
                state = 1;
                rightPressed = true;
                break;
            case A:
                state = 1;
                leftPressed = true;
                break;
        }
    }

    public void onKeyReleased(KeyEvent event){
        state = 0;
        switch (event.getCode()){
            case W :
            case UP:
                upPressed = false;
                break;
            case S:
                downPressed = false;
                break;
            case D:
                rightPressed = false;
                break;
            case A:
                leftPressed = false;
                break;
        }
    }

    public void onMove(){
        if (upPressed){
            position.setY(position.getY() - 10);
        }
        if (downPressed){
            position.setY(position.getY() + 10);
        }
        if (leftPressed){
            position.setX(position.getX() - 10);
        }
        if (rightPressed){
            position.setX(position.getX() + 10);
        }
    }

    public Vector getPosition() {
        return position;
    }


}

