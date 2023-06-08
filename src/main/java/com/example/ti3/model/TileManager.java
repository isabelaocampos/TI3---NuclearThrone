package com.example.ti3.model;

import com.example.ti3.control.MainController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;

public class TileManager {

    MainController main;
    private Tile[] tiles;

    public static final String Tile_Path = MainController.Main_Resources_Path + "\\tiles";
    private final int tiles_amount = 12;
    private int[][] mapTile;
    private ArrayList<Point> validatePosition;
    private int maxWorldCol;
    private int maxWorldRow;

    private final String stagePath;


    public TileManager(int rows, int columns, String stagePath) {
        this.stagePath = stagePath;
        this.maxWorldCol = columns;
        this.maxWorldRow = rows;
        this.validatePosition = new ArrayList<>();
        tiles = new Tile[tiles_amount];
        mapTile = new int[rows][columns];



    }



    public void initializeTilesImages(){
        tiles[0] = new Tile(getTileImage("\\blocktiles\\tile032.png"));
        tiles[1] = new Tile(getTileImage("\\blocktiles\\tile034.png"));
        tiles[2] = new Tile(getTileImage("\\blocktiles\\tile035.png"));

        for (int i = 3; i < tiles_amount; i++){
            tiles[i] = new Tile(getTileImage("\\darktiles\\tile0" + i + ".png"));
            //if (i > 6){
                //tiles[i].setCollision(true);
            //}
        }

    }

    private Image getTileImage(String relativePath){
        return new Image(Tile_Path + relativePath);
    }

    public void loadGame(){
        try{
            File File = new File(stagePath);
            FileInputStream fis = new FileInputStream(File);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            int col = 0;
            int row = 0;
            while (col < maxWorldCol && row < maxWorldRow){
                String line = reader.readLine();
                while (col < maxWorldCol){
                    String[] num = line.split(" ");
                    int nume = Integer.parseInt(num[col]);
                    mapTile[row][col] = nume;
                    col++;
                    //if (!tiles[nume].isCollision() && row != 11 && row != 12 && row != 13){
                       // validatePosition.add(new Point(col,row));
                    //}
                }
            if (col == maxWorldCol){
                col = 0;
                row ++;
            }
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext gc, int playerX, int playerY, int screenX, int screenY){
        int col = 0;
        int row = 0;

        while(col < maxWorldCol && row < maxWorldRow){
            int titleNum = mapTile[row][col];

            int worldX = col * 48;
            int worldY = row * 48;
            int screenInX = worldX - playerX + screenX;
            int screenInY = worldY - playerY + screenY + 2;
            if(worldX + 48 > playerX - screenX && worldX - 48 < playerX + screenX &&
                    worldY + 48 > playerY - screenY && worldY - 48 < playerY + screenY){
                gc.drawImage(tiles[titleNum].getImage(), screenInX, screenInY, 48, 48);
            }
            col++;

            if(col == maxWorldCol){
                col = 0;
                row++;
            }
        }

    }



}
