package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

class Utils {

    private static final String authURI = "http://oauth.vk.com/authorize";
    private static final String redirectUri = "http://oauth.vk.com/blank.html";
    private static final int appId = 4029909;
    private static final int scope = 8;

    public static LinkedList<AudioFile> parseJson(String jsonRequest) {

        LinkedList<AudioFile> searchAudioResults = new LinkedList<AudioFile>();

        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonRequest);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        JSONObject obj2 = (JSONObject) obj.get("response");
        JSONArray items = (JSONArray) obj2.get("items");

        for (Object item1 : items) {
            JSONObject item = (JSONObject) item1;
            searchAudioResults.add(new AudioFile(Integer.parseInt(item.get("id").toString()), Integer.parseInt(item.get("duration").toString()), item.get("artist").toString(), item.get("title").toString(), item.get("url").toString().substring(0, item.get("url").toString().indexOf("?"))));
        }
        return searchAudioResults;
    }

    public static String authUri() {
        return authURI + "?client_id=" + appId + "&scope=" + scope + "&redirect_uri=" + redirectUri + "&response_type=token";
    }

    public static String searchAudio(String request) {
        try {
            return "https://api.vk.com/method/audio.search?user_id=" + AccessToken.getUserId() + "&v=5.5&access_token=" + AccessToken.getToken() + "&q=" + URLEncoder.encode(request, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String sanitizeFilename(String name) {
        return name.replaceAll("[:\\\\/*?|<>]", "_");
    }
}
