/**
 * Score Manager class that handles all scoring functionality in the game.
 * Tracks current level score, total score, and calculates time-based bonuses.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class ScoreManager {
    private int totalScore;
    private int currentScore;
    private long levelStartTime;
    private boolean isDarkMode = false;

    /**
     * Constructs a new ScoreManager with initial scores set to zero.
     */
    public ScoreManager() {
        totalScore = 0;
        currentScore = 0;
    }

    /**
     * Starts the level timer for calculating time-based bonuses.
     */
    public void startLevelTimer() {
        levelStartTime = System.currentTimeMillis();
        // Don't reset current level score here
    }

    /**
     * Calculates the final score for the current level including time bonus.
     * Adds the level score to the total score.
     * 
     * @return The final score for the current level.
     */
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
    
    /**
     * Gets the final score without time bonus.
     * Used specifically for game over when player dies.
     * 
     * @return The final score without time bonus.
     */
    public int getFinalScore() {
        // For game over, just add current score to total without time bonus
        return totalScore + currentScore;
    }
    
    /**
     * Adds points to the current score.
     * 
     * @param points The number of points to add.
     */
    public void addScore(int points) {
        currentScore += points;
        
        // Ensure score doesn't go negative
        if (currentScore < 0) {
            currentScore = 0;
        }
    }
    
<<<<<<< HEAD
    /**
     * Applies a score penalty when player takes damage.
     */
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
=======
>>>>>>> 1884543 (Initial commit - added project files)

    /**
     * Gets the total score including current level score.
     * 
     * @return The total score.
     */
    public int getTotalScore() { 
        return totalScore + currentScore; // Include current level score in total
    }
    
    /**
     * Gets the current level score.
     * 
     * @return The current level score.
     */
    public int getCurrentScore() { 
        return currentScore; 
    }
    
    /**
     * Sets the current level score.
     * 
     * @param score The score to set.
     */
    public void setCurrentScore(int score) {
        this.currentScore = score;
    }
    
    /**
     * Resets both current and total scores to zero.
     */
    public void resetScore() {
        totalScore = 0;
        currentScore = 0;
    }
    
    /**
     * Sets dark mode display setting.
     * 
     * @param darkMode True to enable dark mode, false otherwise.
     */
    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
    }
    
    /**
     * Checks if dark mode is enabled.
     * 
     * @return True if dark mode is enabled, false otherwise.
     */
    public boolean isDarkMode() {
        return isDarkMode;
    }
}