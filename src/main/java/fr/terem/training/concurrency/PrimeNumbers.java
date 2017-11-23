package fr.terem.training.concurrency;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimeNumbers {
    public static void main(String[] args) throws Exception {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        final int threshold = 50_000_00;
        System.out.println(" Number of CPU available = "+Runtime.getRuntime().availableProcessors());

        final long start = System.currentTimeMillis();

        final List<Integer> candidates = IntStream.range(2, threshold).boxed().collect(Collectors.toList());
        //final List<Integer> primeNumbers = candidates.stream().filter(PrimeNumbers::isPrime).collect(Collectors.toList());
        final List primeNumbers = forkJoinPool.submit(() -> candidates.parallelStream().filter(PrimeNumbers::isPrime).collect(Collectors.toList())).get();



        System.out.println("Execution time: " + (System.currentTimeMillis() - start));

        System.out.println("Size: " + primeNumbers.size());
    }

    private static boolean isPrime(final int candidate) {
        for (int i = 2; i * i <= candidate; ++i) {
            if (candidate % i == 0) {
                return false;
            }
        }
        return true;
    }
}
