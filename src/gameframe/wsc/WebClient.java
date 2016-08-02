/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameframe.wsc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * WebClient
 *
 * Simple web client to fetch content from endpoint
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class WebClient {

    public static Response get(String url) throws Exception {
        Response response = null;

        URL u = new URL(url);

        if ("http".equals(u.getProtocol())) {
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.connect();

            String contentType = conn.getContentType();
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }

            String content = sb.toString();

            response = new Response(content, contentType, responseCode,
                    responseMessage);

        } else {
            throw new IllegalArgumentException(
                    "This is only for http requests");
        }

        return response;
    }

}
