import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class InventoryManager extends JFrame {
    private JTextField nameField, quantityField, priceField;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel totalValueLabel;

    public InventoryManager() {
        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null);

        // Input panel for form
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));

        // Fields panel
        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        nameField = new JTextField(10);
        quantityField = new JTextField(5);
        priceField = new JTextField(5);

        fieldsPanel.add(new JLabel("Item Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Quantity:"));
        fieldsPanel.add(quantityField);
        fieldsPanel.add(new JLabel("Price:"));
        fieldsPanel.add(priceField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        inputPanel.add(fieldsPanel);
        inputPanel.add(buttonPanel);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Item Name", "Quantity", "Price"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Total value label
        totalValueLabel = new JLabel("Total Inventory Value: ₹0.00");
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        valuePanel.add(totalValueLabel);

        // Event listeners
        addButton.addActionListener(e -> addItem());
        updateButton.addActionListener(e -> updateItem());
        deleteButton.addActionListener(e -> deleteItem());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                nameField.setText(tableModel.getValueAt(row, 0).toString());
                quantityField.setText(tableModel.getValueAt(row, 1).toString());
                priceField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });

        // Layout setup
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(valuePanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addItem() {
        String name = nameField.getText();
        String quantityText = quantityField.getText();
        String priceText = priceField.getText();

        if (name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!isNumeric(quantityText) || !isNumeric(priceText)) {
            JOptionPane.showMessageDialog(this, "Quantity and Price must be numeric.");
            return;
        }

        tableModel.addRow(new Object[]{name, quantityText, priceText});
        clearFields();
        updateTotalValue();
    }

    private void updateItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to update.");
            return;
        }

        String name = nameField.getText();
        String quantityText = quantityField.getText();
        String priceText = priceField.getText();

        if (name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!isNumeric(quantityText) || !isNumeric(priceText)) {
            JOptionPane.showMessageDialog(this, "Quantity and Price must be numeric.");
            return;
        }

        tableModel.setValueAt(name, row, 0);
        tableModel.setValueAt(quantityText, row, 1);
        tableModel.setValueAt(priceText, row, 2);
        clearFields();
        updateTotalValue();
    }

    private void deleteItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to delete.");
            return;
        }
        tableModel.removeRow(row);
        clearFields();
        updateTotalValue();
    }

    private void clearFields() {
        nameField.setText("");
        quantityField.setText("");
        priceField.setText("");
        table.clearSelection();
    }

    private void updateTotalValue() {
        double total = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int qty = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
            double price = Double.parseDouble(tableModel.getValueAt(i, 2).toString());
            total += qty * price;
        }
        totalValueLabel.setText(String.format("Total Inventory Value: ₹%.2f", total));
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryManager());
    }
}
