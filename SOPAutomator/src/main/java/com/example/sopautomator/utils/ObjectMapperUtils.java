package com.example.sopautomator.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String convertObjectToJson(Object obj) {
        String data;
        try {
            data = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
        return data;
    }

}
