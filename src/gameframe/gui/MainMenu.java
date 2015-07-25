/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.gui;

import gameframe.Direction;
import gameframe.GameFrame;
import gameframe.api.GFGame;
import gameframe.wsc.Game;
import gameframe.wsc.GamesRequest;
import gameframe.wsc.ListOfGamesResponse;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.net.URL;

/**
 * MainMenu
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class MainMenu extends GFGame {
    private static final String ARROW = "→";

    private Dimension size;
    private int borderWidth;
    private int margin;
    private Game[] games;
    private int activeIndex;
    private boolean isAlternate = false;
    private int offset = 0;
    private int lastRow = 0;

    public MainMenu(Dimension size) {
        super(size);

        this.size = size;

        borderWidth = size.width/100;
        margin = size.width/50;

        activeIndex = 0;
        games = null;
        try {
            ListOfGamesResponse listOfGames = GamesRequest.getListOfGames();
            Game[] g = listOfGames.getGames();

            // Debug: fill games array with 20 copies of the same ref
            games = new Game[20];
            for (int i=0; i<20; i++) {
                games[i] = new Game("Copy of Snake " + (i+1), "v0.0." + i,
                        g[0].getAuthor(), g[0].getUrl());
            }

        } catch (Exception e) {
            System.err.println("Error while fetching games list: " + e.getMessage());
        }
    }

    @Override
    public void update(float delta, Graphics g) {

        // Draw frame
        int marginTop = drawFrame(g);

        // Print footer info
        int marginBottom = printInfo(g);

        // Draw scrollable game list
        drawGameList(g, marginTop, marginBottom);

    }

    private int drawFrame(Graphics g) {

        // Draw border
        g.setColor(Color.RED);
        g.fillRect(0, 0, size.width, size.height);

        g.setColor(Color.BLACK);
        g.fillRect(borderWidth, borderWidth, size.width-borderWidth*2,
                size.height-borderWidth*2);

        // Print header
        int fontSize = size.width / 10;
        Color fontColor = new Color(0, 175, 0);
        String header = "Game Frame";

        Font oldFont = g.getFont();
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
        FontMetrics fontMetrics = g.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(header);
        g.setColor(fontColor);
        g.drawString(header, size.width/2-stringWidth/2, fontSize+borderWidth+margin);
        g.setFont(oldFont);

        // Return where game list can begin on the y-axis
        return borderWidth + margin + fontSize + margin;
    }

    private int printInfo(Graphics g) {
        String footer = String.format("Game Frame %s", GameFrame.VERSION);

        int fontSize = size.width / 50;

        Font oldFont = g.getFont();
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
        FontMetrics fontMetrics = g.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(footer);

        g.setColor(Color.GRAY);
        g.drawString(footer, size.width/2-stringWidth/2, size.height-borderWidth-fontSize/2);
        g.setFont(oldFont);

        return size.height - borderWidth - fontSize/2 - fontSize - margin;
    }

    private void drawGameList(Graphics g, int startY, int endY) {
        if (games != null) {

            int height = endY - startY;
            int fontSize = size.width / 25;
            int rows = (height-margin*2) / fontSize;
            lastRow = offset + rows;

            g.setColor(Color.YELLOW);
            g.drawRect(margin, startY, size.width-(margin*2), height);

            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
            g.setColor(Color.GRAY);
            FontMetrics fontMetrics = g.getFontMetrics();
            int arrowWidth = fontMetrics.stringWidth(ARROW);

            int stringWidth, x, index;
            String gameName;
            for (int i=0; i<rows; i++) {
                index = i+offset;

                if (index > games.length-1)
                    break;

                if (index == activeIndex && isAlternate)
                    gameName = games[index].getAuthor() +
                            " (" + games[index].getVersion() + ")";
                else
                    gameName = games[index].getName();

                stringWidth = fontMetrics.stringWidth(gameName);

                // Crop long names
                while (stringWidth > size.width-(margin*4)-arrowWidth) {
                    gameName = gameName.substring(0, gameName.length()-1);
                    stringWidth = fontMetrics.stringWidth(gameName);
                }

                x = size.width/2-stringWidth/2;

                if (index == activeIndex) {
                    gameName = ARROW + gameName;
                    x -= arrowWidth;
                }

                g.drawString(gameName, x, startY+margin+fontSize+i*fontSize);
            }
        }
    }

    @Override
    public void onDirection(Direction direction) {
        super.onDirection(direction);

        if (direction == Direction.UP && activeIndex > 0)
            if (activeIndex-- == offset)
                offset -= 5;

        if (direction == Direction.DOWN && activeIndex < games.length-1)
            if (activeIndex++ == lastRow-1)
                offset += 5;

        isAlternate = false;
    }

    @Override
    public void onAction() {
        Game game = games[activeIndex];

        GFGame loadedGame = null;
        try {
            loadedGame = GameFrame.loadGame(new URL(game.getUrl()));
        } catch (Exception e) {
            System.err.println("Couldn't load game: " + e.getMessage());
        }

        if (loadedGame != null) {
            GameFrame.startGame(loadedGame);
        }
    }

    @Override
    public void onAlternate() {
        isAlternate = !isAlternate;
    }

}
