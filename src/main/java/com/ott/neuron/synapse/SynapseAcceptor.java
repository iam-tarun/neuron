package com.ott.neuron.synapse;

import com.ott.neuron.impulse.Impulse;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.impulse.ImpulseType;
import com.ott.neuron.user.UserService;
import com.ott.neuron.utils.ErrorLogger;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.UUID;

@Component
public class SynapseAcceptor {
    private final ServerSocket serverSocket;
    private final ImpulseMemory memory;
    private final SynapseManager manager;
    private final UserService userService;

    public SynapseAcceptor(SynapseConfig synapseConfig, ImpulseMemory memory, SynapseManager manager, UserService userService) throws IOException {
        this.serverSocket = new ServerSocket(synapseConfig.getNeuronPort());
        this.memory = memory;
        this.manager = manager;
        this.userService = userService;
    }

    public void start() {
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket socket = serverSocket.accept();
                    String peerID = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                    SynapseHandler handler = new SynapseHandler(socket);
                    System.out.println("SynapseAcceptor: accepted connection from " + socket.getRemoteSocketAddress() + " with peerID: " + peerID);

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
                            ErrorLogger.log("SynapseAcceptor → PeerThread", "Connection lost: " + peerID, e);
//                               manager.removePeer(peerID);
                        }
                    }, "Synapse-PeerThread-" + peerID).start();

                } catch (IOException e) {
                    ErrorLogger.log("SynapseAcceptor", "Error accepting socket connection", e);
                }
            }
        }, "Synapse-Acceptor-MainThread").start();
    }

    @PreDestroy
    public void close() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
