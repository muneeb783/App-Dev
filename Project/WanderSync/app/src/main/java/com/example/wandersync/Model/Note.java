package com.example.wandersync.Model;

public class Note {
    private String messageContent;

    public Note(String content) {
        this.messageContent = content;
    }

    public Note() { }

    public String getContent() {
        return messageContent;
    }
    public void setContent(String str) {
        this.messageContent = str;
    }
}
