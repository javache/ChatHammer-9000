package ch9k.core.gui;

import ch9k.core.ApplicationWindow;
import ch9k.core.Login;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 * Shows options for login
 * @author Bruno Corijn
 */
public class LoginView extends JPanel {
    private JButton newUserButton;
    private JButton loginButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel titleLabel;
    private JLabel errorMessage;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private Login loginController;

    /** 
     * Creates new LoginView form
     * @param controller
     * @param window 
     */
    public LoginView(Login controller, ApplicationWindow window) {
        loginController = controller;

        // init fields and layout
        initFields();
        initLayout();

        // show window
        window.setContentPane(this);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                loginController.setCancelled(true);
            }
        });
        window.setVisible(true);
    }

    private void initFields() {
        titleLabel = new JLabel("ChatHammer 9000");
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameLabel = new JLabel("Username");
        usernameField = new JTextField();

        passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField();

        loginButton = new JButton("Log in");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                validateLogin();
            }
        });

        newUserButton = new JButton();
        newUserButton.setText("New user?");
        newUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // TODO: handle
            }
        });

        errorMessage = new JLabel();
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);
    }
   
    private void initLayout() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(Alignment.CENTER)
                .addComponent(titleLabel)
                .addComponent(errorMessage)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10)
                            .addGroup(layout.createParallelGroup()
                                .addComponent(usernameLabel)
                                .addComponent(passwordLabel)
                            )
                        )
                        .addComponent(newUserButton)
                    )
                    .addGap(20)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(usernameField, 120, 120, 120)
                        .addComponent(passwordField, 120, 120, 120)
                        .addComponent(loginButton, Alignment.TRAILING)
                    )
                )
            )
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addContainerGap(50, 100)
            .addComponent(titleLabel)
            .addGap(15)
            .addComponent(errorMessage)
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(usernameLabel)
                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE,
                    GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(passwordLabel)
                .addComponent(passwordField, GroupLayout.PREFERRED_SIZE,
                    GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(newUserButton)
                .addComponent(loginButton)
            )
            .addContainerGap(100, 150)
        );
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(username.isEmpty() || password.isEmpty()) {
            errorMessage.setText("<html><center>" +
                "Please fill in all fields.");
            errorMessage.setVisible(true);
            return;
        } else {
            boolean success = loginController.login(username, password);
            if(!success) {
                errorMessage.setText("<html><center>" +
                    "The provided credentials are invalid.<br />Please try again.");
                errorMessage.setVisible(true);
            } else {
                errorMessage.setVisible(false);
            }
        }
    }
}
