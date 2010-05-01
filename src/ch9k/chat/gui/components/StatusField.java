package ch9k.chat.gui.components;

import ch9k.core.I18n;
import java.awt.SystemColor;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 * Textfield that allows the user to update his status
 * @author Pieter De Baets
 */
public class StatusField extends JTextField implements FocusListener {
    private static final String DEFAULT_TEXT = I18n.get("ch9k.chat", "set_status");

    private boolean textChanged;
    
    public StatusField() {
        super(DEFAULT_TEXT);
        setForeground(SystemColor.textInactiveText);

        // add listener so we can clear text on focus
        addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(!textChanged && getText().equals(DEFAULT_TEXT)) {
            setText("");
            setForeground(SystemColor.textText);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(getText().isEmpty()) {
            setText(DEFAULT_TEXT);
            setForeground(SystemColor.textInactiveText);
            textChanged = false;
        } else {
            textChanged = true;
        }
    }
}
