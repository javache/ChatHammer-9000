package ch9k.chat.gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 * Textfield that allows the user to update his status
 * @author Pieter De Baets
 */
public class StatusField extends JTextField implements FocusListener {
    public StatusField() {
        super("Set status");
        setForeground(Color.GRAY);

        addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(getText().equals("Set status")) {
            setText("");
            setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(getText().isEmpty()) {
            setText("Set status");
            setForeground(Color.GRAY);
        }
    }
}
