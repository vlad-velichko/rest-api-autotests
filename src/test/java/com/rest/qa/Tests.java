package com.rest.qa;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.ConnectException;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.fail;

public class Tests {

    private RequestSpecification scenario = given();        // for beautifying given-when-then

    @BeforeSuite
    public void beforeSuite() throws IOException {
        Config.init("config/config.yml", "config/endpoints.yml");
    }

    @Test(description = "Simple test for checking connection. If this test fails, other tests will be skipped")
    public void testPing() {
        try {
            scenario.when()
                    .get(Config.pingUrl);
            scenario.then()
                    .statusCode(200);
        } catch (Exception e) {                             // compiler considers ConnectException can't be thrown here
            if (e instanceof ConnectException)              // but it's thrown there really
                fail("No connection to server " + Config.serviceUrl);
            throw e;
        }
    }

    @Test(description = "Failed authorization for empty credentials", dependsOnMethods = "testPing")
    public void testEmptyCredentials() {
        scenario.when()
                .post(Config.authorizeUrl);
        scenario.then()
                .statusCode(403);
    }

    @Test(description = "Failed authorization for wrong login", dependsOnMethods = "testPing")
    public void testWrongLogin() {
        scenario.given()
                .param("username", "wrongUserName")
                .param("password", Config.password);
        scenario.when()
                .post(Config.authorizeUrl);
        scenario.then()
                .statusCode(403);
    }
}
