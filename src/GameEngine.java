import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import java.util.List;


/**
 * The core engine responsible for running the game loop and managing game state.
 * Handles initialization, updates, collision detection, player death,
 * level advancement, and game state transitions. Serves as the central
 * coordinator between various game components.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */

public class GameEngine {
    private AnimationTimer gameLoop;
    private Player player;
    private Level currentLevel;
    private UIManager uiManager;
    private ScoreManager scoreManager;
    private GraphicsContext gc;
    private GameView gameView;
    private InputHandler inputHandler;
    private boolean isPaused = false;
    private boolean debugMode = false;
    private long lastUpdate = 0;
    
    /**
     * Constructs a new game engine with the specified components.
     * Links the engine to the UI, score system, graphics context, and view.
     * 
     * @param uiManager The manager for user interface elements.
     * @param scoreManager The manager for tracking and calculating score.
     * @param gc The graphics context for rendering.
     * @param gameView The view component for displaying the game.
     */
    public GameEngine(UIManager uiManager, ScoreManager scoreManager, GraphicsContext gc, GameView gameView) {
        this.uiManager = uiManager;
        this.scoreManager = scoreManager;
        this.gc = gc;
        this.gameView = gameView;
    }

     /**
     * Initializes the game state for a new game.
     * Creates the player, first level, and starts the score timer.
     * Updates UI elements to reflect the initial state.
     */
    public void initializeGame() {
        player = new Player(100, 570);
        currentLevel = LevelFactory.createLevel(1);
        player.setLevelBounds(currentLevel.getLevelWidth());
        scoreManager.startLevelTimer();
        uiManager.updateHearts(player.getHealth());
        uiManager.updateLevel(currentLevel.getLevelNumber());
        uiManager.updateScore(scoreManager.getCurrentScore());
        startGameLoop();
    }
    
    
    /**
     * Starts the main game loop using JavaFX AnimationTimer.
     * The loop handles input processing, game state updates,
     * and rendering at appropriate intervals.
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused) return;

                if (lastUpdate == 0) lastUpdate = now;
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0; 
                lastUpdate = now;

                inputHandler.processInput();
                update(deltaTime);
                gameView.render();
            }
        };
        gameLoop.start();
    }

    /**
     * Updates the game state for each frame.
     * Handles player and level updates, collision detection,
     * UI updates, and transitions between gameplay states.
     * 
     * @param deltaTime Time elapsed since the last update in seconds.
     */
    private void update(double deltaTime) {
        if (player.isActive()) {
            player.update(deltaTime);
            currentLevel.updateEfficiently(deltaTime, player.getX(), 1280);
            handleCollisions();
            uiManager.updateScore(scoreManager.getCurrentScore());
        } else if (player.isDying()) {
            player.update(deltaTime); 
        } else if (!player.isActive() && !player.isDying()) {
            
            pauseGame();
            handlePlayerDeath();
        }
    }
    
    /**
     * Handles all collision detection between the player and game objects.
     * Processes interactions with enemies, obstacles, coins, and level completion flags.
     * Triggers appropriate actions based on collision types.
     */
    private void handleCollisions() {
        List<GameObject> nearbyObjects = currentLevel.getObjectsNearPlayer(player.getX(), 1280);
        for (GameObject obj : nearbyObjects) {
            if (player.collidesWith(obj)) {
                if (obj instanceof Enemy) {
                    ((Enemy) obj).attack(player);
                    uiManager.updateHearts(player.getHealth());
                } else if (obj instanceof Obstacle && ((Obstacle) obj).isDamaging()) {
                    player.takeDamage(1.0f);
                    scoreManager.applyDamagePenalty();
                    uiManager.updateHearts(player.getHealth());
                } else if (obj instanceof Coin) {
                    Coin coin = (Coin) obj;
                    if (!coin.isCollected()) {
                        coin.collect();
                        scoreManager.addScore(coin.getType() == Coin.CoinType.GOLD ? 10 : 1);
                    }
                } else if (obj instanceof Flag) {
                    advanceToNextLevel();
                }
            }
        }

        // Platform-specific collision (for moving platforms)
        for (Obstacle obstacle : currentLevel.getObstacles()) {
            if (obstacle instanceof MovingPlatform && player.collidesWith(obstacle)) {
                player.setVelocity(player.getVelocity().add(((MovingPlatform) obstacle).getVelocity()));
                player.setY(obstacle.getY() - player.getHeight());
            }
        }
    }

    /**
     * Handles the player death sequence.
     * Determines remaining hearts, displays the appropriate dialog,
     * and sets up continuation or game over based on player status.
     */
    private void handlePlayerDeath() {
        // Get current health to determine remaining hearts
        int remainingHearts = player.getHealth() - 1;
        AnimationManager.cleanupAllAnimations(); // Prevent animation-related freezes
        
        if (remainingHearts > 0) {
            // Show death dialog with remaining hearts
            javafx.application.Platform.runLater(() -> {
                uiManager.showDeathDialog(remainingHearts, 
                    () -> {
                        resetPlayer();
                        resumeGame();
                    }, 
                    () -> {
                        showGameOver(false);
                    });
            });
        } else {
            showGameOver(false);
        }
    }

    
    // Modified resetPlayer method with optional health parameter
    private void resetPlayer(float healthValue) {
        player = new Player(100, 570);
        player.setLevelBounds(currentLevel.getLevelWidth());
        // Set the health to the provided value instead of using default
        player.setHealth(healthValue);
        inputHandler.setPlayer(player);
        scoreManager.applyDamagePenalty();
        uiManager.updateHearts(player.getHealth());
        uiManager.updateScore(scoreManager.getCurrentScore());
    }

     /**
     * Resets the player to the starting position with current health.
     * Called after death if the player has remaining hearts.
     * Applies score penalties and updates UI accordingly.
     */
    private void resetPlayer() {
        resetPlayer(3.0f);
    }
    
    /**
     * Advances the game to the next level after completing the current one.
     * Calculates level score, creates a new level, resets player position,
     * and updates UI elements for the new level.
     */
    private void advanceToNextLevel() {
        int levelScore = scoreManager.calculateLevelScore();
        currentLevel = LevelFactory.createLevel(currentLevel.getLevelNumber() + 1);
        float currentHealth = player.getHealth();
        resetPlayer(currentHealth);
        player.setLevelBounds(currentLevel.getLevelWidth());
        scoreManager.startLevelTimer();
        uiManager.updateLevel(currentLevel.getLevelNumber());
        uiManager.updateScore(scoreManager.getCurrentScore());
    }

    /**
     * Displays the game over screen with final score and outcome.
     * Shows different UI based on whether the player won or lost.
     * 
     * @param isWin True if the player completed all levels, false if they died.
     */
    
    private void showGameOver(boolean isWin) {
        
        int finalScore = scoreManager.getFinalScore();
        if (isWin) {
            
            scoreManager.calculateLevelScore();
            finalScore = scoreManager.getTotalScore();
        }
        
        final int scoreToShow = finalScore;
        
        javafx.application.Platform.runLater(() -> {
            uiManager.showGameOverDialog(isWin, scoreToShow, currentLevel.getLevelNumber());
            resetGame();
        });
    }

    /**
     * Pauses the game loop.
     * Stops updates and rendering until the game is resumed.
     */
    public void pauseGame() {
        isPaused = true;
    }

    /**
     * Resumes the game loop after it has been paused.
     * Resets the delta time counter to prevent large jumps.
     */
    public void resumeGame() {
        isPaused = false;
        lastUpdate = 0; // Reset delta time
    }

    /**
     * Completely resets the game to its initial state.
     * Clears all game progress, score, and animations.
     * Creates a fresh game state ready to be played.
     */
    public void resetGame() {
        pauseGame();
        AnimationManager.cleanupAllAnimations();
        scoreManager.resetScore();

        // Reset input handler to clear any pressed keys
        if (inputHandler != null) {
            inputHandler.clearInputs();
        }

        // Initialize a fresh game state
        initializeGame();

        // Re-link input handler with new player
        if (inputHandler != null) {
            inputHandler.setPlayer(player);
        }

        // Resume the game
        resumeGame();
    }

    /**
     * Cleans up resources when the game is shutting down.
     * Stops the game loop, cleans up animations, and releases level resources.
     */
    public void kill() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        AnimationManager.cleanupAllAnimations();
        if (currentLevel != null) {
            currentLevel.cleanup();
        }
    }

    /**
     * Sets the input handler for this game engine.
     * 
     * @param handler The input handler that will process user input.
     */
    public void setInputHandler(InputHandler handler) {
        this.inputHandler = handler;
    }

    /**
     * Gets the current player object.
     * 
     * @return The player instance being used in the game.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the current level object.
     * 
     * @return The level instance currently active in the game.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }
}