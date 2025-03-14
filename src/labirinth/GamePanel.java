package labirinth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.Timer;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import static labirinth.Direction.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * 
 * @author sereg
 */
public class GamePanel extends JPanel {

    private Board board;
    public final int ENTITYSIZE = 50;
    public final int BOARDSIZE = 500;
    private int levelNumber = 0;
    private final Timer moveTimer;
    private final Timer elapsedTimer;
    private final Image background;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isUpPressed;
    private boolean isDownPressed;
    private boolean dark;
    private boolean paused;
    private int elapsedTime;
    private HighScores highScores;

    /**
     * A konstruktor. Meghívja az ősosztály konstruktorát, majd létrehozza az
     * eredményeket tároló objektumot. Hozzáadja az eseményfigyelőket a
     * nyilakhoz, valamint elindítja a timereket is. Végül meghívja a restart
     * metódust, amely létrehozza a táblát reprezentáló objektumot.
     */
    public GamePanel() {
        super();
        try {
            highScores = new HighScores(10);
        } catch (SQLException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        isLeftPressed = false;
        isRightPressed = false;
        isUpPressed = false;
        isDownPressed = false;
        dark = true;
        paused = false;
        background = new ImageIcon("data/images/ground6.png").getImage();
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isLeftPressed = true;
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "released left");
        this.getActionMap().put("released left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isLeftPressed = false;
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isRightPressed = true;
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "released right");
        this.getActionMap().put("released right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isRightPressed = false;
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isDownPressed = true;
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "released down");
        this.getActionMap().put("released down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isDownPressed = false;
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isUpPressed = true;
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "released up");
        this.getActionMap().put("released up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                isUpPressed = false;
            }
        });
        moveTimer = new Timer(10, new MoveListener());
        moveTimer.start();
        elapsedTimer = new Timer(1000, e -> {
            if (!paused) {
                elapsedTime++;
            }
        });
        elapsedTimer.start();
        restart();
    }

    /**
     * A táblát megjelenítő metódus. Kirajzolja a Board-ot, valamint ha a dark
     * adattag értéke igaz, akkor beállítja a sötét hátteret a játékos
     * látószögén kívül.
     *
     * @param grphcs - Egy Graphics objektum.
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grphcs.drawImage(background, 0, 0, 520, 520, this);
        board.draw(g2d);
        if (dark) {
            Rectangle rectangle = new Rectangle(0, 0, 520, 520);
            Ellipse2D circle = new Ellipse2D.Double(board.getPlayerCenterX() - ENTITYSIZE * 3, board.getPlayerCenterY() - ENTITYSIZE * 3, ENTITYSIZE * 6, ENTITYSIZE * 6);
            Area area = new Area(rectangle);
            area.subtract(new Area(circle));
            g2d.setColor(Color.BLACK);
            g2d.fill(area);
        }
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 100, 30);
        g2d.setColor(Color.WHITE);
        g2d.drawString(elapsedTime + " seconds", 10, 20);

    }

    /**
     * Az újraindítást megvalósító metódus. Létrehozza a Board-ot, illetve
     * visszaállítja az időt és a billentyűnyomásokat nyilvántartó változókat.
     */
    public void restart() {
        try {
            board = new Board(BOARDSIZE, ENTITYSIZE, "data/maps/map" + (levelNumber % 10 + 1) + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        isLeftPressed = false;
        isRightPressed = false;
        isUpPressed = false;
        isDownPressed = false;
        paused = false;
        elapsedTime = 0;
    }

    /**
     * Ellenkezőjére állítja a dark adattagot.
     */
    public void setDark() {
        dark = !dark;
    }

    class MoveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                board.moveDragon();
                if (isLeftPressed) {
                    board.movePlayer(LEFT);
                } else if (isRightPressed) {
                    board.movePlayer(RIGHT);
                } else if (isDownPressed) {
                    board.movePlayer(DOWN);
                } else if (isUpPressed) {
                    board.movePlayer(UP);
                }
                if (board.isOver()) {
                    paused = true;
                    String playerName = JOptionPane.showInputDialog(GamePanel.this, "Game Over! You completed " + (levelNumber) + " level(s). Enter your name:");

                    if (playerName == null || playerName.trim().isEmpty()) {
                        playerName = "Unknown Player";
                    }

                    try {

                        highScores.putHighScore(playerName, levelNumber);
                    } catch (SQLException ex) {
                        Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    levelNumber = 0;
                    restart();
                }
                if (board.isWon()) {
                    paused = true;
                    JOptionPane.showMessageDialog(GamePanel.this, (levelNumber+1) + ". level completed");
                    levelNumber++;
                    restart();
                }
                repaint();
            }

        }

    }

    /**
     * A paused változó értékét a paraméterként kapott értékre állítja.
     *
     * @param b - Az új paused érték.
     */
    public void setPaused(boolean b) {
        paused = b;
    }

    /**
     * A highScores adattag gettere.
     *
     * @return - A highscores adattag.
     */
    public HighScores getLeaderboard() {
        return highScores;
    }
}
