import java.util.concurrent.BlockingQueue;

public class MaxLetterRepetitionsAnalyzer implements Runnable {

    final private Thread queueWriterThread;
    final private BlockingQueue<String> queue;
    final private char letter;

    public MaxLetterRepetitionsAnalyzer(Thread queueWriterThread, BlockingQueue<String> queue, char letter) {
        this.queueWriterThread = queueWriterThread;
        this.queue = queue;
        this.letter = letter;
    }

    @Override
    public void run() {
        System.out.printf("%s%s%s%n", "MaxLetterRepetitionsAnalyzer thread for letter '", letter, "' started...");
        long maxValue = 0;
        int lineCounter = 0;
        while (queueWriterThread.isAlive() || !queue.isEmpty()) {
            try {
                if (queue.peek() != null) {
                    String text = queue.take();
                    long a = text
                            .chars()
                            .filter(ch -> ch == letter)
                            .count();
                    lineCounter++;
                    if (a > maxValue) {
                        maxValue = a;
                    }
                }
            } catch (InterruptedException e) {
                System.out.printf("%s%s%s%n",
                        "MaxLetterRepetitionsAnalyzer thread for letter '", letter, "' interrupted...");
            }
        }
        System.out.printf("%s%s%s %d%n", "Total records analyzed from letter '", letter, "' queue: ", lineCounter);
        System.out.printf("%s%s%s %d%n", "Max number of letter '", letter, "' repetitions is:", maxValue);
    }
}
