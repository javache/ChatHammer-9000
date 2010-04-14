package ch9k.core.gui;

import ch9k.core.Login;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

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
    private JPasswordField passwordField;
    private JTextField usernameField;
    private Login loginController;

    /** Creates new form LoginView */
    public LoginView(final Login loginController) {
        this.loginController = loginController;

        // init fields and layout
        initFields();
        initLayout();

        // create a frame
        JFrame window = new JFrame("ChatHammer 9000");
        window.setContentPane(this);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
    }

    private void initFields() {
        titleLabel = new JLabel("ChatHammer 9000");
        titleLabel.setFont(titleLabel.getFont().deriveFont(16f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameLabel = new JLabel("Username");
        usernameField = new JTextField();

        passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField();

        loginButton = new JButton("Log in");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginController.loadConfiguration(
                        new String(usernameField.getText()),
                        new String(passwordField.getPassword()));
            }
        });

        newUserButton = new JButton();
        newUserButton.setText("New user");
        newUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginController.createConfiguration(
                        new String(usernameField.getText()));
            }
        });
    }
   
    private void initLayout() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup()
                .addComponent(titleLabel, GroupLayout.DEFAULT_SIZE,
                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addComponent(usernameLabel)
                        .addComponent(passwordLabel)
                    )
                    .addGap(20)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(usernameField, GroupLayout.PREFERRED_SIZE,
                            80, Short.MAX_VALUE)
                        .addComponent(passwordField, GroupLayout.PREFERRED_SIZE,
                            80, Short.MAX_VALUE)
                        .addComponent(loginButton)
                    )
                )
            )
            .addContainerGap()
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(titleLabel)
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
            .addComponent(loginButton)
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
}
