/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labirinth;

/**
 *
 * @author sereg
 */
public class HighScore {
    private final String name;
    private final int score;

    /**
     * A konstruktor. Beállítja a nevet és a pontszámot.
     * @param name - A név.
     * @param score - A pontszám
     */
    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * A név gettere.
     * @return A név.
     */
    public String getName() {
        return name;
    }

    /**
     * A pontszám gettere.
     * @return A pontszám
     */
    public int getScore() {
        return score;
    }

    /**
     * Az osztály szöveges reprezentálását megvalósító metódus.
     * @return Az adott objektum nevét és pontszámát tartalmazó szöveg.
     */
    @Override
    public String toString() {
        return name + ": " + score + " levels";
    }
}
