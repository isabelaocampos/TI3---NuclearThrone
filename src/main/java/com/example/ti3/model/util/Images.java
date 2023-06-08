package com.example.ti3.model.util;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Images {
    public static Image rotateImage(Image image, double angle) {
        double centerX = image.getWidth() / 2;
        double centerY = image.getHeight() / 2;

        Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.translate(centerX, centerY);
        gc.rotate(angle);
        gc.translate(-centerX, -centerY);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        gc.drawImage(image, 0, 0);

        return canvas.snapshot(params, null);
    }
}
