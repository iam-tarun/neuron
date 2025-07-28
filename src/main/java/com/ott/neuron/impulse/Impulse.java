package com.ott.neuron.impulse;

import java.time.Instant;
import java.util.UUID;

public record Impulse(UUID id, String dest, Instant timestamp, ImpulseType type, String content, String source) {
}
