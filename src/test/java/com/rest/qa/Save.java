package com.rest.qa;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.when;

public class Save extends AbstractTest {

    DataBase db = new DataBase("venv/main.db");

    @Test(description = "Saving data without token should return error code 403. No new records in database")
    public void testSaveWOToken() {
        when()
                .post(Config.saveUrl)
                .then()
                .statusCode(403)
                .and()
                .assertThat();
    }
}
