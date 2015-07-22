/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.wsc;

/**
 * Game
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class Game {
    private String name;
    private String version;
    private String author;
    private String url;

    public Game() {
    }

    public Game(String name, String version, String author, String url) {
        this.name = name;
        this.version = version;
        this.author = author;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

}
