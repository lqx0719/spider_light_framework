package response;

import java.io.InputStream;

import request.Request;

public class Response {
    private Request request;
    private Body    body;
    private String info = "";
    public Response(Request request, InputStream inputStream) {
        this.request = request;
        this.info = request.getInfo();
        this.body = new Body(inputStream, request.charset());
    }

    public Body body() {
        return body;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
