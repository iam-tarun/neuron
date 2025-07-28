package com.ott.neuron.cortex.screens;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.ott.neuron.impulse.Impulse;
import com.ott.neuron.impulse.ImpulseMemory;
import com.ott.neuron.impulse.ImpulseType;
import com.ott.neuron.synapse.Axon;
import com.ott.neuron.user.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MessageScreen extends BaseScreen {

    private final String username;
    private final String neuron;
    private final String source;
    private final ImpulseMemory memory;
    private final UserService userService;

    private Axon axon;
    public Set<String> seenIds = new HashSet<>();

    public MessageScreen(String username, String neuron, String source, Axon axon, ImpulseMemory memory, UserService userService) {
        this.username = username;
        this.neuron = neuron;
        this.source = source;
        this.axon = axon;
        this.memory = memory;
        this.userService = userService;
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
        TextBox messageLog = new TextBox(new TerminalSize(80, 10))
                .setReadOnly(true)
                .setCaretWarp(true)
                .setVerticalFocusSwitching(true);

        Thread pollerThread = getThread(gui, messageLog);
        pollerThread.start();


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

            Impulse imp = new Impulse(UUID.randomUUID(), neuron, Instant.now(), ImpulseType.CHAT, newMessage, source);

            axon.sendImpulse(neuron, imp);
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
        System.out.println("Source is " + source + " , neuron is " + neuron);

        return content;
    }

    private Thread getThread(WindowBasedTextGUI gui, TextBox messageLog) {
        Thread pollerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {

                List<Impulse> newMessages = memory.getRecent(100).stream()
                        .filter(msg -> !seenIds.contains(msg.id().toString())).toList();

                if (!newMessages.isEmpty()) {
                    gui.getGUIThread().invokeLater(() -> {
                        for (Impulse msg :  newMessages) {
                            String sender = msg.source().equals(userService.getNeuronId()) ? "You" : msg.source();
                            messageLog.addLine(sender+": "+msg.content());
                            seenIds.add(msg.id().toString());
                        }
                        messageLog.setCaretPosition(messageLog.getLineCount() - 1);
                    });
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        });

        pollerThread.setDaemon(true);
        return pollerThread;
    }

}
