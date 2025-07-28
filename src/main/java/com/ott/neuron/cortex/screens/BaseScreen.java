package com.ott.neuron.cortex.screens;

import com.googlecode.lanterna.gui2.*;
import com.ott.neuron.cortex.WindowStyler;

public abstract class BaseScreen {

    public Window build(WindowBasedTextGUI gui) {
        Window window = new BasicWindow(getTitle());
        WindowStyler.apply(window);

        window.setComponent(buildContent(gui));
        return window;
    }

    protected abstract String getTitle();
    protected abstract Component buildContent(WindowBasedTextGUI gui);
}
