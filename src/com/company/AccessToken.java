package com.company;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AccessToken {

    private static String token;
    private static int expiresIn; //expiration duration in seconds
    private static int userId;
    private static long expiresAt; //expiration time in milliseconds
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AccessToken.token = token;
    }

    public static int getExpiresIn() {
        return expiresIn;
    }

    public static void setExpiresIn(int expiresIn) {
        AccessToken.expiresIn = expiresIn;
        AccessToken.expiresAt = System.currentTimeMillis() + AccessToken.expiresIn * 1000;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        AccessToken.userId = userId;
    }

    public static long getExpiresAt() {
        return expiresAt;
    }

    public static String info() {
        return "Access Token: " + getToken() + " User id: " + getUserId() + " Expires: " + dateFormat.format(expiresAt);
    }
}
