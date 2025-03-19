public class GameEngine {
    private Player player;
    private Level currentLevel;
    private int score;
    private long levelStartTime;
    private boolean isRunning;

    public GameEngine() {
        player = new Player(100, 600); // Arbitrary starting position
        isRunning = true;
    }

    public void update(double deltaTime) {
        if (!isRunning) return;
        player.update(deltaTime);
        currentLevel.update(deltaTime);

        // Update enemies and check collisions
        for (GameObject obj : currentLevel.getAllObjects()) {
            if (obj instanceof Enemy) {
                Enemy enemy = (Enemy) obj;
                enemy.update(deltaTime);
                enemy.attack(player);
            }
        }
    }

    public void startLevel(int levelNumber) {
        EnemyFactory factory = new EnemyFactory();
        currentLevel = new Level();
        currentLevel.initialize(); // Member 3’s job
        // Spawn enemies based on level (your scaling logic)
        currentLevel.addEnemy(factory.createEnemyForLevel(levelNumber, 500, 600));
        levelStartTime = System.currentTimeMillis();
        isRunning = true;
    }

    public int calculateLevelScore() {
        long timeSpent = (System.currentTimeMillis() - levelStartTime) / 1000;
        return Math.max(1000 - (int)timeSpent * 10, 0); // Example scoring
    }

    public Player getPlayer() { return player; }
    public Level getCurrentLevel() { return currentLevel; }
}