package ch9k.chat.gui;

import ch9k.chat.ChatMessage;
import ch9k.chat.Conversation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * Presentation of the Conversation
 * @author Pieter De Baets
 */
class ConversationView extends JScrollPane {
    private Conversation conversation;
    private ConversationTextPane textPane;

    /**
     * Construct a new ConversationListView
     * @param conversation
     */
    public ConversationView(Conversation conversation) {
        this.conversation = conversation;
        
        textPane = new ConversationTextPane(conversation.getMessageList());
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setBackground(Color.RED);
        textPane.setOpaque(false);

        JPanel textPaneHolder = new JPanel(new BorderLayout());
        textPaneHolder.add(textPane, BorderLayout.NORTH);

        setViewportView(textPane);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    /**
     * Renders the conversation
     */
    public class ConversationTextPane extends JTextPane
            implements ListDataListener {
        private ListModel listModel;
        private boolean init = true;
        
        public ConversationTextPane(ListModel listModel) {
            this.listModel = listModel;
            listModel.addListDataListener(this);


        }

        private void render(ChatMessage message) {
            HTMLDocument document = (HTMLDocument)getDocument();
            HTMLEditorKit editorKit = (HTMLEditorKit)getEditorKit();
            
            try {
                if(init) {
                    setText(message.getText());
                    init = false;
                } else {
                    int offset = document.getEndPosition().getOffset() - 1;
                    editorKit.insertHTML(document, offset, message.getInsertHtml(), 1, 0, null);
                }
            } catch(BadLocationException ex) {
            } catch(IOException ex) {}
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            for(int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                ChatMessage message = (ChatMessage)listModel.getElementAt(i);
                render(message);
            }
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            // do nothing
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            // do nothing
        }
    }
}
