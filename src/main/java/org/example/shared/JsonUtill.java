package org.example.shared;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonUtill {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Data data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Data fromJson(String json) {
        try {
            return objectMapper.readValue(json, Data.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
