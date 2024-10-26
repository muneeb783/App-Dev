package com.example.wandersync.Model;

public class Note {
    private String content;

    public Note(String content) {
        this.content = content;
    }

    public Note() { }

    public String getContent() {
        return content;
    }
    public void setContent(String str) {
        this.content = str;
    }
}
