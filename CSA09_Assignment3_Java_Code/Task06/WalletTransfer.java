public class WalletTransfer {
    static class Wallet {
        final int id;
        int balance;
        Wallet(int id, int balance) { this.id = id; this.balance = balance; }
    }

    static void transfer(Wallet from, Wallet to, int amount) {
        Wallet first = from.id < to.id ? from : to;
        Wallet second = from.id < to.id ? to : from;

        synchronized (first) {
            synchronized (second) {
                if (from.balance < amount)
                    throw new IllegalArgumentException("Insufficient wallet balance");
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }

    public static void main(String[] args) {
        Wallet a = new Wallet(1, 1000);
        Wallet b = new Wallet(2, 500);
        transfer(a, b, 200);
        System.out.println("Wallet A: " + a.balance);
        System.out.println("Wallet B: " + b.balance);
    }
}
