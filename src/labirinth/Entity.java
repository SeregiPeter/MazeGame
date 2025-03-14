/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labirinth;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 *
 * @author sereg
 */
public abstract class Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;

    /**
     * A konstruktor. Inicializálja a játékos spritejának x és y pozícióját, szélességét, magasságát és háttérképét.
     * @param x - X koordináta.
     * @param y - Y koordináta.
     * @param width - Szélesség.
     * @param height - Magasság.
     * @param image - Háttérkép.
     */
    public Entity(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * A sprite kirajzolását végző metódus.
     * @param g - Graphics objektum.
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(image, x, y, width, height, null);
    }

    /**
     * Az x koordináta gettere.
     * @return Az x koordináta.
     */
    public int getX() {
        return x;
    }

    /**
     * Az x koordináta settere.
     * @param x - Az x koordináta.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Az y koordináta gettere.
     * @return Az y koordináta.
     */
    public int getY() {
        return y;
    }

    /**
     * Az y koorináta settere.
     * @param y - Az y koordináta.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * A szélesség gettere.
     * @return A szélesség.
     */
    public int getWidth() {
        return width;
    }

    /**
     * A szélesség settere.
     * @param width - A szélesség.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * A magasság gettere.
     * @return A magasság.
     */
    public int getHeight() {
        return height;
    }

    /**
     * A magasság settere.
     * @param height - A magasság.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * A háttrékép gettere.
     * @return A háttérkép.
     */
    public Image getImage() {
        return image;
    }

    /**
     * A háttérkép settere.
     * @param image - A háttérkép.
     */
    public void setImage(Image image) {
        this.image = image;

    }

    /**
     * A sprite közepének x koordinátáját kisztámító metódus.
     * @return A sprite közepének x koordinátája.
     */
    public int getCenterX() {
        return x + width / 2;
    }

    /**
     * A sprite közepének y koordinátáját kisztámító metódus.
     * @return A sprite közepének y koordinátája.
     */
    public int getCenterY() {
        return y + width / 2;
    }

    /**
     * Másik Entity objektummal való ütközés eldöntésére szolgáló metódus.
     * @param other - A másik Entity objektum.
     * @return True ha ütköznek, false ha nem ütköznek.
     */
    public boolean intersectsWith(Entity other) {
        Rectangle rect = new Rectangle(x, y, width, height);
        Rectangle otherRect = new Rectangle(other.x, other.y, other.width, other.height);
        return rect.intersects(otherRect);
    }

}
