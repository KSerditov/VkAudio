package com.company;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VkApiHttpClient {

    private HttpClient httpClient;
    private HttpGet httpGet;
    private HttpResponse httpResponse;

    public String getAudioJson(String request) {
        String jsonResponse = "";
        httpClient = HttpClientBuilder.create().build();
        httpGet = new HttpGet(Utils.searchAudio(request));
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {

            HttpEntity httpEntity = httpResponse.getEntity();

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(httpEntity.getContent());
                BufferedReader br = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String read = br.readLine();

                while (read != null) {
                    sb.append(read);
                    read = br.readLine();
                }

                jsonResponse = sb.toString();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

        return jsonResponse;
    }

}
