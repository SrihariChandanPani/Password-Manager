import javax.crypto.SecretKey;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PasswordManager {
    private static final String PASSWORDS_FILE = "passwords.txt";
    private SecretKey encryptionKey;
    private Map<String, String> passwords;

    public PasswordManager(SecretKey encryptionKey) {
        this.encryptionKey = encryptionKey;
        passwords = new HashMap<>();
        loadPasswords();
    }

    private void loadPasswords() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    passwords.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading passwords: " + e.getMessage());
        }
    }

    public void addPassword(String account, String password) {
        try {
            String encryptedPassword = EncryptionUtil.encrypt(password, encryptionKey);
            passwords.put(account, encryptedPassword);
            savePasswords();
        } catch (Exception e) {
            System.out.println("Error encrypting password: " + e.getMessage());
        }
    }

    public String getPassword(String account) {
        String encryptedPassword = passwords.get(account);
        if (encryptedPassword == null) {
            return null;
        }
        try {
            return EncryptionUtil.decrypt(encryptedPassword, encryptionKey);
        } catch (Exception e) {
            System.out.println("Error decrypting password: " + e.getMessage());
            return null;
        }
    }

    private void savePasswords() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PASSWORDS_FILE))) {
            for (Map.Entry<String, String> entry : passwords.entrySet()) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving passwords: " + e.getMessage());
        }
    }
  
}
