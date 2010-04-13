package ch9k.chat.gui.views;

import ch9k.chat.Contact;
import ch9k.chat.ContactList;
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
public class ContactListView extends JPanel{

    private static ContactList contactList = ChatApplication.getInstance().getAccount().getContactList();

    private JList contactListView;

    public ContactListView() {
        super();
        init();
    }

    private void init() {
        contactListView = new JList(contactList);
        contactListView.setCellRenderer(new ContactListCellRenderer());
        // add all the components of a ContactList to this (panel)
        this.add(contactListView);
    }

    private static void initTest() {
        JFrame window = new JFrame("ContactList");
        window.setSize(new Dimension(300, 300));
        JList contactListView = new JList(contactList);
        contactListView.setCellRenderer(new ContactListCellRenderer());
        window.getContentPane().add(contactListView);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        new ContactListView();
        Contact contact1 = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Contact contact2 = new Contact("Jaspervdj", InetAddress.getByName("ugent.be"), true);
        contactList.addContact(contact1);
        contactList.addContact(contact2);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                initTest();
            }
        });
        Thread.sleep(2000);
        Contact contact3 = new Contact("Javache", InetAddress.getByName("4chan.org"), false);
        contactList.addContact(contact3);
        Thread.sleep(1000);
        contact1.setOnline(true);
    }
}
