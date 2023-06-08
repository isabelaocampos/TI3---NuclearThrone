package com.example.ti3.screens;

import com.example.ti3.model.*;
import com.example.ti3.control.MainController;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ScreenA extends BaseScreen{

    private Player player;
    private TileManager tileM;

    private ArrayList<Enemy> enemies;

    private ArrayList<Bullet> bullets;

    MainController mc = new MainController();

    public ScreenA(Canvas canvas) {
        super(canvas);
        player = new Player(canvas);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        Enemy enemy = new Enemy(canvas, new Vector(300, 0));
        enemy.start();
        enemies.add(enemy);
    }

    @Override
    public void paint() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0,0, mc.screenWidth, mc.screenHeight);

        tileM.initializeTilesImages();
//        COMO AÚN NO HAY RECURSOS GRAFICOS AÚN NO SE PUEDE PINTAR EL PLAYER
        player.paint();

        for (Enemy b: enemies) {
            b.paint();
        }

        for (int i = 0; i< bullets.size(); i++){
            bullets.get(i).paint();

            if(bullets.get(i).getPositionX() > canvas.getWidth()){
                bullets.remove(i);
                i--;
            }
        }

        for (int i = 0; i< enemies.size(); i++){
            for (int j = 0; j < bullets.size(); j++){

                Enemy actualEnemy = enemies.get(i);
                Bullet actualBullet = bullets.get(j);

                double distance = Math.sqrt(
                        Math.pow(actualEnemy.getPosition().getX() - actualBullet.getPositionX(), 2) +
                                Math.pow(actualEnemy.getPosition().getY() - actualBullet.getPositionY(), 2)
                );

                if (distance <= 10){
                    Enemy deletedEnemy = enemies.remove(i);

                    deletedEnemy.setAlive(false);
                    bullets.remove(j);
                    return;
                }
                System.out.println(distance);
            }
        }

    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        player.onKeyPressed(event);
    }

    @Override
    public void onKeyReleased(KeyEvent event) {
        player.onKeyReleased(event);
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        double diffX = event.getX() - player.getPosition().getX();
        double diffY = event.getY() - player.getPosition().getY();

        Vector diff = new Vector(diffX, diffY);

        diff.normalize();
        diff.setSpeed(4);

        bullets.add(
                new Bullet(canvas, new Vector( player.getPosition().getX(), player.getPosition().getY()), diff)
        );


    }
}
