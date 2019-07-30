package com.rest.qa;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.matchesRegex;

public class Tests {

    @BeforeSuite
    public void beforeSuite() throws IOException {
        Config.init("config.yml");
        RestAssured.get();                                              // Check connection.
    }

    @Test(description = "Endpoint /ping/ should return code 200")
    public void testPing() {
        when()
                .get(Config.pingUrl)
                .then()
                .statusCode(200);
    }

    @Test(description = "Failed authorization for empty credentials")
    public void testEmptyCredentials() {
        when()
                .post(Config.authorizeUrl)
                .then()
                .statusCode(403);
    }

    @Test(description = "Failed authorization for wrong login")
    public void testWrongLogin() {
        given()
                .param("password", Config.password)
                .param("username", "wrongUserName")
                .when()
                .post(Config.authorizeUrl)
                .then()
                .statusCode(403);
    }

    @Test(description = "Failed authorization for wrong password")
    public void testWrongPass() {
        given()
                .param("username", Config.userName)
                .param("password", "wrongPassword")
                .when()
                .post(Config.authorizeUrl)
                .then()
                .statusCode(403);
    }

    @Test(description = "Authorization with valid credentials")
    public void testValidCredentials() {
        given()
                .param("username", Config.userName)
                .param("password", Config.password)
                .when()
                .post(Config.authorizeUrl)
                .then()
                .statusCode(200)
                .and()
                .body("token", describedAs("Valid UUID as a token",
                        matchesRegex("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")));
    }
}
