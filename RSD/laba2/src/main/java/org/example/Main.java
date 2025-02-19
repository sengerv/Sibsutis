package org.example;

public class Main {
    public static void main(String[] args) {

        int numThreads = 5;
        int taskLength = 100;

        ThreadManager threadManager = new ThreadManager(numThreads, taskLength);
        threadManager.initializeThreads();
        threadManager.runThread();
        threadManager.stopThread();
        threadManager.watcherProgress();
    }
}