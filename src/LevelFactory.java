import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Random;

/**
 * Factory class for creating game levels with increasing difficulty.
 * Responsible for generating level layouts, populating them with enemies,
 * obstacles, coins, and environmental elements.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class LevelFactory {
    private static final Random random = new Random();
    
    /**
     * Creates a new level based on the level number.
     * Higher level numbers result in more challenging levels with
     * more enemies, obstacles, and wider level boundaries.
     * 
     * @param levelNumber The level number to create
     * @return A fully initialized Level object ready for gameplay
     */
    public static Level createLevel(int levelNumber) {
        Level level = new Level(levelNumber);
        
        double levelWidth;
        if (levelNumber <= 3) {
            levelWidth = 2000;
        } else if (levelNumber <= 7) {
            levelWidth = 2500;
        } else {
            levelWidth = 3000;
        }
        level.setLevelWidth(levelWidth);
        
        level.initialize();
        
        addEnvironmentElements(level, levelNumber);
        addEnemies(level, levelNumber);
        addObstacles(level, levelNumber);
        addCoins(level, levelNumber);
        addCompletionFlag(level);
        
        return level;
    }
    
    /**
     * Adds decorative environment elements to the level.
     * Elements include sand dunes, cacti, and rock formations.
     * Higher level numbers result in more environmental elements.
     * 
     * @param level The level to add environment elements to
     * @param levelNumber The current level number
     */
    private static void addEnvironmentElements(Level level, int levelNumber) {
        double levelWidth = level.getLevelWidth();
        
        // Add different desert elements based on level number
        int elementCount = 5 + (levelNumber);
        
        for (int i = 0; i < elementCount; i++) {
            double x = random.nextDouble() * (levelWidth - 300) + 150;
            
            // Determine element type
            int elementType = random.nextInt(3);
            switch (elementType) {
                case 0: 
                    level.addEnvironmentObject(new DesertElement(x, 620, 200, 50, ElementType.SAND_DUNE));
                    break;
                case 1: 
                    level.addEnvironmentObject(new DesertElement(x, 570, 30, 50, ElementType.CACTUS));
                    break;
                case 2: 
                    level.addEnvironmentObject(new DesertElement(x, 590, 80, 30, ElementType.ROCK));
                    break;
            }
        }
    }
    
    /**
     * Adds enemies to the level based on the level number.
     * Higher level numbers result in more enemies and more
     * variety in enemy types.
     * 
     * @param level The level to add enemies to
     * @param levelNumber The current level number
     */
    private static void addEnemies(Level level, int levelNumber) {
        double levelWidth = level.getLevelWidth();
        int enemyCount;
        
        // Determine enemy count based on level
        if (levelNumber <= 3) {
            enemyCount = 1 + levelNumber; // 2-4 enemies
        } else if (levelNumber <= 7) {
            enemyCount = 5 + (levelNumber - 3); // 6-9 enemies
        } else {
            enemyCount = 5 + (levelNumber); // 10+ enemies
        }
        
        // Create enemy factory
        EnemyFactory enemyFactory = new EnemyFactory();
        
        // Add enemies with spacing between them
        double spacing = levelWidth / (enemyCount + 1);
        for (int i = 0; i < enemyCount; i++) {
            double x = spacing * (i + 1);
            level.addEnemy(enemyFactory.createEnemyForLevel(levelNumber, x, 585));
        }
    }
    
    /**
     * Adds obstacles to the level based on the level number.
     * Higher level numbers introduce moving platforms and spikes.
     * 
     * @param level The level to add obstacles to
     * @param levelNumber The current level number
     */
    private static void addObstacles(Level level, int levelNumber) {
        double levelWidth = level.getLevelWidth();
        
        // Add moving platforms for levels 4+
        if (levelNumber >= 4) {
            int platformCount = levelNumber < 6 ? levelNumber - 3 : 3; // 1 platform at level 4, more at higher levels
            
            double spacing = levelWidth / (platformCount + 1);
            for (int i = 0; i < platformCount; i++) {
                double x = spacing * (i + 1);
                double y = 450 + random.nextInt(100);
                level.addObstacle(new MovingPlatform(x, y, 150, 30));
            }
        }
        
        // Add spikes for levels 7+
        if (levelNumber >= 7) {
            int spikeCount = levelNumber < 10 ? levelNumber - 7 + random.nextInt(3) : 3 + random.nextInt(3);
            
            for (int i = 0; i < spikeCount; i++) {
                double x = random.nextDouble() * (levelWidth - 500) + 250;
                level.addObstacle(new Spike(x, 590));
            }
        }
    }
    
    /**
     * Adds collectible coins to the level.
     * Higher level numbers result in more coins, with increased
     * number of valuable gold coins in later levels.
     * 
     * @param level The level to add coins to
     * @param levelNumber The current level number
     */
    private static void addCoins(Level level, int levelNumber) {
        double levelWidth = level.getLevelWidth();
        int silverCoinCount;
        int goldCoinCount;
        
        // More coins in higher levels
        if (levelNumber <= 3) {
            silverCoinCount = 10 + levelNumber * 3;   
            goldCoinCount = 3 + levelNumber;          
        } else if (levelNumber <= 7) {
            silverCoinCount = 15 + (levelNumber - 3) * 3;  
            goldCoinCount = 6 + (levelNumber - 3) * 2;    
        } else {
            silverCoinCount = 30 + (levelNumber - 7) * 3;  
            goldCoinCount = 14 + (levelNumber - 7) * 2;  
        }
        
        for (int i = 0; i < silverCoinCount; i++) {
            double x = 200 + random.nextDouble() * (levelWidth - 400); 
            double y = 450 + random.nextDouble() * 150; 
            level.addCoin(new Coin(x, y, Coin.CoinType.SILVER));
        }
        
        for (int i = 0; i < goldCoinCount; i++) {
            double x = 200 + random.nextDouble() * (levelWidth - 400);
            double y = 450 + random.nextDouble() * 100;
            level.addCoin(new Coin(x, y, Coin.CoinType.GOLD));
        }
    }
    
    /**
     * Adds a completion flag near the end of the level.
     * The player must reach this flag to complete the level.
     * 
     * @param level The level to add the completion flag to
     */
    private static void addCompletionFlag(Level level) {
        double levelWidth = level.getLevelWidth();
        Flag flag = new Flag(levelWidth - 150, 540);
        flag.setActive(true);
        level.setCompletionFlag(flag);
        
    }
}