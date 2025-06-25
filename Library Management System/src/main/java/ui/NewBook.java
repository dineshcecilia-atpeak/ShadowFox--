package ui;

import service.BookService;
import util.BookAPI;

import javax.swing.*;
import java.awt.*;

public class NewBook extends JFrame {
    private final BookService bookService = new BookService();
    private final DashboardFrame dashboard;

    public NewBook(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        setTitle("➕ Add New Book");
        setSize(400, 250);
        setLayout(new GridLayout(5, 2, 10, 10));
        setLocationRelativeTo(null); // Center the window

        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField();
        JButton searchBtn = new JButton("Search");

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();

        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();

        JButton addBtn = new JButton("➕ Add");

        // Search ISBN button logic
        searchBtn.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            if (!isbn.isEmpty()) {
                String[] details = BookAPI.getBookDetailsByISBN(isbn);
                if (details != null) {
                    titleField.setText(details[0]); // Title
                    authorField.setText(details[1]); // Author
                } else {
                    JOptionPane.showMessageDialog(this, " Book not found.");
                }
            }
        });

        // Add book to DB
        addBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            if (!title.isEmpty() && !author.isEmpty()) {
                bookService.addBook(title, author);
                dashboard.refreshBookList();
                JOptionPane.showMessageDialog(this, "Book added successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter all fields.");
            }
        });

        add(isbnLabel);
        add(isbnField);
        add(new JLabel()); // empty
        add(searchBtn);
        add(titleLabel);
        add(titleField);
        add(authorLabel);
        add(authorField);
        add(new JLabel()); // empty
        add(addBtn);

        setVisible(true);
    }
}
