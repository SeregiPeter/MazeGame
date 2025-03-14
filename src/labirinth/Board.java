/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labirinth;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;
import static labirinth.Direction.*;



/**
 *
 * @author sereg
 */
public class Board {
    private ArrayList<Entity> entities;
    private ArrayList<Direction> directions;
    private final int boardSize;
    private final int entitySize;
    private Player player;
    private Dragon dragon;
    private final Image dragonImage;
    private final Image playerImage;
    private final Image wallImage;
    private boolean over;
    private Direction dragonDirection;
    private final int PLAYERVEL = 2;
    private final int DRAGONVEL = 2;
    private final int PLAYERWIDTH = 20;
    private final int PLAYERHEIGHT = 35;

    /**
     * A konstruktor. Beállítja a háttérképeket, elmenti atz irányokat és meghívja a setupMap metódust.
     * @param boardSize - A pálya mérete.
     * @param entitySize - Egy Entity mérete.
     * @param mapPath - Az első pálya sablonjának elérési útvonala.
     * @throws IOException
     */
    public Board(int boardSize, int entitySize, String mapPath) throws IOException {
        over = false;
        dragonImage = new ImageIcon("data/images/dragon4.png").getImage();
        playerImage = new ImageIcon("data/images/player3.png").getImage();
        wallImage = new ImageIcon("data/images/wall2.png").getImage();
        this.boardSize = boardSize;
        this.entitySize = entitySize;
        directions = new ArrayList<>();
        directions.add(UP);
        directions.add(DOWN);
        directions.add(RIGHT);
        directions.add(LEFT);
        dragonDirection = UP;
        setupMap(mapPath);
    }

    /**
     * A pálya és alkotóelemei beolvasását végző metódus.
     * @param mapPath - A pálya sablonjának elérési útvonala.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void setupMap(String mapPath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(mapPath));
        entities = new ArrayList<>();
        int row = 0;
        String line;
        while ((line = br.readLine()) != null) {
            int col = 0;
            for (char entityType : line.toCharArray()) {
                switch(entityType) {
                    case 'W':
                        entities.add(new Wall(col * entitySize, row * entitySize, entitySize, entitySize, wallImage));
                        break;
                    case 'P':
                        player = new Player(col * entitySize, row * entitySize, PLAYERWIDTH, PLAYERHEIGHT, playerImage, PLAYERVEL, PLAYERVEL);
                        entities.add(player);
                        break;
                    case 'D':
                        dragon = new Dragon(col * entitySize, row * entitySize, entitySize, entitySize, dragonImage, DRAGONVEL, DRAGONVEL);
                        entities.add(dragon);
                        break;
                        
                }
                col++;
            }
            row++;
        } 
    }

    /**
     * A játékost a megadott irányba mozgató metódus. Ha a játékos "belemegy" egy másik Entitiy-be vagy kimegy a pályáról, akkor visszaállítja a pozícióját. Ha a játékos találkozik a sárkánnyal vagy beér a célba, akkor az ezeket jelző adattagokat true-ra állítja.
     * @param direction - A megadott irány.
     */
    public void movePlayer(Direction direction) {
        player.move(direction);
        if(player.getY() < 0) {
            player.setY(0);
        }
        if(player.getY() > boardSize) {
            player.setY(boardSize);
        }
        if(player.getX() < 0) {
            player.setX(0);
        }
        if(player.getX() > boardSize) {
            player.setX(boardSize);
        }
        Entity intersectsWith = null;
        for(Entity entity : entities) {
            if(!(entity instanceof Player) && (player.intersectsWith(entity))) {
                intersectsWith = entity;
                break;
            }
        }
        if(intersectsWith instanceof Dragon) {
            over = true;
        }
        if(intersectsWith != null) {
            switch(direction) {
                case UP:
                    player.move(DOWN);
                    break;
                case DOWN:
                    player.move(UP);
                    break;
                case LEFT:
                    player.move(RIGHT);
                    break;
                case RIGHT:
                    player.move(LEFT);
                    break;
            }
        }
        
        

    }

    /**
     * A sárkányt az aktuális irányba mozgató játékos. Ha a sárkány falnak ütközik, akkor egy véletlenszerű új irányt választ.
     */
    public void moveDragon() {
        Direction direction = dragonDirection;
        boolean needToChange = false;
        dragon.move(direction);
        if(dragon.getY() < 0) {
            dragon.setY(0);
            needToChange = true;
        }
        if(dragon.getY() > boardSize) {
            dragon.setY(boardSize);
            needToChange = true;
        }
        if(dragon.getX() < 0) {
            dragon.setX(0);
            needToChange = true;
        }
        if(dragon.getX() > boardSize) {
            dragon.setX(boardSize);
            needToChange = true;
        }
        Entity intersectsWith = null;
        for(Entity entity : entities) {
            if((entity instanceof Wall || entity instanceof Player) && (dragon.intersectsWith(entity))) {
                intersectsWith = entity;
                break;
            }
        }
        if(intersectsWith instanceof Player) {
            over = true;
        }
        if(intersectsWith != null) {
            needToChange = true;
            switch(direction) {
                case UP:
                    dragon.move(DOWN);
                    break;
                case DOWN:
                    dragon.move(UP);
                    break;
                case LEFT:
                    dragon.move(RIGHT);
                    break;
                case RIGHT:
                    dragon.move(LEFT);
                    break;
            }
        }
        if(needToChange) {
            dragonDirection = getDirectionExcept(dragonDirection);
        }
    }
    
    /**
     * Egy a paramétertől különböző, véletlenszerű irányt visszaadó metódus.
     * @param d - A kihagyni kívánt irány.
     * @return A véletlenszerű új irány.
     */
    public Direction getDirectionExcept(Direction d) {
        Collections.shuffle(directions);
        Direction result = directions.get(0);
        for(Direction direction : directions) {
            if(direction != d) {
                result = direction;
                break;
            }
        }
        return result;
    }
    
    /**
     * A táblát kirajzoló metódus. Végigmegy az alkotóelemeken, és egyesével kirajzolja őket.
     * @param g - Egy Graphics objektum.
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for(Entity entity : entities) {
            entity.draw(g2d);
        }  
    }

    /**
     * A játékos közepének x koordinátáját visszaadó metódus.
     * @return A játékos közepének x koordinátája.
     */
    public int getPlayerCenterX() {
        return player.getCenterX();
    }

    /**
     * A játékos közepének y koordinátáját visszaadó metódus.
     * @return A játékos közepének y koordinátája.
     */
    public int getPlayerCenterY() {
        return player.getCenterY();
    }
    
    /**
     * A játékos x koordinátáját visszaadó metódus.
     * @return A játékos x koordinátája.
     */
    public int getPlayerX() {
        return player.getX();
    }

    /**
     * A játékos y koordinátáját visszaadó metódus.
     * @return A játékos y koordinátája.
     */
    public int getPlayerY() {
        return player.getY();
    }
    
    /**
     * Visszaadja, hogy a játékos játékban van-e még.
     * @return True ha a játékos még játékban van, false ha a játékos már elvesztette a játékot.
     */
    public boolean isOver() {
        return over;
    }
    
    /**
     * Visszaadja, hogy a játékos megnyerte-e a játékot. Ha az y koordináta 0 (azaz elérte a pálya tetejét) akkor nyert.
     * @return True ha a játékos megnyerte a játékot, false ha nem.
     */
    public boolean isWon() {
        return player.getY() == 0;
    }
}
