package com.company;

import javax.swing.*;

/**
 * User: kserditov
 * Date: 12/5/13
 * Time: 8:31 PM
 */
public class JButtonDownload extends JButton {

    private String url;

    public JButtonDownload(String text, String url) {
        super(text);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
