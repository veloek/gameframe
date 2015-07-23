/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe;

import gameframe.api.GFGame;
import gameframe.gui.MainMenu;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.net.URL;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author vegard
 */
public class GameFrame extends JFrame implements ActionListener {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static String VERSION = "v0.0.1";

    private static GFGame game;
    private static MainMenu menu;

    public GameFrame() throws Exception {
        super("GameFrame");

        Dimension size = new Dimension(WIDTH, HEIGHT);

        menu = new MainMenu(size);
        startGame(menu);

        // TODO: Use joystick and button input instead of keyboard
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_ESCAPE) {
                    startGame(menu);
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

        JPanel panel = new GameView();
        panel.setPreferredSize(size);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        new Timer(1000/60, this).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class GameView extends JPanel {

        private long now, time;

        public GameView() {
            time = System.nanoTime();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (game != null) game.update(g);
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

    public static GFGame loadGame(URL url) {
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

    public static void main(String[] args) throws Exception {
        new GameFrame();
    }

}
