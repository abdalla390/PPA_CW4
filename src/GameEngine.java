import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import java.util.List;

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

    public GameEngine(UIManager uiManager, ScoreManager scoreManager, GraphicsContext gc, GameView gameView) {
        this.uiManager = uiManager;
        this.scoreManager = scoreManager;
        this.gc = gc;
        this.gameView = gameView;
    }

    public void initializeGame() {
        DebugLogger.init(); // Initialize logging
        player = new Player(100, 570);
        currentLevel = LevelFactory.createLevel(1);
        player.setLevelBounds(currentLevel.getLevelWidth());
        scoreManager.startLevelTimer();
        uiManager.updateHearts(player.getHealth());
        uiManager.updateLevel(currentLevel.getLevelNumber());
        uiManager.updateScore(scoreManager.getCurrentScore());
        startGameLoop();
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused) return;

                if (lastUpdate == 0) lastUpdate = now;
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // Convert to seconds
                lastUpdate = now;

                inputHandler.processInput();
                update(deltaTime);
                gameView.render();
            }
        };
        gameLoop.start();
    }

    private void update(double deltaTime) {
        if (player.isActive()) {
            player.update(deltaTime);
            currentLevel.updateEfficiently(deltaTime, player.getX(), 1280); // Update near player
            handleCollisions();
            uiManager.updateScore(scoreManager.getCurrentScore());
        } else if (player.isDying()) {
            player.update(deltaTime); // Let death animation play
        } else if (!player.isActive() && !player.isDying()) {
            // Player is fully dead (animation complete)
            pauseGame();
            handlePlayerDeath();
        }

        if (debugMode && System.currentTimeMillis() % 1000 < 50) { // Log every second
            DebugLogger.logPlayerState(player);
            DebugLogger.logMemoryUsage();
        }
    }

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

    private void handlePlayerDeath() {
        DebugLogger.log("Handling player death");
        // Get current health to determine remaining hearts
        int remainingHearts = player.getHealth() - 1;
        AnimationManager.cleanupAllAnimations(); // Prevent animation-related freezes
        
        if (remainingHearts > 0) {
            // Show death dialog with remaining hearts
            javafx.application.Platform.runLater(() -> {
                DebugLogger.log("Showing death dialog, hearts remaining: " + remainingHearts);
                uiManager.showDeathDialog(remainingHearts, 
                    () -> {
                        DebugLogger.log("Death dialog: Continue selected");
                        resetPlayer();
                        resumeGame();
                    }, 
                    () -> {
                        DebugLogger.log("Death dialog: Quit selected");
                        showGameOver(false);
                    });
            });
        } else {
            DebugLogger.log("No hearts remaining, showing game over");
            showGameOver(false);
        }
    }

    private void resetPlayer() {
        player = new Player(100, 570);
        player.setLevelBounds(currentLevel.getLevelWidth());
        inputHandler.setPlayer(player);
        scoreManager.applyDamagePenalty();
        uiManager.updateHearts(player.getHealth());
        uiManager.updateScore(scoreManager.getCurrentScore());
    }

    private void advanceToNextLevel() {
        int levelScore = scoreManager.calculateLevelScore();
        currentLevel = LevelFactory.createLevel(currentLevel.getLevelNumber() + 1);
        resetPlayer();
        player.setLevelBounds(currentLevel.getLevelWidth());
        scoreManager.startLevelTimer();
        uiManager.updateLevel(currentLevel.getLevelNumber());
        uiManager.updateScore(scoreManager.getCurrentScore());
    }

    private void showGameOver(boolean isWin) {
        DebugLogger.log("Showing game over. isWin: " + isWin);
        
        // Get the final score (including current level uncollected points)
        int finalScore = scoreManager.getFinalScore();
        if (isWin) {
            // If winning, calculate level score with time bonus
            scoreManager.calculateLevelScore();
            finalScore = scoreManager.getTotalScore();
        }
        
        DebugLogger.log("Final score for game over screen: " + finalScore);
        
        // Capture variables for lambda
        final int scoreToShow = finalScore;
        
        javafx.application.Platform.runLater(() -> {
            uiManager.showGameOverDialog(isWin, scoreToShow, currentLevel.getLevelNumber());
            resetGame();
        });
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
        lastUpdate = 0; // Reset delta time
    }

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

    public void kill() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        AnimationManager.cleanupAllAnimations();
        if (currentLevel != null) {
            currentLevel.cleanup();
        }
    }

    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    public void setInputHandler(InputHandler handler) {
        this.inputHandler = handler;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}