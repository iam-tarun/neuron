package com.ott.neuron.synapse;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SynapseManager {

    Map<String, SynapseHandler> peerHandlers = new ConcurrentHashMap<>();

    public void registerPeer(String id, SynapseHandler handler) {
        this.peerHandlers.put(id, handler);
        System.out.println("Registered peer: " + id);
    }

    public SynapseHandler getPeer(String id) {
        return this.peerHandlers.getOrDefault(id, null);
    }

    public void removePeer(String id) {
        SynapseHandler handler = peerHandlers.remove(id);
        if (handler != null) {
            handler.close();
            System.out.println("Removed and closed peer: " + id);
        }
    }

    public List<String> getPeers() {
        return new ArrayList<>(peerHandlers.keySet());
    }

    @PreDestroy
    public void clearPeers() {
        for (Map.Entry<String, SynapseHandler> entry: peerHandlers.entrySet()) {
            entry.getValue().close();
        }
        peerHandlers.clear();
        System.out.println("All peers disconnected.");
    }
}
