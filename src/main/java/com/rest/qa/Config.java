package com.rest.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class Config {

    public static String serviceUrl;
    public static String userName;
    public static String password;
    public static String pingUrl;
    public static String authorizeUrl;

    static class ConfModel {
        public String serviceUrl;
        public String user;
        public String password;
        public String database;
    }

    static class EndpointsModel {
        public String ping;
        public String authorize;
    }

    public static void init(String configFile, String endpointsFile) throws IOException {
        ConfModel config = new ObjectMapper(new YAMLFactory()).readValue(new File(configFile), ConfModel.class);
        EndpointsModel endp = new ObjectMapper(new YAMLFactory()).readValue(new File(endpointsFile), EndpointsModel.class);
        serviceUrl = config.serviceUrl;
        pingUrl = serviceUrl + endp.ping;
        authorizeUrl = serviceUrl + endp.authorize;
        userName = config.user;
        password = config.password;
    }
}

