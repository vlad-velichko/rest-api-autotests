package com.rest.qa;

import io.restassured.RestAssured;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class AbstractTest {

    static String serviceUrl;
    static String database;
    static String user;
    static String password;
    static String endpointAuth;

    @BeforeSuite
    @Parameters({"serviceUrl", "database", "user", "password"})
    public void beforeSuite(String serviceUrl, String database, String user, String password) {
        AbstractTest.serviceUrl = serviceUrl;
        AbstractTest.database = database;
        AbstractTest.user = user;
        AbstractTest.password = password;
        endpointAuth = Reporter.getCurrentTestResult().getTestContext().getSuite().getXmlSuite().getTests()
                .stream().filter(t -> t.getName().equals("Authorization")).findFirst().orElseThrow().getParameter("endpoint");

        RestAssured.baseURI = serviceUrl;
        RestAssured.get();                                              // Check connection.
    }

    @BeforeClass
    @Parameters({"endpoint"})
    public void beforeClass(@Optional("") String endpoint) {
        RestAssured.basePath = endpoint;
    }
}
