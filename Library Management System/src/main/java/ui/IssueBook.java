package ui;

import service.TransactionService;

import javax.swing.*;
import java.awt.*;

public class IssueBook extends JFrame {
    private final TransactionService transactionService = new TransactionService();
    private final DashboardFrame dashboard;

    public IssueBook(DashboardFrame dashboard) {
        this.dashboard = dashboard; // assign the passed reference

        setTitle("Issue Book");
        setSize(400, 250);
        setLayout(new GridLayout(5, 2, 10, 10));
        setLocationRelativeTo(null); // center window

        JLabel bookIdLabel = new JLabel("Book ID:");
        JTextField bookIdField = new JTextField();

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField();

        JLabel dueDateLabel = new JLabel("Due Date (YYYY-MM-DD):");
        JTextField dueDateField = new JTextField();

        JButton issueButton = new JButton("Issue Book");

        add(bookIdLabel);
        add(bookIdField);
        add(studentIdLabel);
        add(studentIdField);
        add(dueDateLabel);
        add(dueDateField);
        add(new JLabel()); // spacer
        add(issueButton);

        issueButton.addActionListener(e -> {
            try {
                String bookText = bookIdField.getText().trim();
                String studentText = studentIdField.getText().trim();
                String dueDate = dueDateField.getText().trim();

                if (bookText.isEmpty() || studentText.isEmpty() || dueDate.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required.");
                    return;
                }

                int bookId = Integer.parseInt(bookText);
                int studentId = Integer.parseInt(studentText);

                boolean success = transactionService.issueBook(bookId, studentId, dueDate);
                if (success) {
                    JOptionPane.showMessageDialog(this, " Book issued successfully!");
                    dashboard.refreshBookList();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, " Failed to issue. Book may already be borrowed.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric IDs.");
            }
        });

        setVisible(true);
    }
}
