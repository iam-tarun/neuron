package com.ott.neuron.synapse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ott.neuron.impulse.Impulse;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.impulse.ImpulseReceiver;
import com.ott.neuron.utils.ErrorLogger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SynapseHandler {

    private final BufferedReader in;
    private final PrintWriter out;
    private Boolean isRunning;
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final Socket socket;

    public SynapseHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.isRunning = true;
    }

    public void send(Impulse impulse) {
        try {
            System.out.println("Sending impulse from " + socket.getLocalPort() + " to " + socket.getRemoteSocketAddress());
            String jsonData = objectMapper.writeValueAsString(impulse);
            out.println(jsonData);
        } catch (IOException e) {
//            this.isRunning = false;
            ErrorLogger.log("SynapseHandler", "Error sending impulse", e);
        }
    }

    public Impulse readBlocking() throws IOException {
        String msg = in.readLine();
        if(msg != null) {
            return objectMapper.readValue(msg, Impulse.class);
        }
        else {
//            this.isRunning = false;
            throw new IOException("Peer closed the connection unexpectedly");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void close() {
        try {
            isRunning = false;
            socket.close();
        } catch (IOException e) {
            ErrorLogger.log("SynapseHandler", "Error Closing socket", e);
        }
    }

}
