/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe.api;

import gameframe.Direction;
import gameframe.Timer;
import gameframe.TimerListener;
import gameframe.Window;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * GFTestFrame
 *
 * JFrame application for testing a Game Frame game while developing
 *
 * Just instantiate this class with an instance of your game to get a
 * JFrame running. Optional debug information.
 *
 * @author Vegard Løkken <vegard@loekken.org>
 * @author Hallgeir Løkken <istarnion@gmail.com>
 */
public class GFTestFrame implements TimerListener {

    private final Window window;
    private final Timer timer;
    
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private GFGame game;
    
    private boolean debug;

    private boolean takedown = false;
    
    public GFTestFrame(GFGame game, boolean debug) {
        this.game = game;
        this.debug = debug;
        
        window = new Window("GameFrame test", WIDTH, HEIGHT);
        
        // TODO: Use joystick and button input instead of keyboard
        window.getFocusedComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_ESCAPE) {
                    System.exit(0); // TODO: Cleaner exit
                } else if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN ||
                        code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
                    if (game != null) setDirection(code);
                } else if (code == KeyEvent.VK_ENTER) {
                    if (game != null) game.onAction();
                } else if (code == KeyEvent.VK_SPACE) {
                    if (game != null) game.onAlternate();
                }
            }
        });

        timer = new Timer(60);
        timer.addListener(this);
        timer.start();  // Does not return until timer.stop() is called
        dispose(null);
    }

    @Override
    public void update(float delta) {
        Graphics2D g = window.getDrawGraphics();

        if(game != null) {
            game.update(delta, g);
        }

        if(debug) {
            int fontSize = 8;
            int padding = 1;
            
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
            g.setColor(Color.LIGHT_GRAY);

            String debugStr = "Control: arrow keys, " +
                    "Action: ENTER, Alternate: SPACE, FPS: ";
            g.drawString(debugStr + timer.getFPS(), padding, fontSize+padding);
        }
        
        window.render();
        
        if(takedown || window.isDisposed()) {
            timer.stop();
        }
    }
    
    private void setDirection(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                game.onDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                game.onDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                game.onDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                game.onDirection(Direction.RIGHT);
                break;
            default:
                game.onDirection(null);
        }
    }
    
    /**
     * Safely exits the application.
     * Pass in null for the error string if you just want to exit cleanly.
     * 
     * @param err The error message to be printed if needed.
     */
    private void dispose(String err) {
        int errCode = 0;
        if(err != null) {
            errCode = 1;
            System.err.println(err);
        }
        
        window.dispose();
        System.out.println("Exited successfully.");
        System.exit(errCode);
    }
}
