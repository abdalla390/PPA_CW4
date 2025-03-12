public class Level {
    private int levelNumber;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private double levelWidth;
    private double levelHeight;
    
    public void initialize() {
        // Set up level layout, spawn enemies, etc.
    }
    
    public List<GameObject> getAllObjects() {
        // Return all level objects for collision checking
    }
    
    // Level-specific methods
}