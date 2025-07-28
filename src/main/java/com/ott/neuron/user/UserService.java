package com.ott.neuron.user;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserService {
    private User user;
    private static final String NEURON_ID = UUID.randomUUID().toString();

    public void setUsername(String username) {
        this.user = new User(username);
    }

    public String getUsername() {
        return user.username();
    }

    public String getNeuronId() {
        return NEURON_ID;
    }
}
