/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labirinth;

import java.awt.Image;

/**
 *
 * @author sereg
 */
public class Player extends Entity{
    private int velX;
    private int velY;

    /**
     * A konstruktor. Meghívja az ősosztály konstruktorát, valamint beállítja a x és y irány menti sebességet.
     * @param x - X koordináta.
     * @param y - Y koordináta.
     * @param width - Szélesség.
     * @param height - Magasság.
     * @param image - Háttérkép.
     * @param velX - X irány menti sebesség.
     * @param velY - Y irány menti sebesség.
     */
    public Player(int x, int y, int width, int height, Image image, int velX, int velY) {
        super(x, y, width, height, image);
        this.velX = velX;
        this.velY = velY;
    }
    
    /**
     * A játékos mozgásának metódusa. A megadott irányba tolja a játékost a sebesség mértékével.
     * @param direction - Az irány.
     */
    public void move(Direction direction) {
        switch(direction) {
            case UP:
                y -= velY;
                break;
            case DOWN:
                y += velY;
                break;
            case LEFT:
                x -= velX;
                break;
            case RIGHT:
                x += velX;
                break;
        }
    }

}
