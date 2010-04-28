
package ch9k.chat.gui;

import ch9k.chat.AddContactController;
import ch9k.core.ChatApplication;
import ch9k.chat.ContactList;
import ch9k.chat.Contact;
import ch9k.core.I18n;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 * Dialog to add contact
 * @author Pieter De Baets
 */
public class AddContactPanel extends JPanel {
    private AddContactController controller;
    private JDialog dialog;
    
    private JLabel titleLabel;
    private JLabel errorMessage;
    private JLabel usernameLabel;
    private JLabel ipLabel;
    
    private JTextField usernameField;
    private JTextField ipField;
    
    private JButton addContactButton;
    private JButton cancelButton;

    public AddContactPanel(AddContactController controller, JDialog dialog) {
        this.controller = controller;
        this.dialog = dialog;

        dialog.setContentPane(this);

        initComponents();
        initLayout();
    }

    private void initComponents() {
        titleLabel = new JLabel(I18n.get("ch9k.chat", "add_contact"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));

        usernameLabel = new JLabel(I18n.get("ch9k.chat", "username"));
        ipLabel = new JLabel(I18n.get("ch9k.chat", "ip"));

        usernameField = new JTextField();
        // todo make formatted field
        ipField = new JTextField();
        
        errorMessage = new JLabel();
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);

        Action registerAction = new AbstractAction(I18n.get("ch9k.chat", "add_contact")) {
            public void actionPerformed(ActionEvent e) {
                addContact();
                dialog.dispose();
            }
        };
        addContactButton = new JButton(registerAction);
        dialog.getRootPane().setDefaultButton(addContactButton);

        cancelButton = new JButton(new AbstractAction(I18n.get("ch9k.chat", "cancel")) {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    /**
     * add a contact
     * TODO error handling and field validation
     */
    private void addContact() {
        ContactList contactList = ChatApplication.getInstance().getAccount().getContactList();
        try {
            contactList.addContact(new Contact(usernameField.getText(),
                                               InetAddress.getByName(ipField.getText())), true);
        } catch (UnknownHostException e) {
            
        }

    }

    private void initLayout() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addContainerGap(15, 15)
            .addGroup(layout.createParallelGroup()
                .addComponent(titleLabel)
                .addComponent(errorMessage)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addComponent(usernameLabel)
                        .addComponent(ipLabel)
                    )
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(usernameField, 140, 140, 140)
                        .addComponent(ipField, 140, 140, 140)
                    )
                )
                .addGroup(layout.createSequentialGroup()
                    .addComponent(cancelButton)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, Short.MAX_VALUE)
                    .addComponent(addContactButton)
                )
            )
            .addContainerGap(15, 15)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addContainerGap(20, 20)
            .addComponent(titleLabel)
            .addGap(10, 10, Short.MAX_VALUE)
            .addComponent(errorMessage)
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(usernameLabel)
                .addComponent(usernameField)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(ipLabel)
                .addComponent(ipField)
            )
            .addGap(10, 10, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(addContactButton)
                .addComponent(cancelButton)
            )
            .addContainerGap(15, 15)
        );
    }
}
