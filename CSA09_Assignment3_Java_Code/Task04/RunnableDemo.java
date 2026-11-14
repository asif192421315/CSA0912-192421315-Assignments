class ScanTask implements Runnable {
    private final int scripts;
    private final String station;

    ScanTask(String station, int scripts) {
        this.station = station;
        this.scripts = scripts;
    }

    @Override
    public void run() {
        for (int i = 1; i <= scripts; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.println(station + " scanned " + scripts + " scripts.");
    }
}

public class RunnableDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = new Thread[4];
        for (int i = 0; i < stations.length; i++) {
            stations[i] = new Thread(new ScanTask("Station " + (i + 1), 5));
            stations[i].start();
        }
        for (Thread station : stations) station.join();
        System.out.println("All stations completed.");
    }
}
