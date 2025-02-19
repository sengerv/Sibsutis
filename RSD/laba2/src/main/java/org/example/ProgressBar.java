package org.example;

public class ProgressBar {
    private final int MaxLength;
    private int progress;
    private final char progressChar;

    public ProgressBar(int MaxLength, char progressChar) {
        this.MaxLength = MaxLength;
        this.progress = 0;
        this.progressChar = progressChar;
    }

    public void updateProgress(int step) {
        progress = Math.min(progress + step, MaxLength);
        displayProgress();
    }

    public void displayProgress() {
        // Рассчитываем длину прогресс-бара как отношение текущего прогресса к максимальной длине
        int progressLength = (progress * 50) / MaxLength;

        // Убедимся, что progressLength не превышает 50
        progressLength = Math.min(progressLength, 50);

        StringBuilder progressBar = new StringBuilder("[");

        // Заполняем прогресс-бар символами прогресса
        for (int i = 0; i < progressLength; i++) {
            progressBar.append(progressChar);
        }

        // Оставшиеся пробелы до 50 символов
        for (int i = progressLength; i < 50; i++) {
            progressBar.append(" ");
        }

        progressBar.append("]");
        System.out.print("\r" + progressBar.toString());
    }

    public void completeProgress() {
        progress = MaxLength;
        displayProgress();
        System.out.println("Thread completed!");
    }

    public int getProgress() {
        return progress;
    }
}