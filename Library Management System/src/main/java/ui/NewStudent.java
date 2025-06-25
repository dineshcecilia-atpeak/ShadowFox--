package ui;

import service.StudentService;

import javax.swing.*;
import java.awt.*;

public class NewStudent extends JFrame {
    private final StudentService studentService = new StudentService();
    private final DashboardFrame dashboard;

    public NewStudent(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Add New Student");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));

        JTextField nameField = new JTextField();
        JTextField rollField = new JTextField();
        JButton addButton = new JButton("Add");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Roll No:"));
        add(rollField);
        add(new JLabel());
        add(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String roll = rollField.getText().trim();

            if (!name.isEmpty() && !roll.isEmpty()) {
                boolean success = studentService.addStudent(name, roll);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Student added!");
                    dashboard.refreshBookList();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add student.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both fields.");
            }
        });

        setVisible(true);
    }
}
