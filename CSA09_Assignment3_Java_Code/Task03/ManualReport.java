import java.io.*;

public class ManualReport {
    public static void main(String[] args) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader("store1.txt"));
            writer = new BufferedWriter(new FileWriter("report-manual.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Manual report generated.");
        } catch (IOException e) {
            System.out.println("Manual report error: " + e.getMessage());
        } finally {
            if (reader != null) {
                try { reader.close(); }
                catch (IOException e) { System.out.println("Reader close error: " + e.getMessage()); }
            }
            if (writer != null) {
                try { writer.close(); }
                catch (IOException e) { System.out.println("Writer close error: " + e.getMessage()); }
            }
        }
    }
}
