package com.ott.neuron.cortex.screens;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.synapse.Axon;
import com.ott.neuron.synapse.SynapseManager;
import com.ott.neuron.user.UserService;

import java.util.List;

public class UserLoginScreen extends BaseScreen {

    private TextBox usernameBox;
    private final String source;
    private final Axon axon;
    private final SynapseManager synapseManager;
    private final ImpulseMemory memory;
    private final UserService userService;

    public UserLoginScreen(String source, Axon axon, SynapseManager manager, ImpulseMemory memory, UserService userService) {
        this.source = source;
        this.axon = axon;
        this.synapseManager = manager;
        this.memory = memory;
        this.userService = userService;
    }

    @Override
    public String getTitle() {
        return "User Login";
    }

    @Override
    public Component buildContent(WindowBasedTextGUI gui) {
        Panel content = new Panel(new GridLayout(2));
        usernameBox = new TextBox(new TerminalSize(30, 1));

        content.addComponent(new Label("Welcome to Neuron!").setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1
        )));
        content.addComponent(new Label("Your private, terminal-based peer-to-peer messenger.").setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1
        )));
        content.addComponent(new EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));
        content.addComponent(new Label("Enter your username:").setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        )));
        content.addComponent(usernameBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        )).setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.GREEN)));

        Button letsChatButton = new Button("Let's Chat", () -> {
            String username = usernameBox.getText();

            if (username == null || username.isBlank()) {
                MessageDialog.showMessageDialog(gui, "Error", "Please enter a username!");
                return;
            }

            Window nextWindow = new NodeSelectionScreen(username, source, axon, synapseManager, memory, userService).build(gui);
            gui.addWindowAndWait(nextWindow);
        });

        content.addComponent(new EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));
        content.addComponent(letsChatButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1
        )));

        return content;
    }
}
