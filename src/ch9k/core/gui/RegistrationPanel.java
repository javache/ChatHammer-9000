
package ch9k.core.gui;

import ch9k.core.RegistrationController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 * Window that displays a registration dialog
 * @author Pieter De Baets
 */
public class RegistrationPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel preTextLabel;
    private JLabel postTextLabel;
    private JLabel ipTextLabel;
    private JLabel errorMessage;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel repeatPasswordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JButton registerButton;
    private JButton cancelButton;

    private RegistrationController controller;
    private JDialog dialog;

    /**
     * Create a new RegisterWindow
     * @param controller 
     */
    public RegistrationPanel(RegistrationController controller, JDialog dialog) {
        this.controller = controller;
        this.dialog = dialog;

        // set the contentpane
        dialog.setContentPane(this);
        
        // init fields and layout
        initComponents();
        initLayout();
    }

    public void setError(String message) {
        errorMessage.setText("<html><center>" + message);
        errorMessage.setVisible(message != null);
    }

    public void setIp(String ip) {
        ipTextLabel.setText(ip);
    }

    private void initComponents() {
        titleLabel = new JLabel("Register account");
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        preTextLabel = new JLabel("Please fill in the following fields to create your account.");
        postTextLabel = new JLabel("<html>If you wish for other contacts to add you, "
                + "they need to<br>know your IP-address. You are currently reachable at:");
        ipTextLabel = new JLabel(" ");

        errorMessage = new JLabel();
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);

        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        repeatPasswordLabel = new JLabel("Repeat password");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        repeatPasswordField = new JPasswordField();

        Action registerAction = new AbstractAction("Register") {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String repeatedPassword =
                        new String(repeatPasswordField.getPassword());
                boolean result = controller.validate(username,
                        password, repeatedPassword);
                if(result) {
                    dialog.dispose();
                }
            }
        };
        registerButton = new JButton(registerAction);
        dialog.getRootPane().setDefaultButton(registerButton);

        cancelButton = new JButton(new AbstractAction("Cancel") {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    private void initLayout() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup()
                .addComponent(titleLabel)
                .addComponent(preTextLabel)
                .addGroup(layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(errorMessage)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup()
                                .addComponent(usernameLabel)
                                .addComponent(passwordLabel)
                                .addComponent(repeatPasswordLabel)
                            )
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup()
                                .addComponent(usernameField, 140, 140, 140)
                                .addComponent(passwordField, 140, 140, 140)
                                .addComponent(repeatPasswordField, 140, 140, 140)
                            )
                        )
                    )
                )
                .addComponent(postTextLabel)
                .addGroup(layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(ipTextLabel)
                )
                .addGroup(layout.createSequentialGroup()
                    .addComponent(registerButton)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, Short.MAX_VALUE)
                    .addComponent(cancelButton)
                )
             )
            .addContainerGap()
        );

        layout.setVerticalGroup(layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(10, 10, Short.MAX_VALUE)
                .addComponent(preTextLabel)
                .addGap(10)
                .addComponent(errorMessage)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(repeatPasswordLabel)
                    .addComponent(repeatPasswordField)
                )
                .addGap(10, 10, Short.MAX_VALUE)
                .addComponent(postTextLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ipTextLabel)
                .addGap(10, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(registerButton)
                    .addComponent(cancelButton)
                )
                .addContainerGap())
        );
    }
}
