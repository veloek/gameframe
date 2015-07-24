/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe.api;

import gameframe.Direction;

/**
 * GFInputListener
 *
 * Interface for Game Frame input; joystick, action and alternate
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public interface GFInputListener {
    void onDirection(Direction direction);
    void onAction();
    void onAlternate();
}
