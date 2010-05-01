/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch9k.chat.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.StyledEditorKit;

/**
 *
 * @author Bruno
 */
public class FontPanel extends JPanel implements ActionListener, CaretListener {
    private Action boldAction = new StyledEditorKit.BoldAction();

    private Action underlineAction = new StyledEditorKit.UnderlineAction();

    private Action italicAction = new StyledEditorKit.ItalicAction();

    private String[] fontTypes = {"Serif", "SansSerif", "Monospaced"};

    private String[] fontSizes = { "10", "12", "14", "16", "18", "20" };

    private JComboBox fontType;

    private JComboBox fontSize;

    private JTextPane editor;

    public FontPanel(JTextPane textpane) {
        editor = textpane;
        editor.addCaretListener(this);

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
            int pos = editor.getCaretPosition();
            editor.selectAll();
            new StyledEditorKit.FontFamilyAction("set-font-family", fontTypes[index]).actionPerformed(e);
            editor.setCaretPosition(pos);
        } else if (e.getSource() == fontSize) {
            int pos = editor.getCaretPosition();
            editor.selectAll();
            int index = (((JComboBox) e.getSource()).getSelectedIndex());
            new StyledEditorKit.FontSizeAction("set-font-size", Integer.parseInt(fontSizes[index])).actionPerformed(e);
            editor.setCaretPosition(pos);
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        //TODO
    }
}

