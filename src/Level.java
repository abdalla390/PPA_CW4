import java.util.ArrayList;
import java.util.List;

public class Level {
    private int levelNumber;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private double levelWidth;
    private double levelHeight;
    private EnemyFactory enemyFactory;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.enemies = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.levelWidth = 2000; // Example width
        this.levelHeight = 720; // Example height
        this.enemyFactory = new EnemyFactory();
    }

    public void initialize() {
        // Spawn enemies based on level number
        if (levelNumber <= 3) {
            enemies.add(enemyFactory.createScorpion(500, 600));
        } else if (levelNumber <= 7) {
            enemies.add(enemyFactory.createScorpion(500, 600));
            enemies.add(enemyFactory.createSnake(800, 600));
        } else {
            enemies.add(enemyFactory.createScorpion(500, 600));
            enemies.add(enemyFactory.createSnake(800, 600));
            enemies.add(enemyFactory.createVulture(600, 400));
        }
    }

    public List<GameObject> getAllObjects() {
        List<GameObject> allObjects = new ArrayList<>();
        allObjects.addAll(enemies);
        allObjects.addAll(obstacles);
        return allObjects;
    }

    public void update(double deltaTime, Player player) {
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                enemy.update(deltaTime);
                enemy.attack(player);
            }
        }
    }

    // Other methods...
}