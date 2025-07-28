package com.ott.neuron.cortex;

import com.ott.neuron.synapse.SynapseAcceptor;
import com.ott.neuron.synapse.SynapseConnector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class Neuron implements CommandLineRunner{
    private final SynapseAcceptor acceptor;
    private final SynapseConnector connector;
    private final List<String> initialAxons;

    public Neuron(SynapseAcceptor acceptor,  SynapseConnector connector, ConfigLoader configLoader) {
        this.acceptor = acceptor;
        this.connector = connector;
        this.initialAxons = configLoader.getPeerIDs();
    }

    @Override
    public void run(String... args) {
        try {
            boot();
        } catch (IOException e) {
            System.err.println("Failed to boot neuron: " + e.getMessage());
        }
    }

    public void boot() throws IOException {
        System.out.println("[Neuron] Booting up...");
        acceptor.start();
        for (String axon: initialAxons) {
            System.out.println(axon);
            String[] parts = axon.split(":");
            int port = Integer.parseInt(parts[1]);
            String ip = parts[0];
            connector.connect(port, ip);
        }
        System.out.println("[Neuron] Ready to transmit impulses.");
    }
}
