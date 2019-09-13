package com.rest.qa;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.when;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

public class Save extends AbstractTest {

    @BeforeClass
    public void authorize() {
        SaveRequest.setUser(user);
        SaveRequest.setPassword(password);
        SaveRequest.setAuthPath(authPath);
        SaveRequest.setDefaultRetryCount(retryCount);
    }

    @Test(description = "Saving data without token should return error code 403. No new records in database")
    public void testSaveWithoutToken() {
        when().post().then().statusCode(403);
        //todo: check No new records in database
    }

    @Test(description = "Saving data with outdated token should return error code 403. No new records in database")
    public void testSaveWithOutdatedToken() throws InterruptedException {
        new SaveRequest().authorize();
        System.out.println("Waiting 61 second for token expiration..."); //Todo: need logging
        sleep(61_000);
        Response response = new SaveRequest()
                .payload("any payload")
                .autoAuth(false)
                .post();
        assertThat(response.statusCode()).isEqualTo(403);
        //todo: check No new records in database
    }

    @Test(description = "Saving data with wrong payload should return error code 400. No new records in database")
    public void testSaveWrongPayload() {
        Response response = new SaveRequest()
                .payload("Wrong Raw Payload")
                .post(0);
        assertThat(response.statusCode()).isEqualTo(400);
        //todo: check No new records in database
    }
}
