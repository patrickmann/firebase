package com.example.patrick.nanochat;

class ChatMessage {
    private String name;
    private String message;

    // Empty constructor is necessary for Firebase's deserializer
    public ChatMessage() {
    }
    public ChatMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }
    public String getName () {
        return name;
    }
    public String getMessage () {
        return message;
    }
}