package com.company;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

    private static final String defaultFolder = "E:\\temp\\music\\";
    private static File targetFile;
    private static String successMessage = "Download completed!";
    private static String failureMessage = "Download failed!";

    public static void downloadFile(final String fileName, final String link, final JButtonDownload jButtonDownload) {

        jButtonDownload.setEnabled(false);

        final String buttonText = jButtonDownload.getText();
        targetFile = new File(defaultFolder + fileName);

        try {
            final URL url = new URL(link);
            getFileSize(targetFile, url, jButtonDownload);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        org.apache.commons.io.FileUtils.copyURLToFile(url, targetFile);
                        jButtonDownload.setText(buttonText + " " + successMessage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }).start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            jButtonDownload.setText(buttonText + " " + failureMessage);
        }
    }

    public static void getFileSize(final File file, final URL url, final JButtonDownload jButtonDownload) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int fileSize = 0;
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("HEAD");
                    conn.getInputStream();
                    fileSize = conn.getContentLength();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    conn.disconnect();
                }

                String buttonText = jButtonDownload.getText();

                while (!jButtonDownload.getText().contains(successMessage) && !jButtonDownload.getText().contains(failureMessage)) {
                    if (file.exists()) {
                        jButtonDownload.setText(buttonText + " " + file.length() / 1024 + "/" + fileSize / 1024 + " Kb");
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
        }).start();

    }

}
