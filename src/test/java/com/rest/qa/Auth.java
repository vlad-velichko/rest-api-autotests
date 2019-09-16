package com.rest.qa;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.matchesRegex;

public class Auth extends AbstractTest {

    @Test(description = "Authorization without credentials should be failed")
    public void testEmptyCredentials() {
        when().post().then().statusCode(403);
    }

    @Test(description = "Authorization with wrong username and good password should be failed (HTTP code 403)")
    public void testWrongLogin() {
        given()
                .param("password", password)
                .param("username", "wrongUserName")
                .when().post().then().statusCode(403);
    }

    @Test(description = "Authorization with wrong password and good username should be failed (HTTP code 403)")
    public void testWrongPass() {
        given()
                .param("username", user)
                .param("password", "wrongPassword")
                .when().post().then().statusCode(403);
    }

    @Test(description = "Authorization with valid credentials should return code 200 and UUID-style token")
    public void testValidCredentials() {
        given()
                .param("username", user)
                .param("password", password)
                .when().post().then().statusCode(200)
                .and()
                .body("token", describedAs("Valid UUID as a token",
                        matchesRegex("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")));
    }
}
