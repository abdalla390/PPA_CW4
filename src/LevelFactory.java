import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Random;

public class LevelFactory {
    private static final Random random = new Random();
    
    public static Level createLevel(int levelNumber) {
        Level level = new Level(levelNumber);
        
        // Set width based on level
        double levelWidth;
        if (levelNumber <= 3) {
            levelWidth = 2000;
        } else if (levelNumber <= 7) {
            levelWidth = 2500;
        } else {
            levelWidth = 3000;
        }
        level.setLevelWidth(levelWidth);
        
        // Initialize level (base environment)
        level.initialize();
        
        // Add environment elements
        addEnvironmentElements(level, levelNumber);
        
        // Add enemies based on level
        addEnemies(level, levelNumber);
        
        // Add obstacles based on level
        addObstacles(level, levelNumber);
        
        // Add coins to the level
        addCoins(level, levelNumber);
        
        // Add completion flag at end of level
        addCompletionFlag(level);
        
        return level;
    }
    
    private static void addEnvironmentElements(Level level, int levelNumber) {
        double levelWidth = level.getLevelWidth();
        
        // Add different desert elements based on level number
        int elementCount = 5 + (levelNumber * 2); // More elements in higher levels
        
        for (int i = 0; i < elementCount; i++) {
            double x = random.nextDouble() * (levelWidth - 300) + 150; // Spread elements across level
            
            // Determine element type
            int elementType = random.nextInt(3);
            switch (elementType) {
                case 0: // Sand dune
                    level.addEnvironmentObject(new DesertElement(x, 620, 200, 50, ElementType.SAND_DUNE));
                    break;
                case 1: // Cactus
                    level.addEnvironmentObject(new DesertElement(x, 570, 30, 50, ElementType.CACTUS));
                    break;
                case 2: // Rock formation
                    level.addEnvironmentObject(new DesertElement(x, 590, 80, 30, ElementType.ROCK));
                    break;
            }
        }
    }
    
    private static void addEnemies(Level level, int levelNumber) {
        double levelWidth = level.getLevelWidth();
        int enemyCount;
        
        // Determine enemy count based on level
        if (levelNumber <= 3) {
            enemyCount = 2 + levelNumber; // 3-5 enemies
        } else if (levelNumber <= 7) {
            enemyCount = 5 + (levelNumber - 3); // 6-9 enemies
        } else {
            enemyCount = 10 + (levelNumber - 7); // 11-13 enemies
        }
        
        // Create enemy factory
        EnemyFactory enemyFactory = new EnemyFactory();
        
        // Add enemies with spacing between them
        double spacing = levelWidth / (enemyCount + 1);
        for (int i = 0; i < enemyCount; i++) {
            double x = spacing * (i + 1);
            level.addEnemy(enemyFactory.createEnemyForLevel(levelNumber, x, 570));
        }
    }
    
    private static void addObstacles(Level level, int levelNumber) {
        double levelWidth = level.getLevelWidth();
        
        // Add moving platforms for levels 4+
        if (levelNumber >= 4) {
            int platformCount = levelNumber - 3; // 1 platform at level 4, more at higher levels
            
            double spacing = levelWidth / (platformCount + 1);
            for (int i = 0; i < platformCount; i++) {
                double x = spacing * (i + 1);
                double y = 450 + random.nextInt(100); // Vary height
                level.addObstacle(new MovingPlatform(x, y, 150, 30));
            }
        }
        
        // Add spikes for levels 8+
        if (levelNumber >= 8) {
            int spikeCount = levelNumber - 7 + random.nextInt(3); // More spikes at higher levels
            
            for (int i = 0; i < spikeCount; i++) {
                double x = random.nextDouble() * (levelWidth - 500) + 250; // Spread spikes
                level.addObstacle(new Spike(x, 590));
            }
        }
    }
    
    private static void addCoins(Level level, int levelNumber) {
        double levelWidth = level.getLevelWidth();
        int silverCoinCount;
        int goldCoinCount;
        
        // More coins in higher levels
        if (levelNumber <= 3) {
            silverCoinCount = 15 + levelNumber * 5;    // 20-30 silver coins
            goldCoinCount = 3 + levelNumber;           // 4-6 gold coins
        } else if (levelNumber <= 7) {
            silverCoinCount = 30 + (levelNumber - 3) * 5;  // 35-50 silver coins
            goldCoinCount = 6 + (levelNumber - 3) * 2;     // 8-14 gold coins
        } else {
            silverCoinCount = 50 + (levelNumber - 7) * 5;  // 55-65 silver coins
            goldCoinCount = 14 + (levelNumber - 7) * 2;    // 16-20 gold coins
        }
        
        // Add silver coins throughout the level
        for (int i = 0; i < silverCoinCount; i++) {
            double x = 200 + random.nextDouble() * (levelWidth - 400); // Avoid start and end areas
            double y = 400 + random.nextDouble() * 150; // Place at various heights
            level.addCoin(new Coin(x, y, Coin.CoinType.SILVER));
        }
        
        // Add gold coins in more accessible places (lower Y values = higher position)
        // Fixed: Made gold coins more accessible by adjusting the Y coordinate range
        for (int i = 0; i < goldCoinCount; i++) {
            double x = 200 + random.nextDouble() * (levelWidth - 400);
            // Changed from 300-400 to 400-500 range to make coins more accessible with jumping
            double y = 400 + random.nextDouble() * 100;
            level.addCoin(new Coin(x, y, Coin.CoinType.GOLD));
        }
    }
    
    private static void addCompletionFlag(Level level) {
        // Add completion flag near the end of the level
        double levelWidth = level.getLevelWidth();
        
        // Position the flag at a visible location near the end (not too close to the very edge)
        Flag flag = new Flag(levelWidth - 150, 540);
        
        // Make sure the flag is active
        flag.setActive(true);
        
        // Set the flag for the level
        level.setCompletionFlag(flag);
        
        System.out.println("Flag placed at position: (" + flag.getX() + ", " + flag.getY() + 
                         ") for level " + level.getLevelNumber());
    }
}