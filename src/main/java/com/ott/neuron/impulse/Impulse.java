package com.ott.neuron.impulse;

import java.time.Instant;

public record Impulse(String ID, String from, String to, String content, Instant time, ImpulseType type) {
}
