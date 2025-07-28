package com.ott.neuron.synapse;

import com.ott.neuron.impulse.Impulse;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.utils.ErrorLogger;
import org.springframework.stereotype.Component;

@Component
public class Axon {
    private final SynapseManager manager;
    private SynapseHandler handler;
    private ImpulseMemory memory;

    public Axon(SynapseManager manager, ImpulseMemory memory) {
        this.manager = manager;
        this.memory = memory;
    }

    public void sendImpulse(String peerID, Impulse imp) {
        try {
            handler = manager.getPeer(peerID);
            if (handler != null && handler.isRunning()) {
                handler.send(imp);
                System.out.println("Successfully sent impulse");
            } else {
                System.out.println("Peer not connected or unavailable: " + peerID);
            }
        } catch (Exception e) {
            ErrorLogger.log("Axon->SendImpulse", "error sending message", e);
        }
    }
}
