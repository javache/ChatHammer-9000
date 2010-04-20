
package ch9k.chat.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * Shows the status of a user as a color
 * @author Pieter De Baets
 */
public class StatusIcon implements Icon {
    private int size;
    private boolean online;

    /**
     * Create a new statusicon
     * @param size Size of the component
     * @param online Initial status
     */
    public StatusIcon(int size, boolean online) {
        this.size = size;
        this.online = online;
    }

    /**
     * Set the online status
     * @param online
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        // TODO: add away and busy
        if(online) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.GRAY);
        }
        g.fillRect(x, y+2, size-2, size-2);

        g.setColor(Color.BLACK);
        g.drawRect(x, y+2, size-2, size-2);
    }

    @Override
    public int getIconWidth() {
        return size + 4;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}
