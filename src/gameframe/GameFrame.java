/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe;

import gameframe.api.GFGame;
import gameframe.gui.MainMenu;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.security.Policy;

/**
 * GameFrame
 *
 * Main class of the Game Frame project
 *
 * Loads the menu and sets up key listeners
 *
 * @author Vegard Løkken <vegard@loekken.org>
 * @author Hallgeir Løkken <istarnion@gmail.com>
 */
public class GameFrame implements TimerListener {
    
    private final Window window;
    
    private final Timer timer;
    
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final String VERSION = "v0.0.1";

    private static GFGame game;
    private static MainMenu menu;

    private boolean takedown = false;
    
    public GameFrame() {
        window = new Window("GameFrame", WIDTH, HEIGHT);

        menu = new MainMenu(new Dimension(WIDTH, HEIGHT));
        mainMenu();

        // TODO: Use joystick and button input instead of keyboard
        window.getFocusedComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_ESCAPE) {
                    mainMenu();
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
        }
    }

    public static void startGame(GFGame game) {
        if (game != null) {
            GameFrame.game = game;
        }
    }

    public static void mainMenu() {
        startGame(menu);
    }

    public static GFGame loadGame(URL url) {
        boolean isGFGame = false;
        Class gameClass = null;
        GFGame instance = null;

        try {
            GFClassLoader cl = new GFClassLoader(url);
            String className = cl.getMainClassName();
            gameClass = cl.loadClass(className);
            isGFGame = GFGame.class.isAssignableFrom(gameClass);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (isGFGame) {
            Constructor cont = null;
            try {
                cont = gameClass.getConstructor(Dimension.class);
            } catch (Exception e) {
                System.err.println(gameClass.getName() +
                        ": No constructor with Dimension argument");

                // Try to find constructor without arguments
                try {
                    cont = gameClass.getConstructor();
                } catch (Exception e2) {
                    System.err.println(gameClass.getName() +
                            ": No empty constructor either. Giving up...");
                }
            }

            if (cont != null) {
                try {
                    instance = (GFGame) cont.newInstance(new Dimension(WIDTH, HEIGHT));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        return instance;
    }

    /**
     * Safely exits the application.
     * Pass in null for the error string if you just want to exit cleanly.
     * 
     * @param err The error message to be printed if needed.
     */
    private void dispose(String err) {
        timer.stop();
        
        int errCode = 0;
        if(err != null) {
            errCode = 1;
            System.err.println(err);
        }
        
        window.dispose();
        System.out.println("Exited successfully.");
        System.exit(errCode);
    }
    
    public static void main(String[] args) {

        // Sandboxing
        Policy.setPolicy(new SandboxSecurityPolicy());
        System.setSecurityManager(new SecurityManager());

        // Start Game Frame
        new GameFrame();
    }

}
