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
    List<Integer> tasks = Collections.synchronizedList(new ArrayList<>());

    public ComplexTaskExecutor(int threads) {
        executorService = Executors.newFixedThreadPool(threads);

    }

    public void executeTasks(int countOfTasks) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(countOfTasks, () -> {
            int sum = tasks.stream().mapToInt(Integer::intValue).sum();
            System.out.println("sum: " + sum);
        });
        for (int i = 0; i < countOfTasks; i++) {
            int taskId = i;
            executorService.submit(() -> {
                ComplexTask task = new ComplexTask(taskId);
                tasks.add(task.execute());
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
