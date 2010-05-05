package ch9k.chat.gui;

import ch9k.chat.ChatMessage;
import ch9k.chat.Conversation;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Formatter;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

/**
 * Presentation of the Conversation
 * @author Pieter De Baets
 */
class ConversationListView extends JScrollPane {
    private Conversation conversation;
    private JList messages;

    /**
     * Construct a new ConversationListView
     * @param conversation
     */
    public ConversationListView(Conversation conversation) {
        this.conversation = conversation;

        messages = new JList(conversation.getMessageList());
        messages.setCellRenderer(new ChatMessageListCellRenderer());

        setViewportView(messages);
    }
    
    /**
     * Renders a chat message
     */
    public class ChatMessageListCellRenderer extends JPanel implements ListCellRenderer {

        private JLabel author;

        private JLabel text;

        public ChatMessageListCellRenderer() {
            setLayout(new BorderLayout());
            author = new JLabel();
            text = new JLabel();
            add(author, BorderLayout.WEST);
            add(text, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            ChatMessage message = (ChatMessage) value;
            Formatter formatter = new Formatter();
            formatter.format("<%1$tH:%1$tM>", message.getTime());
            author.setText(formatter.toString() + " " + message.getAuthor() + ": ");
            text.setText(message.getText());
            return this;
        }
    }
}
