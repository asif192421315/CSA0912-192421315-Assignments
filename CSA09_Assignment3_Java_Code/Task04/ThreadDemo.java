class ScannerStation extends Thread {
    private final int scripts;

    ScannerStation(String name, int scripts) {
        super(name);
        this.scripts = scripts;
    }

    @Override
    public void run() {
        int scanned = 0;
        for (int i = 1; i <= scripts; i++) {
            scanned++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.println(getName() + " scanned " + scanned + " scripts.");
    }
}

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        ScannerStation[] stations = {
            new ScannerStation("Station 1", 5),
            new ScannerStation("Station 2", 5),
            new ScannerStation("Station 3", 5),
            new ScannerStation("Station 4", 5)
        };
        for (ScannerStation station : stations) station.start();
        for (ScannerStation station : stations) station.join();
        System.out.println("All stations completed.");
    }
}
