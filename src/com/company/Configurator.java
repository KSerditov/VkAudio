package com.company;

import java.io.*;
import java.util.Properties;

public class Configurator {

    public static boolean loadAccessTokenFromFile() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("access_token.properties"));
            long expTime = Long.parseLong(properties.getProperty("expiresAt"));
            if(expTime <= System.currentTimeMillis() ){
                return false;
            }
            AccessToken.setToken(properties.getProperty("access_token"));
            AccessToken.setUserId(Integer.parseInt(properties.getProperty("userId")));

            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void saveAccessTokenToFile() {
        Properties properties = new Properties();
        properties.put("access_token", String.valueOf(AccessToken.getToken()));
        properties.put("expiresAt", String.valueOf(AccessToken.getExpiresAt()));
        properties.put("userId", String.valueOf(AccessToken.getUserId()));
        try {
            properties.store(new FileOutputStream("access_token.properties"), "");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
