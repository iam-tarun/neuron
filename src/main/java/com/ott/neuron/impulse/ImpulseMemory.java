package com.ott.neuron.impulse;

import com.ott.neuron.synapse.SynapseConnector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class ImpulseMemory implements ImpulseReceiver{

    private final List<Impulse> history = new CopyOnWriteArrayList<>();

    public void receive(Impulse impulse) {
        history.add(impulse);
    }

    public List<Impulse> getRecent(int limit) {
        int size = history.size();
        if (limit >= size) return new ArrayList<>(history);
        return new ArrayList<>(history.subList(size - limit, size));
    }

    public synchronized List<Impulse> getMessagesForPeer(String peerId) {
        return history.stream()
                .filter(imp -> imp.dest().equals(peerId) || imp.source().equals(peerId))
                .collect(Collectors.toList());
    }
}
