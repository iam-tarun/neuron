package com.ott.neuron.util;

import java.nio.channels.SocketChannel;
import java.util.Queue;

public class PeerData {
    private final SocketChannel channel;
    private final Queue<String> messageQueue;

    public PeerData(SocketChannel channel, Queue<String> messageQueue) {
        this.channel = channel;
        this.messageQueue = messageQueue;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public Queue<String> getMessageQueue() {
        return messageQueue;
    }

    public void enqueueMessage(String message) {
        messageQueue.add(message);
    }
}
