package com.example.ti3.model;

import javafx.scene.image.Image;


public class Tile {
    public boolean collision = false;
    private Image image;

    public Tile(Image image){
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
