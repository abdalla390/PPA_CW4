import javafx.scene.canvas.GraphicsContext;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Level {
    private int levelNumber;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<GameObject> environmentObjects;
    private List<Coin> coins;
    private double levelWidth;
    private double levelHeight;
    private GameObject completionFlag;
    
    // Performance optimization
    private boolean debugMode = false;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.enemies = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.environmentObjects = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.levelHeight = 720;
        this.levelWidth = 2000;
    }
    
    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    public void initialize() {
        // Add basic environment elements
        environmentObjects.add(new DesertElement(100, 650, 200, 50, ElementType.SAND_DUNE));
    }

    public void update(double deltaTime) {
        // Update all game objects
        for (Enemy enemy : enemies) enemy.update(deltaTime);
        for (Obstacle obstacle : obstacles) obstacle.update(deltaTime);
        for (GameObject envObj : environmentObjects) envObj.update(deltaTime);
        for (Coin coin : coins) coin.update(deltaTime);
        if (completionFlag != null) completionFlag.update(deltaTime);
    }
    
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

    public List<GameObject> getAllObjects() {
        List<GameObject> objects = new ArrayList<>();
        objects.addAll(enemies);
        objects.addAll(obstacles);
        objects.addAll(environmentObjects);
        objects.addAll(coins);
        if (completionFlag != null) objects.add(completionFlag);
        return objects;
    }
    
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
    
    private boolean isVisible(GameObject obj, double cameraX, double viewportWidth) {
        return !(obj.getX() + obj.getWidth() < cameraX || 
                obj.getX() > cameraX + viewportWidth);
    }

    public boolean isCompleted(Player player) {
        if (player == null || completionFlag == null) {
            return false;
        }
        
        if (debugMode && Math.random() < 0.01) {
            System.out.println("Checking completion: Player (" + player.getX() + "," + player.getY() + 
                             "), Flag (" + completionFlag.getX() + "," + completionFlag.getY() + ")");
        }
        
        boolean colliding = player.collidesWith(completionFlag);
        if (colliding && debugMode) {
            System.out.println("*** LEVEL COMPLETED - Player touched flag! ***");
        }
        
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
        System.out.println("Added " + (coin.getType() == Coin.CoinType.GOLD ? "gold" : "silver") + 
                         " coin at position: (" + coin.getX() + ", " + coin.getY() + ")");
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
    
    // New method to fix the undeclared getObstacles() error
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

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