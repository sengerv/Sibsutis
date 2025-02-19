package org.example;

public class WorkerThread implements Runnable{
    private  int IdTread;
    private  int LengthTread;
    private  ProgressBar progressBar;
    private  TimeTracker timeTracker;
    private  double result;
    private boolean isFinished = false;

    public WorkerThread( int IdTread, int LengthTread, ProgressBar progressBar, TimeTracker timeTracker){
        this.IdTread = IdTread;
        this.LengthTread = LengthTread;
        this.progressBar = progressBar;
        this.timeTracker = timeTracker;
    }

    @Override
    public void run() {
        timeTracker.start();
        for (int i = 0; i < LengthTread; i++) {
            result += Math.sqrt(i);
            progressBar.updateProgress(1);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Thread " + IdTread + " interrupted: " + e.getMessage());
            }
        }
        synchronized (System.out) {
            progressBar.completeProgress();
        }
        timeTracker.stop();
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public int getIdTread() {
        return IdTread;
    }

    public double getResult() {
        return result;
    }

    public TimeTracker getTimeTracker() {
        return timeTracker;
    }
}
