package ch9k.chat.gui;

import ch9k.chat.ChatMessage;
import ch9k.chat.Conversation;
import ch9k.chat.event.NewChatMessageEvent;
import ch9k.core.ChatApplication;

import ch9k.eventpool.EventPool;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author Pieter De Baets
 */
class MessageEditor extends JPanel {
    private Conversation conversation;
    private JTextPane editor;

    public MessageEditor(Conversation conversation) {
        super(new BorderLayout());
        this.conversation = conversation;
        this.setPreferredSize(new Dimension(0,100));

        editor = new JTextPane();
        editor.setEditable(true);
        editor.setContentType("text/html");
        editor.putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        editor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(editor);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                send();
            }
        });

        JPanel input = new JPanel(new BorderLayout());
        JPanel buttons = new FontPanel(editor);
        
        input.add(scrollPane, BorderLayout.CENTER);
        input.add(sendButton,BorderLayout.EAST);

        this.add(buttons, BorderLayout.NORTH);
        this.add(input, BorderLayout.CENTER);
    }

    public void send(){
        if(!editor.getText().trim().isEmpty()){
            ChatMessage message = new ChatMessage(ChatApplication.getInstance().getAccount().getUsername(), editor.getText());
            EventPool.getAppPool().raiseEvent(new NewChatMessageEvent(conversation,message));
            editor.setText("");
        }
    }

}
