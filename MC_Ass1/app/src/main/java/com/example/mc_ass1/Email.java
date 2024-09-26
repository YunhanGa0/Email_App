package com.example.mc_ass1;

public class Email {
    private int avatar;  // Resource ID for avatar image
    private String sender;
    private String subjectPreview;
    private String time;

    public Email(int avatar, String sender, String subjectPreview, String time) {
        this.avatar = avatar;
        this.sender = sender;
        this.subjectPreview = subjectPreview;
        this.time = time;
    }

    public int getAvatar() {
        return avatar;
    }

    public String getSender() {
        return sender;
    }

    public String getSubjectPreview() {
        return subjectPreview;
    }

    public String getTime() {
        return time;
    }
}
