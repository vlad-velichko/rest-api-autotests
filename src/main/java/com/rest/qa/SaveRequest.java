package com.rest.qa;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class SaveRequest {

    private static String user;
    private static String password;
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

    private String token;
    private String payload;
    private boolean autoAuth = true;
    private boolean check200 = true;
    private boolean isJson = false;

    public SaveRequest payloadText(String payload) {
        this.payload = "payload=" + payload;
        return this;
    }

    public SaveRequest payloadJson(String payload) {
        this.payload = new JSONObject().put("payload", payload).toString();
        isJson = true;
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

    public SaveRequest check200(boolean check200) {
        this.check200 = check200;
        return this;
    }

    public void authorize() {
        token = given().basePath(authPath)
                .param("username", user)
                .param("password", password)
                .post().then().extract().body().jsonPath()
                .get("token");
    }

    private Response internalPost() {
        RequestSpecification request = given().body(payload).header("Authorization", "Bearer" + token);
        if (isJson) request.header("content-type", "application/json");
        return request.post().then().extract().response();
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
        }
        if (response.path("status").equals("ERROR") && retryCount > 0) {
            response = post(retryCount - 1);
        }
        if (check200) response.then().statusCode(200);
        return response;
    }
}
