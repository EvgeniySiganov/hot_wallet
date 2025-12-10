package ru.siganov.hot_wallet.myblockingqueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int size = 8;
        BlockingQueue<String> queue = new BlockingQueue<>(size);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 2; i++) {
            executorService.execute(() -> {
                int ii = 0;
                while (true) {
                    try {
                        queue.enqueue(Thread.currentThread().getName() + ": " + ii++);
                        Thread.sleep(100);
                        System.out.println(queue);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        for (int i = 0; i < 2; i++) {
            executorService.execute(() -> {
                while (true) {
                    try {
                        System.out.println("consume: " + queue.dequeue());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
