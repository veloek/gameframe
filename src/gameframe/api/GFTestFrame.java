/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe.api;

import gameframe.Direction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GFTestFrame
 *
 * JFrame application for testing a Game Frame game while developing
 *
 * Just instantiate this class with an instance of your game to get a
 * JFrame running. Optional debug information.
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class GFTestFrame extends JFrame implements ActionListener {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private GFGame game;
    private int direction = -1;
    private boolean debug;

    public GFTestFrame(GFGame game, boolean debug) {
        super("GameFrame TestFrame");

        this.game = game;
        this.debug = debug;

        Dimension size = new Dimension(WIDTH, HEIGHT);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_ESCAPE) {
                    System.exit(0); // TODO: Cleaner exit
                } else if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN ||
                        code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
                    setDirection(code);
                } else if (code == KeyEvent.VK_ENTER) {
                    game.onAction();
                } else if (code == KeyEvent.VK_SPACE) {
                    game.onAlternate();
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
        private int frameCount, fps;

        public GameView() {
            time = System.nanoTime();
            frameCount = fps = 0;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            game.update(g);

            if (debug) {
                now = System.nanoTime();
                frameCount++;
                if (now-time > 1000000000) {
                    fps = frameCount;
                    frameCount = 0;
                    time = now;
                }
                drawDebug(g);
            }
        }

        private void drawDebug(Graphics g) {
            int fontSize = 8;
            int padding = 1;

            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
            g.setColor(Color.LIGHT_GRAY);

            String debugStr = "Control: arrow keys, " +
                    "Action: ENTER, Alternate: SPACE, FPS: ";
            g.drawString(debugStr + fps, padding, fontSize+padding);
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
                default:
                    game.setDirection(null);
            }
        }
    }
}
