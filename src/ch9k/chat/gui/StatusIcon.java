
package ch9k.chat.gui;

import ch9k.core.Account;
import ch9k.core.ChatApplication;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * Shows the status of a user as a color
 * @author Pieter De Baets
 */
public class StatusIcon implements Icon {
    Account account;

    public StatusIcon() {
        account = ChatApplication.getInstance().getAccount();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y+2, 18, 18);
        g.setColor(Color.BLACK);
        g.drawRect(x, y+2, 18, 18);
    }

    @Override
    public int getIconWidth() {
        return 24;
    }

    @Override
    public int getIconHeight() {
        return 20;
    }
}
