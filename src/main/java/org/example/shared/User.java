package org.example.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    String nickName;
    boolean isOnline;

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("nickName") String nickName) {
        this.nickName = nickName;
        this.isOnline = true;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
