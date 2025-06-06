import javax.swing.*; //Jframe
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ContactGUI extends JFrame{
    private ArrayList<Contact> contacts= new ArrayList<>(); //creating a list called contacts that will hold many Contact objects.
    private DefaultListModel<String>listModel= new DefaultListModel<>(); //GUI list that stores strings
    private JList<String> contactList= new JList<>(listModel); //the contacts in a list form using the strings stored in listModel
    private JTextField nameField= new JTextField(15);
    private JTextField phoneField= new JTextField(15);
    private JTextField emailField= new JTextField(15);

    public ContactGUI(){
        setTitle("Contact Management System");
        setSize(500,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //to open in centre

        //bg colour
        getContentPane().setBackground(Color.decode("#FFF4E6"));

        //padding
        JPanel container= new JPanel(new BorderLayout(10,10));
        container.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        container.setBackground(Color.decode("#FFF4E6"));

        //input panel
        JPanel inputPanel= new JPanel();
        inputPanel.setLayout(new GridLayout(4,2,8,8));
        inputPanel.setBackground(Color.decode("#FFE7C7"));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //Labels
        JLabel nameLabel= new JLabel("Name");
        nameLabel.setForeground(Color.decode("#6B4226"));
        JLabel phoneLabel= new JLabel("Phone number");
        phoneLabel.setForeground(Color.decode("#6B4226"));
        JLabel emailLabel= new JLabel("Email");
        emailLabel.setForeground(Color.decode("#6B4226"));

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);

        nameField.setBackground(Color.WHITE);
        phoneField.setBackground(Color.WHITE);
        emailField.setBackground(Color.WHITE);

        JButton addBtn= new JButton("Add");
        addBtn.setBackground(Color.decode("#FF6B6B"));
        addBtn.setForeground(Color.WHITE);
        addBtn.setOpaque(true);
        addBtn.setBorderPainted(false);

        JButton deleteBtn= new JButton("Delete");
        deleteBtn.setBackground(Color.decode("#FFA500"));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setOpaque(true);
        deleteBtn.setBorderPainted(false);

        JButton updateBtn= new JButton("Update");
        updateBtn.setBackground(Color.decode("#FFA500"));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setOpaque(true);
        updateBtn.setBorderPainted(false);

        inputPanel.add(addBtn);
        inputPanel.add(deleteBtn);

        //List Panel
        JScrollPane listScroll= new JScrollPane(contactList);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.setBackground(Color.WHITE);

        // add components to container
        container.add(inputPanel,BorderLayout.NORTH);
        container.add(listScroll,BorderLayout.CENTER);
        container.add(updateBtn, BorderLayout.SOUTH);

        add(container);

        //Action
        addBtn.addActionListener(e -> addContact());
        deleteBtn.addActionListener(e -> deleteContact());
        updateBtn.addActionListener(e -> updateContact());

        contactList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt){
                int index= contactList.getSelectedIndex();
                if (index>=0){
                    Contact c= contacts.get(index);
                    nameField.setText(c.name);
                    phoneField.setText(c.phone);
                    emailField.setText(c.email);
                }
            }
        });

    }

    // condition to check validity

    private boolean isValidEmail(String email){
        String emailRegex="^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$";
        return Pattern.matches(emailRegex,email.toLowerCase());
    }
    private boolean isValidPhoneNumber(String phone){
        return phone.matches("\\d{10}");
    }

    private void addContact(){
        String name= nameField.getText().trim();
        String phone= phoneField.getText().trim();
        String email=emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(this,"All fields are required.");
            return;
        }

        if(!isValidPhoneNumber(phone)){
            JOptionPane.showMessageDialog(this,"Phone number must be exactly 10 digits.");
            return;
        }

        if(!isValidEmail(email)){
            JOptionPane.showMessageDialog(this,"Email must be a valid format.");
            return;
        }

        Contact c= new Contact(name,phone,email);
        contacts.add(c);
        listModel.addElement(c.toString());
        clearFields();
    }

    private void deleteContact(){
        int index= contactList.getSelectedIndex();
        if(index>=0){
            contacts.remove(index);
            listModel.remove(index);
            clearFields();
        }else{
            JOptionPane.showMessageDialog(this,"Select a contact to delete.");
        }
    }

    private void updateContact(){
        int index=contactList.getSelectedIndex();
        if(index<0){
            JOptionPane.showMessageDialog(this, "Select a contact to update.");
            return;
        }

        String name=nameField.getText().trim();
        String phone=phoneField.getText().trim();
        String email=emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(this,"All fields are required.");
            return;
        }

        if(!isValidPhoneNumber(phone)){
            JOptionPane.showMessageDialog(this,"Phone number must be exactly 10 digits.");
            return;
        }

        if(!isValidEmail(email)){
            JOptionPane.showMessageDialog(this,"Email must be a valid format.");
            return;
        }

        Contact c= new Contact(name,phone,email);
        contacts.set(index,c);
        listModel.set(index,c.toString());
        clearFields();
    }

    private void clearFields(){
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        contactList.clearSelection();

    }


    //main function
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            ContactGUI window=new ContactGUI();
            window.setVisible(true);
        });
    }
}
