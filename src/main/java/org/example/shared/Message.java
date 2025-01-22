package org.example.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private String message;
    private User sender;
    private User receiver;

    public Message() {
    }

    @JsonCreator
    public Message(
            @JsonProperty("message") String message,
            @JsonProperty("sender") User sender,
            @JsonProperty("receiver") User receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }
}
