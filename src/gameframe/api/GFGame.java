/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.api;

import gameframe.Direction;
import gameframe.HighscoreRank;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * GFGame
 *
 * Superclass of any Game Frame game's main class
 *
 * Supplies the game with canvas size and joystick direction
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
abstract public class GFGame implements GFInputListener {
    private Direction direction;
    private final Dimension size;

    public GFGame(Dimension size) {
        this.size = size;
    }

    protected Direction getDirection() {
        return direction;
    }

    private void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Dimension getSize() {
        return size;
    }

    protected HighscoreRank gameOver(int score) {
        return new HighscoreRank(score, 1, true);
    }

    abstract public void update(float delta, Graphics g);
    
    @Override
    public void onDirection(Direction direction) {
        setDirection(direction);
    }

}
