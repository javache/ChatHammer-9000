package ch9k.core.gui;

import ch9k.core.ApplicationWindow;
import ch9k.core.LoginController;
import ch9k.core.RegistrationController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
 * @author Pieter De Baets
 */
public class LoginPanel extends JPanel {
    private JButton newUserButton;
    private JButton loginButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel titleLabel;
    private JLabel errorMessage;
    private JPasswordField passwordField;
    private JTextField usernameField;

    private final LoginController controller;
    private final ApplicationWindow window;

    /** 
     * Creates new LoginView form
     * @param loginController
     * @param window 
     */
    public LoginPanel(LoginController loginController, ApplicationWindow window) {
        this.controller = loginController;
        this.window = window;

        // setup window
        window.setContentPane(this);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                controller.setCancelled(true);
            }
        });

        // init fields and layout
        initComponents();
        initLayout();

        window.setVisible(true);
    }

    private void initComponents() {
        titleLabel = new JLabel("<html><b>ChatHammer</b> 9000");
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameLabel = new JLabel("Username");
        usernameField = new JTextField();

        passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField();
        
        Action loginAction = new AbstractAction("Log in") {
            public void actionPerformed(ActionEvent e) {
                validateLogin();
            }
        };
        loginButton = new JButton(loginAction);
        getRootPane().setDefaultButton(loginButton);

        newUserButton = new JButton();
        newUserButton.setText("New user?");
        newUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new RegistrationController(controller, window);
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
                        .addComponent(usernameLabel)
                        .addComponent(passwordLabel)
                        .addComponent(newUserButton)
                    )
                    .addGroup(layout.createParallelGroup()
                        .addComponent(usernameField, 140, 140, 140)
                        .addComponent(passwordField, 140, 140, 140)
                        .addComponent(loginButton, Alignment.TRAILING)
                    )
                )
            )
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addContainerGap(50, 100)
            .addComponent(titleLabel)
            .addGap(10)
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
                .addComponent(loginButton)
                .addComponent(newUserButton)
            )
            .addContainerGap(50, 150)
        );
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(username.isEmpty() || password.isEmpty()) {
            errorMessage.setText("<html><center>Please fill in all fields.");
            errorMessage.setVisible(true);
        } else {
            boolean success = controller.login(username, password);
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
