package ch9k.chat.gui.views;

import ch9k.chat.ChatMessage;
import java.awt.Component;
import java.util.Formatter;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Jens Panneel
 */
public class ChatMessageListCellRenderer extends JLabel implements ListCellRenderer {

    public ChatMessageListCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        ChatMessage message = (ChatMessage)value;
        Formatter f = new Formatter();
        f.format("<%1$tH:%1$tM> %2$s: %3$s", message.getTime(),message.getAuthor(), message.getText());
        setText(f.toString());
        return this;
    }
}
