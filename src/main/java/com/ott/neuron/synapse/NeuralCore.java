package com.ott.neuron.synapse;

import com.ott.neuron.impulse.ImpulseType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

@Component
public class NeuralCore implements CommandLineRunner, SynapseManager {

    private final NeuronEngine engine;
    private Selector neuralSelector;
    private ServerSocketChannel serverSocketChannel;
    private final SynapseHandler synapseHandler;

    public NeuralCore(NeuronEngine engine, SynapseHandler synapseHandler) {
        this.engine = engine;
        this.synapseHandler = synapseHandler;
    }

    @Override
    public void run(String... args) throws Exception {

        neuralSelector = Selector.open();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(engine.getNeuronIP(), engine.getNeuronPort()));
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(neuralSelector, SelectionKey.OP_ACCEPT);
    }

    public void loop() {
        try{

            neuralSelector.select();

            Set<SelectionKey> selectedKeys = neuralSelector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                keyIterator.remove();

                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                } else if (key.isWritable()) {
                    handleWrite(key);
                }
            }

        } catch(Exception e) {
            System.out.println("Failed to secure connection");
            e.printStackTrace();
        }
    }

    public void handleAccept(SelectionKey key) throws Exception {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();

        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            System.out.println("Accepted new connection from " + clientChannel.getRemoteAddress());

            ByteBuffer buffer = ByteBuffer.allocate(engine.getBufferSize());
            clientChannel.register(neuralSelector, SelectionKey.OP_READ, buffer);
            this.synapseHandler.initHandshakeOrAck(clientChannel, ImpulseType.JOIN);
        }
    }

    public void handleRead(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        int bytesRead = channel.read(buffer);

        if (bytesRead == -1) {
            System.out.println("Client disconnected: " + channel.getRemoteAddress());
            channel.close();
            key.cancel();
            return;
        }

        if (bytesRead > 0) {
            buffer.flip();

            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String msg = new String(bytes);
            ImpulseType type = synapseHandler.processMessage(channel, msg);
            if (type == ImpulseType.JOIN || type == ImpulseType.ACK) {
                engine.registerKey(channel, key);
            }
            buffer.clear();
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    public void handleWrite(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        Queue<String> queue = engine.getMessageQueue(channel);

        if (queue == null || queue.isEmpty()) {
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            return;
        }

        while (!queue.isEmpty()) {
            String msg = queue.peek();
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());

            channel.write(buffer);

            if (buffer.hasRemaining()) {
                buffer.compact();
                key.attach(buffer);
                return;
            }

            queue.poll();
        }

        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);

    }

    public void connect(String remoteIP, int remotePort) throws Exception {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(remoteIP, remotePort));

        ByteBuffer buffer = ByteBuffer.allocate(engine.getBufferSize());
        SelectionKey key = channel.register(neuralSelector, SelectionKey.OP_READ, buffer);

        engine.registerKey(channel, key);
        System.out.println("Initiated connection to peer at " + remoteIP + ":" + remotePort);
    }

}
