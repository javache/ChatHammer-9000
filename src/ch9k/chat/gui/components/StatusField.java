package ch9k.chat.gui.components;

import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import java.awt.SystemColor;

/**
 * Textfield that allows the user to update his status
 * @author Pieter De Baets
 */
public class StatusField extends DefaultValueField {
    public StatusField() {
        super(I18n.get("ch9k.chat", "set_status"), SystemColor.textInactiveText);
        
        String text = ChatApplication.getInstance().getAccount().getStatus();
        if(!text.isEmpty()) {
            setText(text);
        }
    }

    public void update() {
        String status = "";
        if(isTextChanged()) {
            status = getText();
        }

        Account account = ChatApplication.getInstance().getAccount();
        account.setStatus(status);
    }
}
