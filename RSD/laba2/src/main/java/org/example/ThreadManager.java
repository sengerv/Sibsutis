package org.example;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
    private final List<WorkerThread> threads;
    private final int amountThread;
    private final int taskLength;

    public ThreadManager(int amountThread, int taskLength){
        this.amountThread = amountThread;
        this.threads = new ArrayList<>();
        this.taskLength = taskLength;
    }

    public List<WorkerThread> getThreads() {
        return threads;
    }

    public void initializeThreads() {
        for (int i = 0; i < amountThread; i++) {
            ProgressBar progressBar = new ProgressBar(taskLength, '#');
            TimeTracker timeTracker = new TimeTracker();
            WorkerThread workerThread = new WorkerThread(i + 1, taskLength, progressBar, timeTracker);
            threads.add(workerThread);
        }
    }


    public void runThread(){
        for (WorkerThread thread : threads) {
            new Thread(thread::run).start();
        }
    }

    public void stopThread(){
        for (WorkerThread thread : threads) {
            try {
                while (!thread.isFinished()) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                System.out.println("Waiting interrupted: " + e.getMessage());
            }
        }
        System.out.println("All threads are completed!");
    }

    public void watcherProgress(){
        for (WorkerThread thread : threads) {
            System.out.println("Thread " + thread.getIdTread() + " completed. Result: " + thread.getResult() +
                    ", Lead time: " + thread.getTimeTracker().getElapsedTime() + " ms");
        }
    }
}
