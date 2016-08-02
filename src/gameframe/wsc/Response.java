/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe.wsc;

/**
 * Response
 *
 * WebClient response object
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class Response {

    private String content;
    private String contentType;
    private int responseCode;
    private String responseMessage;

    public Response() {
    }

    public Response(String content, String contentType, int responseCode,
            String responseMessage) {
        this.content = content;
        this.contentType = contentType;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
