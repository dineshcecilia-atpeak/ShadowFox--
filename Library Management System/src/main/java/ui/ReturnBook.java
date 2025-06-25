package ui;

import service.TransactionService;

import javax.swing.*;
import java.awt.*;

public class ReturnBook extends JFrame {
    private final TransactionService transactionService = new TransactionService();
    private final DashboardFrame dashboard;

    public ReturnBook(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("↩️ Return Book");
        setSize(400, 200);
        setLayout(new GridLayout(4, 2, 10, 10));
        setLocationRelativeTo(null); // center window

        JLabel bookIdLabel = new JLabel("Book ID:");
        JTextField bookIdField = new JTextField();

        JLabel studentIdLabel = new JLabel("Student ID:");
        JTextField studentIdField = new JTextField();

        JButton returnButton = new JButton("Return Book");

        add(bookIdLabel);
        add(bookIdField);
        add(studentIdLabel);
        add(studentIdField);
        add(new JLabel()); // spacer
        add(returnButton);

        returnButton.addActionListener(e -> {
            try {
                String bookText = bookIdField.getText().trim();
                String studentText = studentIdField.getText().trim();

                if (bookText.isEmpty() || studentText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Both Book ID and Student ID are required.");
                    return;
                }

                int bookId = Integer.parseInt(bookText);
                int studentId = Integer.parseInt(studentText);

                boolean success = transactionService.returnBook(bookId, studentId);

                if (success) {
                    JOptionPane.showMessageDialog(this, " Book returned successfully!");
                    dashboard.refreshBookList();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, " Return failed. Check if book was issued to this student.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric IDs.");
            }
        });

        setVisible(true);
    }
}
