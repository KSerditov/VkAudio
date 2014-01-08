package com.company;

public class AudioFile {
    private int id;
    private int duration;
    private String artist;
    private String title;
    private String link;

    public AudioFile(int id, int duration, String artist, String title, String link) {
        this.id = id;
        this.duration = duration;
        this.artist = artist.trim();
        this.title = title.trim();
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return getArtist() + " " + getTitle() + " " + getLink();
    }
}
