import javafx.scene.canvas.GraphicsContext;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Represents a game level containing all objects and environment elements.
 * Manages collections of enemies, obstacles, coins, and decorative elements.
 * Provides methods for updating, rendering, and querying level objects
 * with performance optimizations for object culling.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class Level {
    private int levelNumber;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<GameObject> environmentObjects;
    private List<Coin> coins;
    private double levelWidth;
    private double levelHeight;
    private GameObject completionFlag;
    

    /**
     * Constructs a new level with the specified number.
     * Initializes empty collections for level objects.
     * 
     * @param levelNumber The level number, which determines difficulty and content.
     */
    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.enemies = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.environmentObjects = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.levelHeight = 720;
        this.levelWidth = 2000;
    }

    /**
     * Initializes the level with basic environment elements.
     * Called during level creation.
     */
    public void initialize() {
        environmentObjects.add(new DesertElement(100, 650, 200, 50, ElementType.SAND_DUNE));
    }
    
    
    /**
     * Updates all game objects in the level.
     * 
     * @param deltaTime Time elapsed since the last update in seconds.
     */
    public void update(double deltaTime) {
        for (Enemy enemy : enemies) enemy.update(deltaTime);
        for (Obstacle obstacle : obstacles) obstacle.update(deltaTime);
        for (GameObject envObj : environmentObjects) envObj.update(deltaTime);
        for (Coin coin : coins) coin.update(deltaTime);
        if (completionFlag != null) completionFlag.update(deltaTime);
    }
    
    /**
     * Updates game objects more efficiently by only updating objects near the player.
     * Implements culling for performance optimization.
     * 
     * @param deltaTime Time elapsed since the last update in seconds.
     * @param playerX The x-coordinate of the player.
     * @param cullingDistance The maximum distance for objects to be updated.
     */
    public void updateEfficiently(double deltaTime, double playerX, double cullingDistance) {
        for (Enemy enemy : enemies) {
            if (Math.abs(enemy.getX() - playerX) < cullingDistance) {
                enemy.update(deltaTime);
            }
        }
        
        for (Obstacle obstacle : obstacles) {
            if (Math.abs(obstacle.getX() - playerX) < cullingDistance) {
                obstacle.update(deltaTime);
            }
        }
        
        for (Coin coin : coins) {
            if (Math.abs(coin.getX() - playerX) < cullingDistance) {
                coin.update(deltaTime);
            }
        }
        
        if (Math.random() < 0.1) { // Only update 10% of the time
            for (GameObject envObj : environmentObjects) {
                if (Math.abs(envObj.getX() - playerX) < cullingDistance) {
                    envObj.update(deltaTime);
                }
            }
        }
        
        if (completionFlag != null) completionFlag.update(deltaTime);
    }
    
    /**
     * Returns all active game objects that are near the player.
     * Used for collision detection and rendering optimization.
     * 
     * @param playerX The x-coordinate of the player.
     * @param cullingDistance The maximum distance for objects to be included.
     * @return A list of game objects near the player.
     */
    public List<GameObject> getObjectsNearPlayer(double playerX, double cullingDistance) {
        List<GameObject> nearby = new ArrayList<>();
        
        for (Enemy enemy : enemies) {
            if (Math.abs(enemy.getX() - playerX) < cullingDistance && enemy.isActive()) {
                nearby.add(enemy);
            }
        }
        
        for (Obstacle obstacle : obstacles) {
            if (Math.abs(obstacle.getX() - playerX) < cullingDistance && obstacle.isActive()) {
                nearby.add(obstacle);
            }
        }
        
        for (Coin coin : coins) {
            if (Math.abs(coin.getX() - playerX) < cullingDistance && coin.isActive()) {
                nearby.add(coin);
            }
        }
        
        if (completionFlag != null && 
            Math.abs(completionFlag.getX() - playerX) < cullingDistance && 
            completionFlag.isActive()) {
            nearby.add(completionFlag);
        }
        
        return nearby;
    }

    /**
     * Returns all game objects in the level.
     * 
     * @return A combined list of all game objects.
     */
    public List<GameObject> getAllObjects() {
        List<GameObject> objects = new ArrayList<>();
        objects.addAll(enemies);
        objects.addAll(obstacles);
        objects.addAll(environmentObjects);
        objects.addAll(coins);
        if (completionFlag != null) objects.add(completionFlag);
        return objects;
    }
    
    /**
     * Returns all visible game objects based on the camera position.
     * Used for efficient rendering.
     * 
     * @param cameraX The x-coordinate of the camera.
     * @param viewportWidth The width of the visible viewport.
     * @return A list of visible game objects.
     */
    public List<GameObject> getVisibleObjects(double cameraX, double viewportWidth) {
        List<GameObject> visible = new ArrayList<>();
        
        for (GameObject obj : enemies) {
            if (isVisible(obj, cameraX, viewportWidth) && obj.isActive()) {
                visible.add(obj);
            }
        }
        
        for (GameObject obj : obstacles) {
            if (isVisible(obj, cameraX, viewportWidth) && obj.isActive()) {
                visible.add(obj);
            }
        }
        
        for (GameObject obj : environmentObjects) {
            if (isVisible(obj, cameraX, viewportWidth) && obj.isActive()) {
                visible.add(obj);
            }
        }
        
        for (GameObject obj : coins) {
            if (isVisible(obj, cameraX, viewportWidth) && obj.isActive()) {
                visible.add(obj);
            }
        }
        
        if (completionFlag != null && 
            isVisible(completionFlag, cameraX, viewportWidth) && 
            completionFlag.isActive()) {
            visible.add(completionFlag);
        }
        
        return visible;
    }
    
    /**
     * Determines if a game object is visible within the camera view.
     * 
     * @param obj The game object to check.
     * @param cameraX The x-coordinate of the camera.
     * @param viewportWidth The width of the visible viewport.
     * @return True if the object is visible, false otherwise.
     */
    private boolean isVisible(GameObject obj, double cameraX, double viewportWidth) {
        return !(obj.getX() + obj.getWidth() < cameraX || 
                obj.getX() > cameraX + viewportWidth);
    }

    /**
     * Checks if the level is completed by the player.
     * Level is completed when the player collides with the completion flag.
     * 
     * @param player The player object.
     * @return True if the level is completed, false otherwise.
     */
    public boolean isCompleted(Player player) {
        if (player == null || completionFlag == null) {
            return false;
        }
        
        boolean colliding = player.collidesWith(completionFlag);
        
        return colliding;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public double getLevelWidth() {
        return levelWidth;
    }

    public double getLevelHeight() {
        return levelHeight;
    }

    public void setLevelWidth(double width) {
        this.levelWidth = width;
    }

    public GameObject getCompletionFlag() {
        return completionFlag;
    }

    public void setCompletionFlag(GameObject flag) {
        this.completionFlag = flag;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public void addEnvironmentObject(GameObject obj) {
        environmentObjects.add(obj);
    }
    
    public void addCoin(Coin coin) {
        if (coins == null) {
            coins = new ArrayList<>();
        }
        coins.add(coin);
    }
    
    public List<Coin> getActiveCoins() {
        List<Coin> activeCoins = new ArrayList<>();
        for (Coin coin : coins) {
            if (coin.isActive() && !coin.isCollected()) {
                activeCoins.add(coin);
            }
        }
        return activeCoins;
    }
    
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * The Method sets everything in the Level to null to ensure consistency.
     */
    public void cleanup() {
        if (enemies != null) {
            enemies.clear();
            enemies = null;
        }
        
        if (obstacles != null) {
            obstacles.clear();
            obstacles = null;
        }
        
        if (environmentObjects != null) {
            environmentObjects.clear();
            environmentObjects = null;
        }
        
        if (coins != null) {
            coins.clear();
            coins = null;
        }
        
        completionFlag = null;
    }
}