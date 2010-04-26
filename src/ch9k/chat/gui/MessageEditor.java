package ch9k.chat.gui;

import ch9k.chat.Conversation;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 *
 * @author Pieter De Baets
 */
class MessageEditor extends JPanel {
    private Conversation conversation;
    private JTextPane input;

    public MessageEditor(Conversation conversation) {
        this.conversation = conversation;
        setPreferredSize(new Dimension(0, 100));

        input = new JTextPane();
        add(input);
    }
}
