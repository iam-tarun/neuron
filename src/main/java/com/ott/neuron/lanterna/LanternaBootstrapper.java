package com.ott.neuron.lanterna;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

// In this file we are implementing command line runner interface which runs immediately after the spring container and
// dependency injection is done.
// Our bootstrapper will create a new thread for running the UI.
// This will make sure that UI will not block the main spring container.


@Component
public class LanternaBootstrapper implements CommandLineRunner {

    @Override
    public void run(String... args) {
        Thread lanternaUIThread = new Thread(() -> {
            try {
                NeuronUIManager.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        lanternaUIThread.setDaemon(false);
        lanternaUIThread.start();
    }
}
