import java.io.*;

public class SafeReport {
    public static void main(String[] args) {
        try (
            BufferedReader reader = new BufferedReader(new FileReader("store1.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("report-safe.txt"))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Safe report generated.");
        } catch (IOException e) {
            System.out.println("Safe report error: " + e.getMessage());
            for (Throwable t : e.getSuppressed())
                System.out.println("Suppressed: " + t.getMessage());
        }
    }
}
