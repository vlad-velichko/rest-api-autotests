package com.rest.qa;

import io.restassured.RestAssured;
import org.testng.ITest;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public abstract class AbstractTest implements ITest {

    private String testName;
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

    @Override
    public String getTestName() {
        return testName;
    }

    @BeforeMethod
    public void BeforeMethod(Method method, Object[] testData) {
        testName = method.getAnnotation(Test.class).testName();
    }
}
