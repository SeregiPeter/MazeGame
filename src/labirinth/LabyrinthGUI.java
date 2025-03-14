/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labirinth;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LabyrinthGUI {

    private JFrame frame;
    private GamePanel gamePanel;

    /**
     * A kosntruktor. Létrehozza a frame-et, illetve a menüsort a funkcionalitással együtt, valamint példányosítja a gamePanel-t.
     */
    public LabyrinthGUI() {
        frame = new JFrame("Labirinth");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("data/images/wall2.png");
        frame.setIconImage(icon.getImage());
        gamePanel = new GamePanel();
        frame.getContentPane().add(gamePanel);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenuItem newMenuItem = new JMenuItem("New game");
        gameMenu.add(newMenuItem);
        newMenuItem.addActionListener((ActionEvent ae) -> {
            gamePanel.restart();
        });
        JMenuItem lightMenuItem = new JMenuItem("Light");
        gameMenu.add(lightMenuItem);
        lightMenuItem.addActionListener((ActionEvent ae) -> {
            gamePanel.setDark();
        });

        JMenuItem highScoresItem = new JMenuItem("Leaderboard");
        gameMenu.add(highScoresItem);
        highScoresItem.addActionListener((ActionEvent ae) -> {
            gamePanel.setPaused(true);
            ArrayList<HighScore> highScoresList = new ArrayList<HighScore>();
            try {
                highScoresList = gamePanel.getLeaderboard().getHighScores();
            } catch (SQLException ex) {
                Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (highScoresList.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Leaderboard is empty", "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder leaderboardText = new StringBuilder();

            for (HighScore highScore : highScoresList) {
                leaderboardText.append(highScore.toString());
                leaderboardText.append("\n");
            }

            JTextArea textArea = new JTextArea(leaderboardText.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JOptionPane.showMessageDialog(frame, scrollPane, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
            gamePanel.setPaused(false);
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        exitMenuItem.addActionListener((ActionEvent ae) -> {
            System.exit(0);
        });

        frame.setPreferredSize(new Dimension(515, 560));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

}
