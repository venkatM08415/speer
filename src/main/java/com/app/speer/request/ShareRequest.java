package com.app.speer.request;

public class ShareRequest {

    private String username;

    public ShareRequest() {}

    public ShareRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
