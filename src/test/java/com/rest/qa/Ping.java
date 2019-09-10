package com.rest.qa;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.when;

public class Ping extends AbstractTest {

    @Test(description = "Endpoint /ping/ should return code 200")
    public void testPing() {
        when()
                .get(Config.pingUrl)
                .then()
                .statusCode(200);
    }
}
