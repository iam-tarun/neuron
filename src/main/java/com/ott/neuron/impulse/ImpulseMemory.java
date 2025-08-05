package com.ott.neuron.impulse;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ImpulseMemory {
    private final List<Impulse> history;

    public ImpulseMemory() {
        this.history = Collections.synchronizedList(new ArrayList<>());
    }

    public void receive(Impulse impulse) {
        history.add(impulse);
    }

    public List<Impulse> peerMessages(String peer) {
        return history.stream().filter(imp -> imp.from().equals(peer) || imp.to().equals(peer)).toList();
    }
}
