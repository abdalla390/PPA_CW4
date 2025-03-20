class GameEngine {
    private int currentLevel;
    private int totalScore;
    private Player player;
    private Level currentLevelInstance;
    
    public void startLevel(int levelNumber) {
        this.currentLevel = levelNumber;
        this.currentLevelInstance = LevelFactory.createLevel(levelNumber);
        this.player = new Player(100, 600);
    }
    
    public void update(double deltaTime) {
        player.update(deltaTime);
        currentLevelInstance.update(deltaTime);
        if (player.getHealth() <= 0) {
            checkGameOver();
        }
    }
    
    public boolean checkGameOver() {
        return player.getHealth() <= 0;
    }
    
    public int calculateScore() {
        return totalScore;
    }
}