/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.wsc;

/**
 * ListOfGamesResponse
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class ListOfGamesResponse {
    private String info;
    private String version;
    private Game[] games;

    public ListOfGamesResponse() {
    }

    public ListOfGamesResponse(String info, String version, Game[] games) {
        this.info = info;
        this.version = version;
        this.games = games;
    }

    public String getInfo() {
        return info;
    }

    public String getVersion() {
        return version;
    }

    public Game[] getGames() {
        return games;
    }

}
