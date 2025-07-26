package com.ott.neuron.lanterna.screens;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

public class UserLoginScreen extends BaseScreen {

    private TextBox usernameBox;

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

            Window nextWindow = new NodeSelectionScreen(username).build(gui);
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
