package com.rest.qa;

public class Upload {
    private String user;
    private String payload;

    Upload(String user, String payload) {
        this.user = user;
        this.payload = payload;
    }

    public String getUser() {
        return user;
    }

    public String getPayload() {
        return payload;
    }
}
