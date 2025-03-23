public class ScoreManager {
    private int totalScore;
    private int currentScore;
    private long levelStartTime;
    private boolean isDarkMode = false;

    public ScoreManager() {
        totalScore = 0;
        currentScore = 0;
    }

    public void startLevelTimer() {
        levelStartTime = System.currentTimeMillis();
        // Don't reset current level score here
    }

    public int calculateLevelScore() {
        long timeSpent = (System.currentTimeMillis() - levelStartTime) / 1000;
        int timeBonus = Math.max(0, 500 - (int)(timeSpent * 10)); // Time bonus reduced by 10 points per second
        
        // Add time bonus to current level score
        int levelScore = currentScore + timeBonus;
        
        // Add the level score to total score
        totalScore += levelScore;
        
        System.out.println("Level completed! Current level score: " + currentScore + 
                         " + Time bonus: " + timeBonus + " = Total level score: " + levelScore);
        System.out.println("Overall total score: " + totalScore);
        
        // Reset current level score for the next level
        currentScore = 0;
        
        return levelScore;
    }
    
    // This method gets the current total score without adding time bonus
    // Used specifically for game over when player dies
    public int getFinalScore() {
        // For game over, just add current score to total without time bonus
        return totalScore + currentScore;
    }
    
    public void addScore(int points) {
        currentScore += points;
        
        // Ensure score doesn't go negative
        if (currentScore < 0) {
            currentScore = 0;
        }
    }
    
    public void applyDamagePenalty() {
        // If player has 5 or more coins, take 5 points
        if (currentScore >= 5) {
            currentScore -= 5;
            System.out.println("Applied 5-point damage penalty. New score: " + currentScore);
        } else {
            // If less than 5 coins, take whatever is available
            System.out.println("Not enough coins for damage penalty. Current score: " + currentScore);
            currentScore = 0;
        }
    }

    public int getTotalScore() { 
        return totalScore + currentScore; // Include current level score in total
    }
    
    public int getCurrentScore() { 
        return currentScore; 
    }
    
    public void setCurrentScore(int score) {
        this.currentScore = score;
    }
    
    public void resetScore() {
        totalScore = 0;
        currentScore = 0;
    }
    
    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
    }
    
    public boolean isDarkMode() {
        return isDarkMode;
    }
}