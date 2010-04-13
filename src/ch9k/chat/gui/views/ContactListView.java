package ch9k.chat.gui.views;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
import ch9k.core.Account;
import ch9k.core.ChatApplication;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author Jens Panneel
 */
public class ContactListView extends JPanel {
    private JList list;

    /**
     * Create a new ContactListView
     * (must be called from EventQueue thread)
     */
    public ContactListView() {
        Account account = ChatApplication.getInstance().getAccount();
        ContactList contactList = account.getContactList();

        list = new JList(contactList);
        list.setCellRenderer(new ContactListCellRenderer());
        
        // add components to the panel
        add(list);
    }

    private static void initTest(ContactListView listView) {
        JFrame window = new JFrame("ContactList");
        window.setSize(new Dimension(300, 300));
        window.setContentPane(listView);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        Account account = ChatApplication.getInstance().getAccount();
        ContactList contactList = account.getContactList();
        
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact contact2 = new Contact("Jaspervdj", InetAddress.getByName("ugent.be"), true);
        contactList.addContact(contact1);
        contactList.addContact(contact2);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ContactListView contactListView = new ContactListView();
                initTest(contactListView);
            }
        });
        
        Thread.sleep(2000);
        Contact contact3 = new Contact("Javache", InetAddress.getByName("4chan.org"), false);
        contactList.addContact(contact3);
        
        Thread.sleep(1000);
        contact1.setOnline(true);
    }
}
