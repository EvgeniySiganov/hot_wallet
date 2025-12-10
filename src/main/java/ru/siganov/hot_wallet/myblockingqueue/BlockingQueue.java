package ru.siganov.hot_wallet.myblockingqueue;


import java.util.Arrays;

public class BlockingQueue<T> {

    private final Object [] queue;
    private int enIndex;
    private int deIndex;
    private int size;

    public BlockingQueue(int capacity) {
        this.queue = new Object[capacity];
    }

    public synchronized void enqueue(T value) throws InterruptedException {
        while (size == queue.length) {
            wait();
        }
        queue[enIndex++] = value;
        size++;
        if (enIndex == queue.length) {
            enIndex = 0;
        }
        notifyAll();
    }

    @SuppressWarnings("unchecked")
    public synchronized T dequeue() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        T result = (T) queue[deIndex];
        queue[deIndex] = null;
        size--;
        deIndex++;
        if (deIndex == queue.length) {
            deIndex = 0;
        }
        notifyAll();
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(queue);
    }

    public synchronized int size() {
        return size;
    }
}
