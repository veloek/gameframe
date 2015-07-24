/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe;

import gameframe.api.GFGame;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.net.URL;

/**
 *
 * @author vegard
 */
public class GameFrame implements TimerListener {
    
    private final Window window;
    
    private final Timer timer;
    
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private GFGame game;
    private int direction = -1;

    private boolean takedown = false;
    
    public GameFrame() throws Exception {
        
        window = new Window("GameFrame", WIDTH, HEIGHT);
        
        Response r = WebClient.get("http://vtek.no/listribute/api/app");
        System.out.println(r.getContent());
        
        game = loadGame(new URL("http://dev.vtek.no/GFSnake.jar"));

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

        window.render();
        
        if(takedown || window.isDisposed()) {
            timer.stop();
        }
    }

    private void setDirection(int keyCode) {
        if (keyCode != direction) {
            direction = keyCode;

            switch (keyCode) {
                case KeyEvent.VK_UP:
                    game.setDirection(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    game.setDirection(Direction.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    game.setDirection(Direction.LEFT);
                    break;
               case KeyEvent.VK_RIGHT:
                    game.setDirection(Direction.RIGHT);
                    break;
            }
        }
    }

    private GFGame loadGame(URL url) {
        boolean isGFGame = false;
        Class gameClass = null;
        GFGame instance = null;

        try {
            JarClassLoader jcl = new JarClassLoader(url);
            String className = jcl.getMainClassName();
            gameClass = jcl.loadClass(className);
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
    
    public static void main(String[] args) throws Exception {
        new GameFrame();
    }

}
