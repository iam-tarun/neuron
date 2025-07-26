package com.ott.neuron.lanterna;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.ott.neuron.lanterna.screens.UserLoginScreen;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class NeuronUIManager {

    public static TextColor foreground = TextColor.ANSI.CYAN;
    public static TextColor background = TextColor.ANSI.BLACK;

    public static void start() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            final Window window = new UserLoginScreen().build(textGUI);

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
