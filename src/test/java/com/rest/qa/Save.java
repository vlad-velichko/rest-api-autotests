package com.rest.qa;

import io.restassured.response.Response;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.logging.Logger;

import static io.restassured.RestAssured.when;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class Save extends AbstractTest {

    private static DataBase db;

    @BeforeClass
    public void authorize() {
        SaveRequest.setUser(user);
        SaveRequest.setPassword(password);
        SaveRequest.setAuthPath(authPath);
        SaveRequest.setDefaultRetryCount(retryCount);
        db = new DataBase(database);
    }

    @AfterClass
    public void cleanDB() throws Exception {
        db.cleanUploads();
    }

    @Test(description = "Saving data without token should return error code 403. No new records in database")
    public void testSaveWithoutToken() {
        long time = db.getDbLastModified();

        when().post().then().statusCode(403);

        assertEquals(db.getDbLastModified(), time, "Timestamp DB was modified last time");
    }

    @Test(description = "Saving data with outdated token should return error code 403. No new records in database")
    public void testSaveWithOutdatedToken() throws Exception {
        long time = db.getDbLastModified();
        SaveRequest saveRequest = new SaveRequest().payloadText("any payload");
        saveRequest.authorize();

        Logger.getLogger(this.getClass().getName()).info("<logger> Waiting 61 second for token expiration...");
        SECONDS.sleep(61);

        saveRequest
                .autoAuth(false)
                .check200(false)
                .post().then()
                .statusCode(403);

        assertEquals(db.getDbLastModified(), time, "Timestamp DB was modified last time");
    }

    @Test(description = "Saving data with wrong payload should return error code 400. No new records in database")
    public void testSaveWrongPayload() {
        long time = db.getDbLastModified();

        new SaveRequest()
                .rawPayload("Wrong Raw Payload")
                .check200(false)
                .post(0).then()
                .statusCode(400);

        assertEquals(db.getDbLastModified(), time, "Timestamp DB was modified last time");
    }

    @Test(description = "Saving data with DB-side error should return error message in response. No new records in database")
    public void testSaveDbError() {
        for (int i = 0; i < 100; i++) {
            long time = db.getDbLastModified();
            Response response = new SaveRequest().payloadText("Try#" + i).post(0);

            if (response.jsonPath().get("error") != null) {
                SoftAssert soft = new SoftAssert();
                soft.assertEquals(response.path("error"), "I dont like this payload", "Message in a response");
                soft.assertEquals(response.path("status"), "ERROR", "Status in a response");
                soft.assertEquals(db.getDbLastModified(), time, "Timestamp DB was modified last time. (This is an engineered bug in a server)");
                soft.assertAll();
                return;
            }
        }
        throw new SkipException("Test result skipped. No errors on DB side during 100 tries");
    }

    @Test(description = "Saving payload in text format. Response should contain ID = ID for new MD5 record in DB")
    public void testSaveAsText() throws Exception {
        Response response = new SaveRequest().payloadText("text").post();

        Integer id = response.path("id");
        String md5 = new BigInteger(1, MessageDigest.getInstance("MD5").digest("text".getBytes())).toString(16);

        assertNotNull(id, "ID in a response");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(db.getUserId(id), user, "Username in database");
        soft.assertEquals(db.getPayloadMD5(id), md5, "Payload MD5 in database");
        soft.assertAll();
    }

    @Test(description = "Saving payload in JSON format. Response should contain ID = ID for new MD5 record in DB")
    public void testSaveAsJson() throws Exception {
        Response response = new SaveRequest().payloadJson("Json").post();

        Integer id = response.path("id");
        String md5 = new BigInteger(1, MessageDigest.getInstance("MD5").digest("Json".getBytes())).toString(16);

        assertNotNull(id, "ID in a response");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(db.getUserId(id), user, "Username in database");
        soft.assertEquals(db.getPayloadMD5(id), md5, "Payload MD5 in database");
        soft.assertAll();
    }
}
