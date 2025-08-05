package com.ott.neuron.synapse;

import com.ott.neuron.impulse.Impulse;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.impulse.ImpulseType;
import com.ott.neuron.impulse.ImpulseUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.UUID;

@Component
public class SynapseHandler {

    private final NeuronEngine engine;
    private final ImpulseMemory memory;

    public SynapseHandler(NeuronEngine engine, ImpulseMemory memory) {
        this.engine = engine;
        this.memory = memory;
    }

    public void initHandshakeOrAck(SocketChannel channel, ImpulseType type) throws Exception {
        Impulse handshake = new Impulse(
                UUID.randomUUID().toString(),
                engine.getNeuronID(),
                channel.getRemoteAddress().toString(),
                engine.getNeuronID(),
                Instant.now(),
                type
        );

        String msg = ImpulseUtil.serialize(handshake);
        engine.enqueueMsg(msg, channel);
    }

    public ImpulseType processMessage(SocketChannel channel, String msg) throws Exception {
        Impulse impulse = ImpulseUtil.deSerialize(msg);

        switch(impulse.type()) {
            case JOIN -> {
                initHandshakeOrAck(channel, ImpulseType.ACK);
                engine.registerPeer(impulse.content(), channel);
            }
            case ACK -> {
                System.out.println("Acknowledged connection with" + impulse.content());
                engine.registerPeer(impulse.content(), channel);
            }
            case CHAT -> memory.receive(impulse);
            default -> System.out.println("Unknown Impulse type found! " + impulse.type());
        }
        return impulse.type();
    }
}
