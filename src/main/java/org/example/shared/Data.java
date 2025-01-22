package org.example.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private String request;
    private Message message;

    public Data() {
    }

    @JsonCreator
    public Data(
            @JsonProperty("request") String request,
            @JsonProperty("message") Message message) {
        this.request = request;
        this.message = message;
    }

    public String getRequest() {
        return request;
    }

    public Message getMessage() {
        return message;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
