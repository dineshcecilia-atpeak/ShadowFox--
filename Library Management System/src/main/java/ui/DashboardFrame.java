package ui;

import model.Book;
import model.Student;
import service.BookService;
import service.StudentService;
import service.TransactionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final BookService bookService = new BookService();
    private final StudentService studentService = new StudentService();
    private final TransactionService transactionService = new TransactionService();
    private final JTabbedPane tabbedPane;

    public DashboardFrame(String username) {
        setTitle(" Library Dashboard - Welcome " + username);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔁 Tabbed pane setup
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // 📥 Buttons panel (bottom)
        JPanel bottomPanel = new JPanel();

        JButton newBookBtn = new JButton("➕ New Book");
        newBookBtn.addActionListener(e -> new NewBook(this)); // passes DashboardFrame to NewBook

        JButton newStudentBtn = new JButton(" New Student");
        newStudentBtn.addActionListener(e -> new NewStudent(this));

        JButton issueBookBtn = new JButton(" Issue Book");
        issueBookBtn.addActionListener(e -> new IssueBook(this));


        JButton returnBookBtn = new JButton(" Return Book");
        returnBookBtn.addActionListener(e -> new ReturnBook(this));

        JButton logoutBtn = new JButton(" Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        bottomPanel.add(newBookBtn);
        bottomPanel.add(newStudentBtn);
        bottomPanel.add(issueBookBtn);
        bottomPanel.add(returnBookBtn);
        bottomPanel.add(logoutBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // 🔄 Load all tabs
        refreshBookList();

        setVisible(true);
    }

    public void refreshBookList() {
        tabbedPane.removeAll();

        tabbedPane.addTab("Books", getBooksPanel());
        tabbedPane.addTab("Students", getStudentsPanel());
        tabbedPane.addTab("Issue Details", getIssueDetailsPanel());
        tabbedPane.addTab("Return Details", getReturnDetailsPanel());

        revalidate();
        repaint();
    }

    private JPanel getBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        List<Book> books = bookService.getBooks();

        String[] columns = {"ID", "Title", "Author", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Book b : books) {
            model.addRow(new Object[]{
                    b.id, b.title, b.author, b.is_borrowed ? "Borrowed" : "Available"
            });
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        List<Student> students = studentService.getAllStudents();

        String[] columns = {"Student ID", "Name", "Roll"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Student s : students) {
            model.addRow(new Object[]{s.id, s.name, s.roll});
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getIssueDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Student ID", "Name", "Book ID", "Book Name", "Issue Date", "Due Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        transactionService.getIssueDetails().forEach(model::addRow);

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getReturnDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Student ID", "Name", "Book ID", "Book Name", "Issue Date", "Due Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        transactionService.getReturnDetails().forEach(model::addRow);

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
