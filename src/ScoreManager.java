import javafx.animation.AnimationTimer;

public class ScoreManager {
    private int totalScore;
    private int currentScore;
    private long levelStartTime;
    private AnimationTimer scoreTimer;

    public ScoreManager() {
        totalScore = 0;
    }

    // Start the timer when a level begins
    public void startLevelTimer() {
        levelStartTime = System.currentTimeMillis();
        currentScore = 1000;

        // Ensure the old timer is stopped before starting a new one
        if (scoreTimer != null) {
            scoreTimer.stop();
        }

        // decrease score by 20 every second
        scoreTimer = new AnimationTimer() {
            private long lastUpdate = System.nanoTime();

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000) { // 1 second elapsed
                    currentScore = Math.max(0, currentScore - 20); // Prevent negative score
                    lastUpdate = now;
                }
            }
        };
        scoreTimer.start();
    }

    // Stop the timer and finalize the score
    public int finalizeLevelScore() {
        if (scoreTimer != null) {
            scoreTimer.stop();
        }
        totalScore += currentScore; // Add to total score
        return currentScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }
}
