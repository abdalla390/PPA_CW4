import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Core game engine that manages the game loop, level progression, and scoring.
 * Responsible for coordinating all game systems and tracking game state.
 * 
 * @author Hussain Bin Ali Bin Hussain Albeshri
 * @version 1.0
 */

public class GameEngine {
    private Player player;
    private Level currentLevel;
    private UIManager uiManager;
    
    private boolean isRunning;
    private boolean isGameOver;
    
    private int score;
    private long levelStartTime;
    
    //TENTATIVE!
    private static final int MAX_LEVEL = 10; //Hell nah!
    private static final int MAX_LIVES = 3;
    private static final int POINTS_FAST = 9; //Fast completion time
    private static final int POINTS_MEDIUM = 7; //Medium completion time
    private static final int POINTS_SLOW = 1; //Slow completion time
    
    //Time thresholds
    private static final long FAST_TIME = 5000;  // 5 second
    private static final long MEDIUM_TIME = 15000;  // 15 seconds
    
    //Animation stuff (i don't know how it'll work yet yet!)
    private AnimationTimer gameLoop;
    private GraphicsContext gc;
    
    /**
     * Constructor initializes the game engine with required components
     * 
     * @param uiManager The UI manager for updating display elements
     * @param gc GraphicsContext for rendering
     */
    
    public GameEngine(UIManager uiManager, GraphicsContext gc){
        
        this.uiManager = uiManager;
        this.gc = gc;
        this.isRunning = false;
        this.isGameOver = false;
        this.score = 0;
    
    }
    
    /**
     * Initializes game components and starts the game
     */
    public void intializeGame(){
        
        //New player and first level
        player = new Player(); //(null, null); //x,y coordinates are the starting coords of the player based on our design
        startLevel(1);
        
        gameLoop = new AnimationTimer() {
            private long lastUpdateTime = 0;
            private long nanoUpdateTime = 0; //same as lastUpdateTime but in nanoseconds
            
            @Override
            public void handle(long now){
                
                if (nanoUpdateTime == 0){
                
                    nanoUpdateTime = now;
                    return;
                    
                }
                
                double deltaTime = (now - nanoUpdateTime) / 1000000000.0; //nanoseconds to seconds
                lastUpdateTime = now;
                
                if(isRunning && !isGameOver){
                
                    update(deltaTime);
                    
                    // Claude explaination of use of delta time (popular but confusing a little)
                    /*
                        You scale movement by the actual time elapsed (position += speed * deltaTime)
                        Assuming speed = 300 pixels/second:
                        At 60 FPS: deltaTime ≈ 0.0167, so movement = 300 × 0.0167 = 5 pixels per frame
                        At 30 FPS: deltaTime ≈ 0.0333, so movement = 300 × 0.0333 = 10 pixels per frame
                        The character moves ~300 pixels per second regardless of frame rate (framerate independent experience)
                       */
                    
                }
            }
        };
        
        isRunning = true;
        gameLoop.start();
    
    }
    
    /**
     * Main game loop - updates all game entities and checks for game conditions
     * 
     * @param deltaTime Time elapsed since last update in seconds
     */
    public void update(double deltaTime) {
        // Main game loop logic
        
        player.update(deltaTime);
        
        currentLevel.update(deltaTime);
        
        checkCollisions(); 
        
        //check if level is done
        if (currentLevel.isCompleted()){
        
            int levelScore = calculateLevelScore();
            score+= levelScore;
            endLevel();
            
            if (currentLevel.getLevelNumber() >= MAX_LEVEL){ // Level methods not developed yet!
            
                isGameOver = true;
                uiManager.showGameOverDialong(true, score); //NOT DEVELOPED YET !!!!
            
            }
            
            uiManager.updateScore(score);
            uiManager.updateHearts(player.getHealth());
        }
        
    }
    
    /**
     * Initializes and starts a new level
     * 
     * @param levelNumber The level to start
     */
    public void startLevel(int levelNumber) {
        // Initialize and start a new level
        // Reset level timer
        
        currentLevel = LevelFactory.createLevel(levelNumber);
        currentLevel.initialize();
        levelStartTime = System.currentTimeMillis();
    }
    
    /**
     * Handles level completion tasks
     */
    public void endLevel(){
    
        //Cleanup logic not developed yet!
    
    }
    
    
    /**
     * Calculates score based on completion time
     * 
     * @return Points earned for level completion
     */
    public int calculateLevelScore() {
        // Calculate score based on time spent in level
        long currentTime = System.currentTimeMillis();
        long timeSpent = currentTime - levelStartTime;
        
        //Points given based on the set thresholds above!
        if (timeSpent <= FAST_TIME){
            return POINTS_FAST; 
        }
        
        else if (timeSpent <= MEDIUM_TIME){
            return POINTS_MEDIUM;
        }
        
        else {
            return POINTS_SLOW;
        }
    }
    
    /**
     * Checks if player has lost all hearts
     * 
     * @return true if game is over, false otherwise
     */
    public boolean checkGameOver(){
        return player.getHealth() <= 0; //Health not implemented yet for player!
    }
    
    /**
     * Checks collisions between player and all game objects
     */
    private void checkCollisions(){
    
        List<GameObject> objects = currentLevel.getAllObjects();
        for (GameObject obj : objects){
            if (player.collidesWith(obj)){
                if (obj instanceof Enemy){ //instanceof is bad coding practice, we need to make getters for this (obj.getType())
                    player.takeDamage();
                }
                else if (obj instanceof Obstacle && ((Obstacle) obj).isDamaging()){ //obstacles still not implemented, but we need to have some sort of other knockback effect for other types of obstacles!
                    player.takeDamage();
                }
            }
        }
    }
    
    /**
     * Pauses the game
     */
    public void pauseGame(){
        isRunning = false;
    }
    
    /**
     * Resumes the game
     */
    public void resumeGame(){
        isRunning = true;
    }
    
    /**
     * Resets the game
     */
    public void resetGame(){
        score = 0;
        isGameOver = false;
        intializeGame();
    }
    
    /**
     * kills the game engine
     */
    public void kill(){
        if(gameLoop!=null){
            gameLoop.stop();
        }
    }
    
    /**
     * Get current game score
     */
    public int getScore(){
        return score;
    }
    
    /**
     * Get current level
     */
    public int getLevel(){
        return currentLevel != null ? currentLevel.getLevelNumber() : 0;
    }

}