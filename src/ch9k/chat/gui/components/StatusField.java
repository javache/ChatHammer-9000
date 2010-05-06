package ch9k.chat.gui.components;

import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
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
public class StatusField extends JTextField implements FocusListener, ActionListener {
    private static final String DEFAULT_TEXT = I18n.get("ch9k.chat", "set_status");

    private boolean textChanged;
    
    public StatusField() {
        super(DEFAULT_TEXT);
        setForeground(SystemColor.textInactiveText);
        setFont(getFont().deriveFont(12f));
        setText(ChatApplication.getInstance().getAccount().getStatus());

        // add listener so we can clear text on focus
        addFocusListener(this);
        addActionListener(this);
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

        updateStatus();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        updateStatus();
    }
    
    public void updateStatus() {
        String status = "";
        if(textChanged) {
            status = getText();
        }

        Account account = ChatApplication.getInstance().getAccount();
        account.setStatus(status);
    }
}
