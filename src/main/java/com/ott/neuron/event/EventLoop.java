package com.ott.neuron.event;

import com.ott.neuron.synapse.NeuralCore;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class EventLoop {

    private final Queue<Event> taskQueue = new ConcurrentLinkedQueue<>();
    private final NeuralCore neuralCore;
    private volatile boolean running = false;

    public EventLoop(NeuralCore neuralCore) {
        this.neuralCore = neuralCore;
    }

    public void start() {
        running = true;

        while (running) {
            try{
                neuralCore.loop();

                Event task = taskQueue.poll();
                if (task != null) {
                    task.execute();
                } else {
                    Thread.yield();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void post(Event event) {
        taskQueue.offer(event);
    }

    public void stop() {
        running = false;
    }
}
