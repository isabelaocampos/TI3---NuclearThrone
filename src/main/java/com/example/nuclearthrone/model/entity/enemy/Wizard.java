package com.example.nuclearthrone.model.entity.enemy;

import java.util.HashMap;

import com.example.nuclearthrone.MainMenu;
import com.example.nuclearthrone.model.entity.AnimationType;
import com.example.nuclearthrone.model.entity.Entity;
import com.example.nuclearthrone.model.entity.ammo.Bullet;
import com.example.nuclearthrone.model.entity.ammo.EnemyBullet;
import com.example.nuclearthrone.model.level.Level;
import com.example.nuclearthrone.model.util.Direction;
import com.example.nuclearthrone.model.util.Images;
import com.example.nuclearthrone.model.util.Vector;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Wizard extends Enemy {

    public static final double RANGE = 250;
    public static final double RANGE_GAP = 50;
    public static final double SECONDS_PER_ATTACK = 3;
    public static final double WIDTH = 50;
    public static final double HEIGHT = 50;
    public static final int HEALTH = 100;
    public static final int SPEED = 4;
    public static HashMap<AnimationType, Image[]> animations;

    private long nextShot;

    public Wizard(double x, double y, int level) {
        super(x, y, WIDTH, HEIGHT, HEALTH, -1, SPEED, level);
        nextShot = System.currentTimeMillis();
        String uri = "file:" + MainMenu.getFile("entities/wizard.png").getPath();
        sprite = new Image(uri, getWidth(), getHeight(), true, false, false);
    }

    @Override
    public void attack(Entity entity) {
        double distance = distanceTo(entity.getCenterX(), entity.getCenterY());
        if (distance > (RANGE + RANGE_GAP)) {
            moveTowards(entity);
        } else if (distance < (RANGE - RANGE_GAP)) {
            moveAwayFrom(entity);
        }
        setX(getX() + Vector.randNegOrPos() * (Math.random() * 2));
        setY(getY() + Vector.randNegOrPos() * (Math.random() * 2));
        if (System.currentTimeMillis() >= nextShot) {
            shoot(entity.getX(), entity.getY());
        }
    }

    public void shoot(double x, double y) {
        nextShot = System.currentTimeMillis() + ((int) ((Math.random() * SECONDS_PER_ATTACK) + SECONDS_PER_ATTACK) * 1000);
        Bullet bullet = new EnemyBullet(this, Level.getSelected());
        bullet.shootTo(x, y, 0);
        Level.currentLevel().bullets.add(bullet);
    }

    @Override
    public void initAnimation() {
        if (animations == null) {
            animations = new HashMap<>();
            animations.put(AnimationType.IDLE, new Image[1]);
            String uri = "file:" + MainMenu.getFile("entities/wizard.png").getPath();
            animations.get(AnimationType.IDLE)[0] = new Image(uri, getWidth(), getHeight(), false, true, false);
        }
    }

    @Override
    public Timeline getAnimation() {
        return new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            if (spriteStage < animations.get(animation).length - 1) {
                spriteStage++;
            } else {
                if (animation != AnimationType.DEATH) {
                    if (animation != AnimationType.IDLE && animation != AnimationType.RUN) {
                        animation = AnimationType.IDLE;
                    }
                    spriteStage = 0;
                }
            }
            sprite = animations.get(animation)[spriteStage];
            if (lookingAt == Direction.LEFT)
                sprite = Images.mirrorImageHorizontally(sprite);
        }));
    }
}
