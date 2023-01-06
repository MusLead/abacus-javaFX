package de.uniks.abacus.controller;

import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public interface Controller
{
    String getTitle();

    void init();

    Parent render() throws IOException;

    void destroy();

    void keyboardListener( KeyEvent e );
}
