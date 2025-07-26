package com.ott.neuron.lanterna.screens;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import java.util.List;

public class NodeSelectionScreen extends BaseScreen {

    private final String username;

    public NodeSelectionScreen(String username) {
        this.username = username;
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
            String selectedNeuron = filteredList.getCheckedItem();
            if(selectedNeuron == null || selectedNeuron.isBlank()) {
                MessageDialog.showMessageDialog(gui, "Error", "Select a neuron to chat with!");
                return;
            }

            Window nextWindow = new MessageScreen(username, selectedNeuron).build(gui);
            gui.addWindowAndWait(nextWindow);
        });

        Button cancelButton = new Button("Cancel", () -> {
            gui.getActiveWindow().close();
        });

        List<String> allNeurons = List.of(
                "tarun - 127.0.0.1:9000",
                "alice - 10.0.0.5:8000",
                "bob - 192.168.0.10:7000"
        );

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
