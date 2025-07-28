package com.ott.neuron.cortex;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.ott.neuron.cortex.screens.UserLoginScreen;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.synapse.Axon;
import com.ott.neuron.synapse.SynapseManager;
import com.ott.neuron.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class NeuronUIManager {

    public static TextColor foreground = TextColor.ANSI.CYAN;
    public static TextColor background = TextColor.ANSI.BLACK;

    public final Axon axon;
    public final SynapseManager synapseManager;
    public final ImpulseMemory memory;
    public final UserService userService;

    public NeuronUIManager(Axon axon, SynapseManager manager, ImpulseMemory memory, UserService userService) {
        this.axon = axon;
        this.synapseManager = manager;
        this.memory = memory;
        this.userService = userService;
    }

    public void start(String source) throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            final Window window = new UserLoginScreen(source, axon, synapseManager, memory, userService).build(textGUI);

            textGUI.addWindowAndWait(window);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (screen != null) {
                try {
                    screen.startScreen();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
        }
    }
}
