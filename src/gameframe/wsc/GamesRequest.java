/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.wsc;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * GamesRequest
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class GamesRequest {

    private static final String ENDPOINT = "http://vtek.no/gfapi/games/";

    public static ListOfGamesResponse getListOfGames() throws Exception {
        Response r = WebClient.get(ENDPOINT);

        if (r.getResponseCode() / 100 == 2 &&
                r.getContentType().contains("application/json")) {

            String response = r.getContent();
            JSONObject jo = new JSONObject(response);

            String info = jo.getString("info");
            String version = jo.getString("version");
            JSONArray jsonGames = jo.getJSONArray("games");

            List<Game> games = new ArrayList<>();

            JSONObject game;
            String gameName, gameVersion, gameAuthor, gameUrl;
            for (int i=0; i<jsonGames.length(); i++) {
                game = jsonGames.getJSONObject(i);

                gameName = game.getString("name");
                gameVersion = game.getString("version");
                gameAuthor = game.getString("author");
                gameUrl = game.getString("url");

                games.add(new Game(gameName, gameVersion, gameAuthor, gameUrl));
            }

            return new ListOfGamesResponse(info, version,
                    games.toArray(new Game[jsonGames.length()]));

        } else {
            throw new Exception("Invalid response");
        }
    }
}
