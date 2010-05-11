/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch9k.chat.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

/**
 *
 * @author Bruno
 */
class EmoticonRenderer extends JLabel implements ListCellRenderer {

    private Border unselectedBorder;
    private Border selectedBorder;
    
    public EmoticonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object emoticon,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        URL imgURL;
        try {
            imgURL = new URL("http://zeus.ugent.be/~Robust2/" + emoticon + ".gif");
            ImageIcon icon = new ImageIcon(imgURL);
            setIcon(icon);
            icon.setImageObserver(list);
        } catch(MalformedURLException ex) {
            Logger.getLogger(EmoticonRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(isSelected) {
            if(selectedBorder == null) {
                selectedBorder = BorderFactory.createMatteBorder(2, 2, 2, 2,
                        list.getSelectionBackground());
            }
            setBorder(selectedBorder);
        } else {
            if(unselectedBorder == null) {
                unselectedBorder = BorderFactory.createMatteBorder(2, 2, 2, 2,
                        list.getBackground());
            }
            setBorder(unselectedBorder);
        }

        return this;
    }
}
