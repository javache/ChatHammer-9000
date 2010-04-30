/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch9k.chat.gui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.text.StyledEditorKit;

/**
 *
 * @author Bruno
 */
public class FontPanel extends JPanel implements ActionListener {
    private Action boldAction = new StyledEditorKit.BoldAction();

    private Action underlineAction = new StyledEditorKit.UnderlineAction();

    private Action italicAction = new StyledEditorKit.ItalicAction();

    private String[] fontTypes = {"Serif", "SansSerif", "Monospaced"};

    private String[] fontSizes = { "10", "12", "14", "16", "18", "20" };

    private JComboBox fontType;

    private JComboBox fontSize;

    public FontPanel() {
        JToggleButton bold = new JToggleButton(boldAction);
        bold.setText("B");
        Font newButtonFont = new Font(bold.getFont().getName(), Font.BOLD, bold.getFont().getSize());
        bold.setFont(newButtonFont);

        JToggleButton italic = new JToggleButton(italicAction);
        italic.setText("I");
        newButtonFont = new Font(italic.getFont().getName(), Font.ITALIC, italic.getFont().getSize());
        italic.setFont(newButtonFont);

        JToggleButton underline = new JToggleButton(underlineAction);
        underline.setText("<html> <u>U</u> </html>");
        underline.setFont(UIManager.getFont("Button.font"));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String familyNames[] = ge.getAvailableFontFamilyNames();

        for (String familyName : familyNames) {
            System.out.println("Family names: " + familyName);
        }
        
        add(bold);
        add(italic);
        add(underline);

        fontType = new JComboBox(fontTypes);
        fontType.addActionListener(this);
        fontType.setSelectedItem("SansSerif");
        add(fontType);
        
        fontSize = new JComboBox(fontSizes);
        fontSize.addActionListener(this);
        fontSize.setSelectedItem("14");
        add(fontSize);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fontType) {
            int index = (((JComboBox) e.getSource()).getSelectedIndex());
            new StyledEditorKit.FontFamilyAction("set-font-family", fontTypes[index]).actionPerformed(e);
        } else if (e.getSource() == fontSize) {
            int index = (((JComboBox) e.getSource()).getSelectedIndex());
            new StyledEditorKit.FontSizeAction("set-font-size", Integer.parseInt(fontSizes[index])).actionPerformed(e);
        }
    }
}
