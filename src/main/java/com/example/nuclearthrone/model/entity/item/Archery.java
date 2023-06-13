package com.example.nuclearthrone.model.entity.item;

import com.example.nuclearthrone.MainMenu;
import com.example.nuclearthrone.model.entity.AnimationType;
import com.example.nuclearthrone.model.entity.ammo.Arrow;
import com.example.nuclearthrone.model.entity.ammo.Bullet;
import com.example.nuclearthrone.model.level.Level;

import javafx.scene.image.Image;

public class Archery extends Weapon {

    public static final double DELAY = 1200;
    public static final double WIDTH = 10;
    public static final double HEIGHT = 20;
    public static final String SPRITE = "entities/weapon/arco.png";

    public Archery(double x, double y) {
        super(x, y, WIDTH, HEIGHT,DELAY);
        String uri = "file:" + MainMenu.getFile(SPRITE).getPath();
        this.sprite = new Image(uri, 20, 40, false, true, false);
    }

    @Override
    public AnimationType attack(double x, double y) {
        Bullet bullet = new Arrow(Level.getSelected());
        bullet.setVisible(false);
        bullet.shootTo(x, y, DELAY);
        Level.currentLevel().bullets.add(bullet);
        return AnimationType.SHOOT;
    }
}
