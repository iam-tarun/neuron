package com.ott.neuron.cortex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ConfigLoader {
    private List<String> peerIDs;

    public ConfigLoader(@Value("${PEER_IDS}") String peerIDs) {
        this.peerIDs = Arrays.asList(peerIDs.split(","));
    }

    public List<String> getPeerIDs() {
        return peerIDs;
    }
}
