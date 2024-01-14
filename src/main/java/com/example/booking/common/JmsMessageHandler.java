package com.example.booking.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;

public class JmsMessageHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String convertObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T convertMessageToObject(Message message, Class<T> objectclass) throws Exception {

        if (message instanceof TextMessage) {
            String jsonMessage = ((TextMessage) message).getText();

            return mapper.readValue(jsonMessage, objectclass);
        }
        return null;
    }
}
