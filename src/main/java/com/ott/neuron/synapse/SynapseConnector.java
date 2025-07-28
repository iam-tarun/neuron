package com.ott.neuron.synapse;

import com.ott.neuron.cortex.ConfigLoader;
import com.ott.neuron.impulse.Impulse;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.impulse.ImpulseType;
import com.ott.neuron.user.UserService;
import com.ott.neuron.utils.ErrorLogger;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.UUID;

@Component
public class SynapseConnector {
    private Socket socket;
    private final ImpulseMemory memory;
    private final SynapseManager manager;
    private final UserService userService;

    public SynapseConnector(ImpulseMemory memory, SynapseManager manager, UserService userService) {
        this.memory = memory;
        this.manager = manager;
        this.userService = userService;
    }

    public void connect(int axonPort, String axonIP) {
        String peerId = "";
        try {
            this.socket = new Socket(axonIP, axonPort);
            peerId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
            SynapseHandler handler = new SynapseHandler(socket);

            Impulse registerImpulse = new Impulse(UUID.randomUUID(), socket.getRemoteSocketAddress().toString(), Instant.now(), ImpulseType.REGISTER, userService.getNeuronId(), socket.getLocalAddress() + ":" + socket.getLocalPort());
            handler.send(registerImpulse);
            new Thread(() -> {
                try {
                    while (true) {
                        Impulse imp = handler.readBlocking();
                        if (imp.type() == ImpulseType.REGISTER) {
                            manager.registerPeer(imp.content(), handler);
                        } else {
                            memory.receive(imp);
                        }
                    }
                } catch (IOException e) {
//                    manager.removePeer(peerId);
                    ErrorLogger.log("SynapseConnector", "Failed to read from peer", e);
                }
            }, "SynapseConnector->Peer["+peerId+"]").start();

            System.out.println("[SynapseConnector] Connected to peer: " + peerId);

        } catch (IOException e) {
            ErrorLogger.log("SynapseConnector", "Unable to connect", e);
        }
    }
}
