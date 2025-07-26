package com.ott.neuron.lanterna.screens;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class MessageScreen extends BaseScreen {

    private final String username;
    private final String neuron;

    public MessageScreen(String username, String neuron) {
        this.username = username;
        this.neuron = neuron;
    }

    @Override
    public String getTitle() {
        return "Chat!";
    }

    @Override
    public Component buildContent(WindowBasedTextGUI gui) {
        Panel content = new Panel(new LinearLayout(Direction.VERTICAL));

        Panel header = new Panel(new GridLayout(2));
        header.addComponent(new Label("Talking to: " + this.neuron));

        content.addComponent(header);
        content.addComponent(new EmptySpace(new TerminalSize(1, 1)));

//        messages
        TextBox messageLog = new TextBox(new TerminalSize(80, 3))
                .setReadOnly(true)
                .setCaretWarp(true)
                .setVerticalFocusSwitching(true);

        messageLog.setText("You: Hello\nneuronx: Hi");


        content.addComponent(messageLog);
        content.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        Panel inputRow = new Panel(new GridLayout(2));
        TextBox newMessageBox = new TextBox(new TerminalSize(60, 1));
        gui.getGUIThread().invokeLater(newMessageBox::takeFocus);

        Button sendButton = new Button("Send", () -> {
            String newMessage = newMessageBox.getText();
            if (newMessage == null || newMessage.isBlank()) {
                MessageDialog.showMessageDialog(gui, "Error", "Message is empty!");
                return;
            }

            messageLog.addLine("You: " + newMessage);
            messageLog.setCaretPosition(messageLog.getLineCount() - 1);
            newMessageBox.setText("");
        });

        inputRow.addComponent(newMessageBox.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.GREEN)));
        inputRow.addComponent(sendButton);
        content.addComponent(inputRow);

        content.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        content.addComponent(new Button("Back", () -> {
            gui.getActiveWindow().close();
        }));

        return content;
    }

}
