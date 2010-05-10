package ch9k.chat.gui.components;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 * Textfield that allows the user to update his status
 * @author Pieter De Baets
 */
public abstract class DefaultValueField extends JTextField implements FocusListener, ActionListener {
    private String defaultText;
    private Color defaultColor;
    private boolean textChanged;
    
    public DefaultValueField(String defaultText, Color defaultColor) {
        super(defaultText);

        this.defaultText = defaultText;
        this.defaultColor = defaultColor;
        this.textChanged = false;

        setForeground(defaultColor);
        setFont(getFont().deriveFont(12f));

        // add listener so we can clear text on focus
        addFocusListener(this);
        addActionListener(this);
    }

    public boolean isTextChanged() {
        return textChanged;
    }

    public void setText(String text) {
        textChanged = !text.equals(defaultText);
        super.setText(text);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(!textChanged) {
            setText("");
            setForeground(SystemColor.textText);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(getText().isEmpty()) {
            setText(defaultText);
            setForeground(defaultColor);
            textChanged = false;
        } else {
            textChanged = true;
        }

        update();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }
    
    public abstract void update();
}
