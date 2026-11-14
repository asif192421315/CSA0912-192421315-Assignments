import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class InsufficientBalanceException extends RuntimeException {
    InsufficientBalanceException(String message) { super(message); }
}

public class ATM {
    private static double balance = 5000.0;

    static void withdraw(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (amount > balance)
            throw new InsufficientBalanceException("Insufficient account balance.");
        balance -= amount;
        System.out.println("Withdrawal successful.");
        System.out.println("Remaining balance: " + balance);
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter account number: ");
            String account = sc.nextLine();

            System.out.print("Enter withdrawal amount: ");
            int amount = Integer.parseInt(sc.nextLine());

            if (!new File("cardreader.txt").exists())
                throw new FileNotFoundException("Card reader file not found.");

            withdraw(amount);
        } catch (FileNotFoundException e) {
            System.out.println("File Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Input Error: Invalid numeric input.");
        } catch (InsufficientBalanceException e) {
            System.out.println("Transaction Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Input Error: " + e.getMessage());
        } finally {
            System.out.println("ATM transaction process completed.");
        }
    }
}
