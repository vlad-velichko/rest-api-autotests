package com.rest.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.fail;

public class Tests {

    private static ConfModel config;

    @BeforeSuite
    public static void beforeSuite() throws IOException {
        config = new ObjectMapper(new YAMLFactory()).readValue(new File("config/config.yml"), ConfModel.class);
    }

    @Test(description = "Simple test for checking connection. If this test fails, other tests will be skipped")
    public void testPing() {
        try {
            given().when().get(config.getServiceUrl() + "ping/").then().statusCode(200);
        } catch (Exception e) {
            if (e instanceof ConnectException)
                fail("No connection to server " + config.getServiceUrl());
            throw e;
        }
    }

//    @Test(description = "Authorization with wrong login and wrong password should be failed")
//    public void testBadLoginAndBadPassword() {
//        given().when().post(config.getServiceUrl() + "authorize/");
//    }
}
