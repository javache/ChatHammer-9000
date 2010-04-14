package ch9k.chat.gui.views;

import javax.swing.JList;
import javax.swing.ListModel;

/**
 *
 * @author Jens Panneel
 */
public class ChatMessageListView extends JList{
    public ChatMessageListView(ListModel listModel) {
        super(listModel);
        this.setCellRenderer(new ChatMessageListCellRenderer());
    }
}
