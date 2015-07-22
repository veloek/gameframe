/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe.api;

/**
 * GFInputListener
 *
 * Interface for Game Frame buttons; action and alternate
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public interface GFInputListener {
    void onAction();
    void onAlternate();
}
