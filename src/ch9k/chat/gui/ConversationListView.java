package ch9k.chat.gui;

import ch9k.chat.ChatMessage;
import ch9k.chat.Conversation;
import ch9k.core.I18n;
import java.awt.Component;
import java.util.Formatter;
import javax.swing.GroupLayout;
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
            author = new JLabel();
            text = new JLabel();

            GroupLayout layout = new GroupLayout(this);
            setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                    .addComponent(author)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15)
                        .addComponent(text)
                        .addGap(10)
                    )
                )
                .addGap(5)
            );

            layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(author)
                .addGap(3)
                .addComponent(text)
                .addGap(4)
            );
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            ChatMessage message = (ChatMessage) value;

            Formatter formatter = new Formatter();
            String date = formatter.format("%1$tH:%1$tM", message.getTime()).toString();
            author.setText(I18n.get("ch9k.chat", "contact_said",
                message.getAuthor(), date));
            text.setText(message.getText());
            return this;
        }
    }
}
