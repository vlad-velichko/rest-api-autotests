package com.rest.qa;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SaveRequest {

    private static String user;
    private static String password;
    private static String token;
    private static String authPath;
    private static int defRetryCount;

    public static void setUser(String user) {
        SaveRequest.user = user;
    }

    public static void setPassword(String password) {
        SaveRequest.password = password;
    }

    public static void setAuthPath(String authPath) {
        SaveRequest.authPath = authPath;
    }

    public static void setDefaultRetryCount(int retryCount) {
        SaveRequest.defRetryCount = retryCount;
    }

    private String payload;
    private boolean autoAuth = true;

    public SaveRequest payload(String payload) {
        this.payload = "payload=" + payload;
        return this;
    }

    public SaveRequest rawPayload(String rawPayload) {
        this.payload = rawPayload;
        return this;
    }

    public SaveRequest autoAuth(boolean autoAuth) {
        this.autoAuth = autoAuth;
        return this;
    }

    public void authorize() {
        token = given()
                .basePath(authPath)
                .param("username", user)
                .param("password", password)
                .post()
                .then().extract().body().jsonPath()
                .get("token");
    }

    private Response internalPost() {
        return given()
                .header("Authorization", "Bearer" + token)
                .body(payload)
                .post().then().extract().response();
    }

    public Response post() {
        return post(defRetryCount);
    }

    public Response post(int retryCount) {
        Response response = internalPost();
        if (response.statusCode() == 403) {
            if (!autoAuth) return response;
            authorize();
            response = internalPost();
        } else if (response.statusCode() != 200 && retryCount > 0) {
            response = post(retryCount - 1);
        }
        return response;
    }
}
