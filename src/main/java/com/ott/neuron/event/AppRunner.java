package com.ott.neuron.event;

import com.ott.neuron.synapse.NeuralCore;
import org.springframework.boot.CommandLineRunner;

public class AppRunner implements CommandLineRunner {
    private final NeuralCore neuralCore;
    private final EventLoop loop;

    public AppRunner(NeuralCore neuralCore, EventLoop loop) {
        this.neuralCore = neuralCore;
        this.loop = loop;
    }

    @Override
    public void run(String... args) throws Exception {
        this.neuralCore.run();
        Thread loopThread = new Thread(loop::start);
        loopThread.start();

        loop.post(() -> System.out.println("testing"));
    }
}
