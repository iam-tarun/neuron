package com.ott.neuron.synapse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class NeuronEngine {
    private final String neuronID;
    private final Map<String, SocketChannel> peers;
    private final Map<SocketChannel, Queue<String>> messageMap;
    private final Map<SocketChannel, SelectionKey> keyMap;
    private final int neuronPort;
    private final String neuronIP;
    private final int BUFFER_SIZE;

    public NeuronEngine(@Value("${NEURON_PORT}") String port, @Value("${NEURON_IP}") String ip, @Value("${BUFFER_SIZE}") String buffer_size) {
        this.neuronID = UUID.randomUUID().toString();
        this.peers = new ConcurrentHashMap<>();
        this.messageMap = new ConcurrentHashMap<>();
        this.keyMap = new ConcurrentHashMap<>();
        this.neuronPort = Integer.parseInt(port);
        this.neuronIP = ip;
        this.BUFFER_SIZE = Integer.parseInt(buffer_size);
    }

    public String getNeuronID() {
        return this.neuronID;
    }

    public int getNeuronPort() {
        return this.neuronPort;
    }

    public String getNeuronIP() {
        return this.neuronIP;
    }

    public int getBufferSize() {
        return this.BUFFER_SIZE;
    }

    public void registerPeer(String peerID, SocketChannel channel) {
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        peers.put(peerID, channel);
        messageMap.put(channel, queue);
    }

    public void registerKey(SocketChannel channel, SelectionKey key) {
        keyMap.put(channel, key);
    }

    public boolean hasPeer(String peerID) {
        return peers.containsKey(peerID);
    }

    public SocketChannel getChannel(String peerID) {
        return peers.get(peerID);
    }

    public Queue<String> getMessageQueue(SocketChannel channel) {
        return messageMap.get(channel);
    }

    public Map<String, SocketChannel> getAllPeers() {
        return peers;
    }

    public void removePeer(String peerID) {
        SocketChannel channel = peers.remove(peerID);
        messageMap.remove(channel);
    }

    public void enqueueMsg(String msg, SocketChannel channel) {
        Queue<String> queue = messageMap.get(channel);
        if (queue != null) queue.add(msg);

        SelectionKey key = keyMap.get(channel);
        if (key != null && key.isValid()) {
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        }
    }

}
