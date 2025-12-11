package ru.siganov.hot_wallet.mydifficulttask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComplexTaskExecutor {

    ExecutorService executorService;
    CyclicBarrier cyclicBarrier;
    List<Integer> tasks = Collections.synchronizedList(new ArrayList<>());

    public ComplexTaskExecutor(int threads) {
        executorService = Executors.newFixedThreadPool(threads);
        cyclicBarrier = new CyclicBarrier(threads, () -> {
            int sum = tasks.stream().mapToInt(Integer::intValue).sum();
            System.out.println("sum: " + sum);
        });
    }

    public void executeTasks(int countOfTasks) {
        for (int i = 0; i < countOfTasks; i++) {
            int taskId = i;
            executorService.submit(() -> {
                ComplexTask task = new ComplexTask(taskId);
                synchronized (tasks) {
                    tasks.add(task.execute());
                }
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println(Thread.currentThread().getName() + " finished." + tasks);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
