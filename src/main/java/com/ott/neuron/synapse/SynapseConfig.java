package com.ott.neuron.synapse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SynapseConfig {

    private final int neuronPort;
    private final String neuronIP;

    public SynapseConfig(@Value("${NEURON_PORT}") int neuronPort, @Value("${NEURON_IP}") String neuronIP) {
        this.neuronPort = neuronPort;
        this.neuronIP = neuronIP;
    }

    public int getNeuronPort() {
        return neuronPort;
    }

    public String getNeuronIP() {
        return neuronIP;
    }

}
