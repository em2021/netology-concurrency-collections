import java.util.Random;
import java.util.concurrent.*;

public class Main {

    final static BlockingQueue<String> aQueue = new ArrayBlockingQueue<>(100);
    final static BlockingQueue<String> bQueue = new ArrayBlockingQueue<>(100);
    final static BlockingQueue<String> cQueue = new ArrayBlockingQueue<>(100);
    final static int numberOfTextsToAnalyze = 10_000;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Thread queueWriterThread = new Thread(() -> {
            System.out.println("queueWriter thread started...");
            for (int i = 0; i < numberOfTextsToAnalyze; i++) {
                try {
                    aQueue.put(generateText("abc", 100_000));
                    bQueue.put(generateText("abc", 100_000));
                    cQueue.put(generateText("abc", 100_000));
                } catch (InterruptedException ie) {
                    System.out.println("queueWriter thread interrupted.");
                }
                if (i > 0 && i % 1000 == 0) {
                    System.out.println("Records added to queues: " + i);
                }
            }
            System.out.println("Total records added to queues: " + numberOfTextsToAnalyze);
        });
        queueWriterThread.start();
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<?> aQueueAnalyzerFuture = threadPool
                .submit(new MaxLetterRepetitionsAnalyzer(queueWriterThread, aQueue, 'a'));
        Future<?> bQueueAnalyzerFuture = threadPool
                .submit(new MaxLetterRepetitionsAnalyzer(queueWriterThread, bQueue, 'b'));
        Future<?> cQueueAnalyzerFuture = threadPool
                .submit(new MaxLetterRepetitionsAnalyzer(queueWriterThread, cQueue, 'c'));
        threadPool.shutdown();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}