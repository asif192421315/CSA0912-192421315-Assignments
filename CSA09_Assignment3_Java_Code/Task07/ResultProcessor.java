
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ResultProcessor {

    private static final int TOTAL_FILES = 10000;

    // Stores results safely when multiple threads access it.
    private static final ConcurrentLinkedQueue<ProcessingResult> results =
            new ConcurrentLinkedQueue<>();

    // Stores one processing result.
    static class ProcessingResult {
        String fileName;
        boolean success;
        String message;

        ProcessingResult(String fileName, boolean success, String message) {
            this.fileName = fileName;
            this.success = success;
            this.message = message;
        }
    }

    // Callable task for processing one student's result file.
    static class ResultTask implements Callable<ProcessingResult> {

        private final Path filePath;

        ResultTask(Path filePath) {
            this.filePath = filePath;
        }

        @Override
        public ProcessingResult call() {

            String fileName = filePath.getFileName().toString();

            try {
                // Read file content.
                String content = Files.readString(filePath).trim();

                if (content.isEmpty()) {
                    throw new IllegalArgumentException("Empty file");
                }

                // Expected format: StudentID,Mark1,Mark2,Mark3
                String[] data = content.split(",");

                if (data.length != 4) {
                    throw new IllegalArgumentException(
                            "Invalid file format"
                    );
                }

                String studentId = data[0].trim();

                if (studentId.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Student ID is missing"
                    );
                }

                int mark1 = Integer.parseInt(data[1].trim());
                int mark2 = Integer.parseInt(data[2].trim());
                int mark3 = Integer.parseInt(data[3].trim());

                // Validate marks.
                if (!isValidMark(mark1) ||
                    !isValidMark(mark2) ||
                    !isValidMark(mark3)) {

                    throw new IllegalArgumentException(
                            "Marks must be between 0 and 100"
                    );
                }

                int total = mark1 + mark2 + mark3;
                double average = total / 3.0;

                String message = String.format(
                        "Student: %s | Total: %d | Average: %.2f",
                        studentId, total, average
                );

                ProcessingResult result =
                        new ProcessingResult(fileName, true, message);

                results.add(result);
                return result;

            } catch (IOException | IllegalArgumentException e) {

                // A failure in one file does not stop other tasks.
                ProcessingResult result =
                        new ProcessingResult(
                                fileName,
                                false,
                                e.getMessage()
                        );

                results.add(result);
                return result;
            }
        }

        private boolean isValidMark(int mark) {
            return mark >= 0 && mark <= 100;
        }
    }

    public static void main(String[] args) {

        int poolSize = 4;
        Path folder = Path.of("scores");
        int fileCount = TOTAL_FILES;

        // Optional command-line arguments:
        // java ResultProcessor 4 scores 10000
        try {
            if (args.length >= 1) {
                poolSize = Integer.parseInt(args[0]);
            }

            if (args.length >= 2) {
                folder = Path.of(args[1]);
            }

            if (args.length >= 3) {
                fileCount = Integer.parseInt(args[2]);
            }

            if (poolSize <= 0 || fileCount < 0) {
                throw new IllegalArgumentException(
                        "Pool size must be positive and file count non-negative"
                );
            }

        } catch (RuntimeException e) {
            System.out.println("Invalid arguments: " + e.getMessage());
            return;
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(poolSize);

        List<Future<ProcessingResult>> futures =
                new ArrayList<>();

        long startTime = System.currentTimeMillis();

        try {
            // Submit one task for each expected student file.
            for (int i = 1; i <= fileCount; i++) {

                Path file = folder.resolve("student" + i + ".txt");

                Callable<ProcessingResult> task =
                        new ResultTask(file);

                Future<ProcessingResult> future =
                        executor.submit(task);

                futures.add(future);
            }

            // Wait for every task and check unexpected task failures.
            for (int i = 0; i < futures.size(); i++) {

                try {
                    futures.get(i).get();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Processing interrupted.");
                    break;

                } catch (ExecutionException e) {

                    // Handles unexpected exceptions outside the task.
                    String fileName = "student" + (i + 1) + ".txt";

                    results.add(
                            new ProcessingResult(
                                    fileName,
                                    false,
                                    "Unexpected error: " +
                                    e.getCause()
                            )
                    );
                }
            }

        } finally {
            executor.shutdown();
        }

        long endTime = System.currentTimeMillis();

        // Generate final summary.
        int successCount = 0;
        int failureCount = 0;

        System.out.println("\n===== PROCESSING SUMMARY =====");

        for (ProcessingResult result : results) {

            if (result.success) {
                successCount++;
            } else {
                failureCount++;
                System.out.println(
                        "FAILED: " + result.fileName +
                        " | Reason: " + result.message
                );
            }
        }

        System.out.println("\nTotal files expected: " + fileCount);
        System.out.println("Successful files: " + successCount);
        System.out.println("Failed files: " + failureCount);
        System.out.println(
                "Total processed: " +
                (successCount + failureCount)
        );

        System.out.println(
                "Execution time: " +
                (endTime - startTime) + " ms"
        );

        System.out.println("Thread pool size: " + poolSize);
        System.out.println("==============================");
    }
}
