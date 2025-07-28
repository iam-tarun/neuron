package com.ott.neuron.cortex;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.Window;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WindowStyler {
    public static final Theme TERMINA_THEME = new SimpleTheme(
            TextColor.ANSI.GREEN,
            TextColor.ANSI.BLACK
    );

    public static void apply(Window window) {
        window.setTheme(TERMINA_THEME);
        window.setHints(List.of(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));
    }

    public static Theme getTheme() {
        return TERMINA_THEME;
    }
}
