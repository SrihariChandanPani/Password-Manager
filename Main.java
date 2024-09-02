
    
import javax.crypto.SecretKey;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserAuthentication auth = new UserAuthentication();

        // User Authentication
        System.out.println("Welcome to the Password Manager!");
        System.out.print("Do you have an account? (yes/no): ");
        String hasAccount = scanner.nextLine();

        if (hasAccount.equalsIgnoreCase("no")) {
            System.out.print("Register a new username: ");
            String username = scanner.nextLine();
            System.out.print("Create a password: ");
            String password = scanner.nextLine();
            if (auth.registerUser(username, password)) {
                System.out.println("Registration successful! Please log in.");
            } else {
                System.out.println("Username already exists. Try logging in.");
            }
        }

        // Login
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            if (auth.loginUser(username, password)) {
                System.out.println("Login successful!");
                loggedIn = true;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }

        // Password Manager
        try {
            SecretKey key = EncryptionUtil.generateKey();
            PasswordManager pm = new PasswordManager(key);

            while (true) {
                System.out.println("\n1. Add Password");
                System.out.println("2. Retrieve Password");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                if (choice == 1) {
                    System.out.print("Enter account name: ");
                    String account = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    pm.addPassword(account, password);
                    System.out.println("Password saved successfully.");
                } else if (choice == 2) {
                    System.out.print("Enter account name: ");
                    String account = scanner.nextLine();
                    String retrievedPassword = pm.getPassword(account);
                    if (retrievedPassword != null) {
                        System.out.println("Password for " + account + ": " + retrievedPassword);
                    } else {
                        System.out.println("No password found for " + account);
                    }
                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        scanner.close();
    }
}
