package ch9k.chat.gui.views;

import ch9k.chat.Conversation;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author jpanneel
 */
class ConversationPanel extends JPanel{
    private Conversation conversation;

    private ConversationContactLabel contactLabel;
    private ChatMessageListView chatMessageListView;
    //private ChatMessageInputThing input;

    public ConversationPanel(Conversation conversation) {
        super();
        this.conversation = conversation;
        this.setLayout(new BorderLayout());

        contactLabel = new ConversationContactLabel(conversation.getContact());
        this.add(contactLabel, BorderLayout.NORTH);

        chatMessageListView = new ChatMessageListView(conversation);
        this.add(chatMessageListView, BorderLayout.CENTER);
    }
}
