package com.rest.qa;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class AbstractTest {

    static String serviceUrl;
    static String database;
    static String user;
    static String password;
    static String authPath;
    static int retryCount;

    @BeforeSuite
    @Parameters({"serviceUrl", "database", "user", "password", "authPath", "retryCount"})
    public void beforeSuite(String serviceUrl, String database, String user, String password, String authPath, int retryCount) {
        AbstractTest.serviceUrl = serviceUrl;
        AbstractTest.database = database;
        AbstractTest.user = user;
        AbstractTest.password = password;
        AbstractTest.authPath = authPath;
        AbstractTest.retryCount = retryCount;

        RestAssured.baseURI = serviceUrl;
        RestAssured.get();                                              // Check connection.
    }

    @BeforeClass
    @Parameters({"endpoint"})
    public void beforeClass(@Optional("") String endpoint) {
        RestAssured.basePath = endpoint.isBlank() ? authPath : endpoint;
    }
}
