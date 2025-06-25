package ui;

import service.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final UserService userService = new UserService();

    public LoginFrame() {
        setTitle("Library Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 30, 130, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 130, 25);
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(30, 110, 100, 25);
        loginBtn.addActionListener(this::handleLogin);
        add(loginBtn);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(150, 110, 100, 25);
        registerBtn.addActionListener(this::handleRegister);
        add(registerBtn);

        setVisible(true);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (userService.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new DashboardFrame(username);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login failed.");
        }
    }

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (userService.register(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed.");
        }
    }
}
