package com.ott.neuron.cortex.screens;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.synapse.Axon;
import com.ott.neuron.synapse.SynapseHandler;
import com.ott.neuron.synapse.SynapseManager;
import com.ott.neuron.user.UserService;

import java.util.List;

public class NodeSelectionScreen extends BaseScreen {

    private final String username;
    private final String source;
    private final Axon axon;
    private List<String> allNeurons;
    private final SynapseManager synapseManager;
    private final ImpulseMemory memory;
    private final UserService userService;

    public NodeSelectionScreen(String username, String source, Axon axon, SynapseManager manager, ImpulseMemory memory, UserService userService) {
        this.username = username;
        this.source = source;
        this.axon = axon;
        this.synapseManager = manager;
        this.allNeurons = synapseManager.getPeers();
        this.memory = memory;
        this.userService = userService;
    }

    @Override
    public String getTitle() {
        return "Neuron Selection";
    }

    @Override
    public Component buildContent(WindowBasedTextGUI gui) {
        Panel content = new Panel(new GridLayout(2));
        TextBox searchBox = new TextBox(new TerminalSize(30, 1));
        RadioBoxList<String> filteredList = new RadioBoxList<>();
        filteredList.setPreferredSize(new TerminalSize(30, 3));

        Button continueButton = new Button("Continue", () -> {
            try {
                String selectedNeuron = filteredList.getCheckedItem();
                if(selectedNeuron == null || selectedNeuron.isBlank()) {
                    MessageDialog.showMessageDialog(gui, "Error", "Select a neuron to chat with!");
                    return;
                }

                SynapseHandler handler = synapseManager.getPeer(selectedNeuron);
                if (handler == null || !handler.isRunning()) {
                    MessageDialog.showMessageDialog(gui, "Error", "Neuron is not available");
                    return;
                }

                Window nextWindow = new MessageScreen(username, selectedNeuron, source, axon, memory, userService).build(gui);
                gui.addWindowAndWait(nextWindow);
            } catch (Exception e) {
                System.out.println("[UI] connect button crashed: " + e.getMessage());
            }
        });

        Button cancelButton = new Button("Cancel", () -> {
            gui.getActiveWindow().close();
        });



        allNeurons.forEach(filteredList::addItem);

        searchBox.setTextChangeListener((newText, changedByUser) -> {
            filteredList.clearItems();
            allNeurons.stream()
                    .filter(item -> item.toLowerCase().contains(newText.toLowerCase()))
                    .forEach(filteredList::addItem);
        });

        content.addComponent(new Label("Welcome, " + this.username + "!").setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1
        )));

        content.addComponent(new EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));

        content.addComponent(new Label("search by name or IP for a neuron to chat with:").setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1
        )));

        content.addComponent(new EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));

        content.addComponent(searchBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1
        )).setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.GREEN)));

        content.addComponent(new EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));

        content.addComponent(filteredList.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1
        )));

        content.addComponent(new EmptySpace().setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));

        content.addComponent(continueButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        )));
        content.addComponent(cancelButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        )));

        return content;
    }

}
