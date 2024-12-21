import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Task2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Processing random sequence...");
        processRandomSequence();
    }

    private static void processRandomSequence() throws ExecutionException, InterruptedException {
        int n = 20;
        List<Double> sequence = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            sequence.add(random.nextDouble() * 10);
        }

        CompletableFuture.runAsync(() -> System.out.println("Generated sequence: " + sequence)).get();

        long startTime = System.currentTimeMillis();

        CompletableFuture<Double> resultFuture = CompletableFuture.supplyAsync(() -> computeSum(sequence))
                .thenApplyAsync(result -> {
                    long duration = System.currentTimeMillis() - startTime;
                    System.out.println("Computation completed in: " + duration + "ms");
                    return result;
                });

        double result = resultFuture.get();

        CompletableFuture.runAsync(() -> System.out.println("Final result: " + result)).get();
    }

    private static double computeSum(List<Double> sequence) {
        double sum = 0;
        for (int i = 0; i < sequence.size() - 1; i++) {
            sum += sequence.get(i) * sequence.get(i + 1);
        }
        return sum;
    }
}
