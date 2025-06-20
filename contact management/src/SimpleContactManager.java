import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

class Contact {
    String name;
    String phone;
    String email;

    Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name + " | Phone: " + phone + " | Email: " + email;
    }
}

public class SimpleContactManager {
    private static final ArrayList<Contact> contacts = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=== Contact Manager ===");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Update Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addContact();
                case 2 -> viewContacts();
                case 3 -> updateContact();
                case 4 -> deleteContact();
                case 5 -> System.out.println("Exiting program.");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);
    }

    private static void addContact() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter phone (10 digits): ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            System.out.println("Phone number must be exactly 10 digits.");
            return;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format. Example: user@gmail.com, user@edu.in");
            return;
        }

        contacts.add(new Contact(name, phone, email));
        System.out.println("Contact added successfully.");
    }

    private static void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\n--- Contact List ---");
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
    }

    private static void updateContact() {
        viewContacts();
        if (contacts.isEmpty()) return;

        System.out.print("Enter contact number to update: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= contacts.size()) {
            System.out.println("Invalid contact number.");
            return;
        }

        System.out.print("Enter new name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter new phone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter new email: ");
        String email = scanner.nextLine().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            System.out.println("Phone number must be exactly 10 digits.");
            return;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        contacts.set(index, new Contact(name, phone, email));
        System.out.println("Contact updated successfully.");
    }

    private static void deleteContact() {
        viewContacts();
        if (contacts.isEmpty()) return;

        System.out.print("Enter contact number to delete: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= contacts.size()) {
            System.out.println("Invalid contact number.");
            return;
        }

        contacts.remove(index);
        System.out.println("Contact deleted successfully.");
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$";
        return Pattern.matches(emailRegex, email.toLowerCase());
    }

    private static boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }
}
