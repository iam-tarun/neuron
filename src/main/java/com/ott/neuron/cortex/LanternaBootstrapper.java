package com.ott.neuron.cortex;

import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.synapse.Axon;
import com.ott.neuron.synapse.SynapseManager;
import com.ott.neuron.user.User;
import com.ott.neuron.user.UserService;
import com.ott.neuron.utils.ErrorLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

// In this file we are implementing command line runner interface which runs immediately after the spring container and
// dependency injection is done.
// Our bootstrapper will create a new thread for running the UI.
// This will make sure that UI will not block the main spring container.


@Component
public class LanternaBootstrapper implements CommandLineRunner {
    private final String source;
    public final Axon axon;
    private final SynapseManager synapseManager;
    private final ImpulseMemory memory;
    private final UserService userService;

    public LanternaBootstrapper(@Value("${NEURON_PORT}") String sourcePort, @Value("${NEURON_IP}") String sourceIP, Axon axon, SynapseManager manager, ImpulseMemory memory, UserService userService) {
        this.source = sourceIP + ":" + sourcePort;
        this.axon = axon;
        this.synapseManager = manager;
        this.memory = memory;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        try {
            Thread lanternaUIThread = new Thread(() -> {
                try {
                    NeuronUIManager uiManager = new NeuronUIManager(axon, synapseManager, memory, userService);
                    uiManager.start(source);
                } catch (IOException e) {
                    ErrorLogger.log("NeuronUIManager", "Failed to start UI", e);
                }
            }, "Lanterna-UI-Thread");
            lanternaUIThread.setDaemon(false);
            lanternaUIThread.start();
        } catch (Exception e) {
            ErrorLogger.log("Lanterna Bootstrapper", "Exception launching UI thread", e);
        }
    }
}
