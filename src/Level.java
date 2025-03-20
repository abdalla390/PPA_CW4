import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

public class Level {
    private int levelNumber;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<GameObject> environmentObjects;
    private double levelWidth;
    private double levelHeight;
    private GameObject completionFlag;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.enemies = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.environmentObjects = new ArrayList<>();
        this.levelHeight = 720;
        this.levelWidth = 2000;
    }
    
    public void initialize() {
        environmentObjects.add(new DesertElement(100, 500, 200, 100));
        completionFlag = new Flag(levelWidth - 100, levelHeight / 2);
    }
    
    public void update(double deltaTime) {
        for (Enemy enemy : enemies) enemy.update(deltaTime);
        for (Obstacle obstacle : obstacles) obstacle.update(deltaTime);
        for (GameObject envObj : environmentObjects) envObj.update(deltaTime);
        if (completionFlag != null) completionFlag.update(deltaTime);
    }
    
    public List<GameObject> getAllObjects() {
        List<GameObject> objects = new ArrayList<>();
        objects.addAll(enemies);
        objects.addAll(obstacles);
        objects.addAll(environmentObjects);
        if (completionFlag != null) objects.add(completionFlag);
        return objects;
    }
    
    public boolean isCompleted(Player player) {
        return player.collidesWith(completionFlag);
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
    
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }
    
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }
    
    public void addEnvironmentObject(GameObject obj) {
        environmentObjects.add(obj);
    }
}