package ch9k.core.gui;

import ch9k.core.Login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

/**
 *
 * @author Bruno
 */
public class LoginView extends JPanel {

    private JButton newUserButton;
    private JButton loginButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private Login loginController;

    /** Creates new form LoginView */
    public LoginView(Login loginController) {
        this.loginController = loginController;

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        
        usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        
        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        
        loginButton = new JButton();
        loginButton.setText("Log in");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loginAction(evt);
            }
        });
        
        newUserButton = new JButton();
        newUserButton.setText("New user");
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                newUserAction(evt);
            }
        });

        initLayout();
    }

   
    private void initLayout() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newUserButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loginButton))
                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(usernameLabel)
                        .addGap(18, 18, 18)
                        .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent( passwordLabel)
                        .addGap(18, 18, 18)
                        .addComponent(passwordField)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent( passwordLabel)
                    .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(loginButton)
                    .addComponent(newUserButton))
                .addContainerGap(39, Short.MAX_VALUE))
        );
    }

    private void newUserAction(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void loginAction(java.awt.event.ActionEvent evt) {
        String password = new String(passwordField.getPassword());
        loginController.loadAccount(usernameField.getText(), password);
    }


}
