package com.rest.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.RestAssured;

import java.io.File;
import java.io.IOException;

public class Config {

    public static String userName;
    public static String password;
    public static String pingUrl;
    public static String authorizeUrl;

    static class ConfModel {
        public String serviceUrl;    // need 'public' for parsing from .yaml in init()
        public String user;
        public String password;
        public String database;
        public Endpoints endpoints;

        public static class Endpoints {
            public String ping;
            public String authorize;
            public String saveData;
        }
    }

    public static void init(String configFile) throws IOException {
        ConfModel config = new ObjectMapper(new YAMLFactory()).readValue(new File(configFile), ConfModel.class);
        RestAssured.baseURI = config.serviceUrl;
        pingUrl = config.endpoints.ping;
        authorizeUrl = config.endpoints.authorize;
        userName = config.user;
        password = config.password;
    }
}

