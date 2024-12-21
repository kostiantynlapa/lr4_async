import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Task1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Processing text files...");
        processTextFiles();
    }

    private static void processTextFiles() throws ExecutionException, InterruptedException {
        String[] filePaths = {"file1.txt", "file2.txt", "file3.txt"}; // Replace with actual file paths
        List<CompletableFuture<String>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (String filePath : filePaths) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> readFile(filePath))
                    .thenApplyAsync(Task1::removeLetters);

            futures.add(future);
        }

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRunAsync(() -> {
                    long duration = System.currentTimeMillis() - startTime;
                    System.out.println("All text files processed in: " + duration + "ms");
                });

        List<String> results = new ArrayList<>();
        for (CompletableFuture<String> future : futures) {
            results.add(future.get());
        }

        CompletableFuture.runAsync(() -> System.out.println("Processed results: " + results)).get();
        allDone.get();
    }

    private static String readFile(String filePath) {
        long startTime = System.currentTimeMillis();
        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("File " + filePath + " read in: " + duration + "ms");
            return content;
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + filePath, e);
        }
    }

    private static String removeLetters(String text) {
        long startTime = System.currentTimeMillis();
        String result = text.replaceAll("[a-zA-Z]", "");
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Letters removed in: " + duration + "ms");
        return result;
    }
}
