package ch9k.core.gui;

import ch9k.chat.gui.components.StatusField;
import ch9k.chat.gui.components.StatusIcon;
import ch9k.core.Account;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays information about the
 * @author Pieter De Baets
 */
public class AccountPanel extends JPanel {
    private JLabel accountLabel;
    private StatusField statusField;
    private JButton logoffButton;
    private StatusIcon statusIcon = new StatusIcon(20, true);

    public AccountPanel() {
        initComponents();
        initLayout();
    }

    public void initComponents() {
        Account account = ChatApplication.getInstance().getAccount();

        accountLabel = new JLabel(account.getUsername());
        accountLabel.setFont(accountLabel.getFont().deriveFont(15f));
        accountLabel.setIcon(statusIcon);

        statusField = new StatusField();
        String logoffButtonText = I18n.get("ch9k.core", "log_off");
        logoffButton = new JButton(new AbstractAction(logoffButtonText) {
            public void actionPerformed(ActionEvent e) {
                ChatApplication.getInstance().logoff(true);
            }
        });
    }

    public void initLayout() {
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(accountLabel)
                .addComponent(statusField, 140, 200, 400)
            )
            .addGap(5, 5, Short.MAX_VALUE)
            .addComponent(logoffButton)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(accountLabel)
                .addComponent(logoffButton)
            )
            .addGap(4)
            .addComponent(statusField)
        );
    }
}
