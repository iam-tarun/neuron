package com.ott.neuron.impulse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImpulseUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Impulse deSerialize(String msg) throws JsonProcessingException {
        return mapper.readValue(msg, Impulse.class);
    }

    public static String serialize(Impulse imp) throws JsonProcessingException {
        return mapper.writeValueAsString(imp) + "\n";
    }
}
