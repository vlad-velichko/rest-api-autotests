package com.rest.qa;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;

public abstract class AbstractTest {

    @BeforeSuite
    public void beforeSuite() throws IOException {
        Config.init("config.yml");
        RestAssured.get();                                              // Check connection.
    }
}
