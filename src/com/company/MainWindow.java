package com.company;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class MainWindow extends JFrame {

    private JFXPanel jfxPanel;
    private WebEngine engine;
    private Container contentPane;
    private JTextField searchTextField;
    private VkApiHttpClient vkApiHttpClient;
    private GridBagConstraints c;

    public MainWindow() throws HeadlessException {
        super("VK Music Search");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 3, dim.height / 3);
        this.contentPane = this.getContentPane();
        this.setMinimumSize(new Dimension(810, 610));
        setLook();
        this.setLayout(new FlowLayout());

        boolean isAccessTokenLoadedFromFile = Configurator.loadAccessTokenFromFile();
        if (!isAccessTokenLoadedFromFile) {
            showLoginUI();
        } else {
            showSearchUI();
        }
    }

    public void showLoginUI() {

        jfxPanel = new JFXPanel();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final WebView webviewMain = new WebView();
                engine = webviewMain.getEngine();
                engine.load(Utils.authUri());
                engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state, Worker.State state2) {

                        if (state2 == Worker.State.SUCCEEDED) {
                            String s = engine.getLocation();
                            if (!s.startsWith("http://oauth.vk.com")) {
                                engine.load(Utils.authUri());
                            }
                            if (s.startsWith("http://oauth.vk.com/blank.html#access_token=")) {
                                AccessToken.setToken(s.substring(44, s.indexOf("&", 0)));
                                AccessToken.setExpiresIn(Integer.parseInt(s.substring(s.indexOf("expires_in=") + "expires_in=".length(), s.indexOf("&", s.indexOf("expires_in=")))));
                                AccessToken.setUserId(Integer.parseInt(s.substring(s.indexOf("user_id=") + "user_id=".length())));
                                Configurator.saveAccessTokenToFile();
                                System.out.println(AccessToken.info());
                                removeLoginUI();
                                showSearchUI();
                            }
                        }
                    }
                });

                jfxPanel.setScene(new Scene(webviewMain));
                pack();
            }
        });

        contentPane.add(jfxPanel);
    }

    private void removeLoginUI() {
        contentPane.remove(jfxPanel);

    }

    private void showSearchUI() {

        vkApiHttpClient = new VkApiHttpClient();

        GridBagLayout gb = new GridBagLayout();
        c = new GridBagConstraints();
        c.insets = new Insets(1, 1, 1, 1);
        c.fill = GridBagConstraints.HORIZONTAL;
        gb.setConstraints(contentPane, c);
        this.setLayout(gb);

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 9;
        c.anchor = GridBagConstraints.NORTH;
        searchTextField = new JTextField(10);
        searchTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cleanupSearchResults();
                    showSearchResults(Utils.parseJson(vkApiHttpClient.getAudioJson(searchTextField.getText())));
                }
            }
        });
        searchTextField.setHorizontalAlignment(MAXIMIZED_HORIZ);
        contentPane.add(searchTextField, c);

        c.gridx = 1;
        c.weightx = 1;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanupSearchResults();
                showSearchResults(Utils.parseJson(vkApiHttpClient.getAudioJson(searchTextField.getText())));
            }
        });
        contentPane.add(searchButton, c);
        repaint();
        pack();

    }

    private void setLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Can't set look and feel.");
        }
    }

    private void cleanupSearchResults() {
        Component[] components = contentPane.getComponents();
        for (Component component : components) {
            if (component instanceof JButtonDownload) {
                remove(component);
            }
        }
        c.gridy = 0;
        repaint();
        pack();
    }

    private void showSearchResults(LinkedList<AudioFile> audioFiles) {

        JButtonDownload button;

        for (AudioFile audioFile : audioFiles) {
            c.gridx = 0;
            c.gridy = c.gridy + 1;
            c.weightx = 11;
            button = new JButtonDownload(Utils.sanitizeFilename(audioFile.getArtist() + " - " + audioFile.getTitle()), audioFile.getLink());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Downloader.downloadFile(e.getActionCommand() + ".mp3", ((JButtonDownload) e.getSource()).getUrl(), (JButtonDownload) e.getSource());
                }
            });
            button.setHorizontalAlignment(SwingConstants.LEFT);
            contentPane.add(button, c);
            repaint();
            pack();
        }

    }


}
