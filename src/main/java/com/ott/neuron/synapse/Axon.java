package com.ott.neuron.synapse;

import com.ott.neuron.impulse.Impulse;
import com.ott.neuron.impulse.ImpulseUtil;
import org.springframework.stereotype.Component;

import java.nio.channels.SocketChannel;

@Component
public class Axon {

    private final NeuronEngine engine;
    private final SynapseManager synapseManager;

    public Axon(NeuronEngine engine, SynapseManager manager) {
        this.engine = engine;
        this.synapseManager = manager;
    }

    public void send(String peerID, Impulse impulse) throws Exception {
        SocketChannel channel = engine.getChannel(peerID);
        String msg = ImpulseUtil.serialize(impulse);
        engine.enqueueMsg(msg, channel);
    }

    public void connect(String remoteIP, int remotePort) throws Exception {
        this.synapseManager.connect(remoteIP, remotePort);
    }
}
