package netology;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Main {
    private static final BlockingQueue<String> queueA = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> queueB = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> queueC = new LinkedBlockingQueue<>();
    public static void main(String[] args) {
        Thread generatorThread = new Thread(Main::generateTexts);
        Thread countAThread = new Thread(() -> countCharacters(queueA, 'a'));
        Thread countBThread = new Thread(() -> countCharacters(queueB, 'b'));
        Thread countCThread = new Thread(() -> countCharacters(queueC, 'c'));

        generatorThread.start();
        countAThread.start();
        countBThread.start();
        countCThread.start();

        try {
            generatorThread.join();
            countAThread.join();
            countBThread.join();
            countCThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Максимальное количество символов 'a': " + getMaxCharacterCount('a'));
        System.out.println("Максимальное количество символов 'b': " + getMaxCharacterCount('b'));
        System.out.println("Максимальное количество символов 'c': " + getMaxCharacterCount('c'));
    }

    public static void generateTexts() {
        String letters = "abc";
        Random random = new Random();

        for (int i = 0; i < 100_000; i++) {
            int length = 3 + random.nextInt(3);
            StringBuilder text = new StringBuilder();

            for (int j = 0; j < length; j++) {
                text.append(letters.charAt(random.nextInt(letters.length())));
            }

            try {
                if (text.indexOf("a") >= 0) {
                    queueA.put(text.toString());
                }
                if (text.indexOf("b") >= 0) {
                    queueB.put(text.toString());
                }
                if (text.indexOf("c") >= 0) {
                    queueC.put(text.toString());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void countCharacters(BlockingQueue<String> queue, char character) {
        int maxCount = 0;

        while (!queue.isEmpty()) {
            String text = queue.poll();

            if (text != null) {
                int count = 0;

                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == character) {
                        count++;
                    }
                }

                if (count > maxCount) {
                    maxCount = count;
                }
            }
        }

        System.out.println("Максимальное количество символов '" + character + "': " + maxCount);
    }

    public static int getMaxCharacterCount(char character) {
        int maxCount = 0;

        switch (character) {
            case 'a':
                maxCount = queueA.size();
                break;
            case 'b':
                maxCount = queueB.size();
                break;
            case 'c':
                maxCount = queueC.size();
                break;
        }

        return maxCount;
    }
}
