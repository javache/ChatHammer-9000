/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch9k.chat.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

/**
 *
 * @author Bruno
 */
class ColorRenderer extends JLabel implements ListCellRenderer {
    Border unselectedBorder;
    
    Border selectedBorder;

    public ColorRenderer() {
        setOpaque(true);

    }

    public Component getListCellRendererComponent(
            JList list,
            Object color,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        setText("     ");
        super.setBackground((Color) color);


        if(isSelected) {
            if(selectedBorder == null) {
                selectedBorder =
                        BorderFactory.createMatteBorder(2, 2, 2, 2,
                        list.getSelectionBackground());
            }
            setBorder(selectedBorder);
        } else {
            if(unselectedBorder == null) {
                unselectedBorder =
                        BorderFactory.createMatteBorder(2, 2, 2, 2,
                        list.getBackground());
            }
            setBorder(unselectedBorder);
        }


        return this;
    }

    @Override
    public void setForeground(Color color) {
    }

    @Override
    public void setBackground(Color color) {
    }
}

