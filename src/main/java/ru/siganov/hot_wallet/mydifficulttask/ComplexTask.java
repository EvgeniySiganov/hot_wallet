package ru.siganov.hot_wallet.mydifficulttask;

public class ComplexTask {
    private int taskId;

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    public int execute() {
        for (int i = 0; i < 100; i++) {
            taskId = taskId + i % 7;
        }
        return taskId;
    }
}
