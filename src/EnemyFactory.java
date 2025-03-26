import java.util.Random;

/**
 * Factory class responsible for creating enemy instances.
 * Implements the Factory design pattern to centralize enemy creation
 * and distribute appropriate enemy types based on the game level.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
class EnemyFactory {
    /** Random number generator for enemy type selection */
    private Random random = new Random();

    /**
     * Creates a Scorpion enemy at the specified position.
     * Scorpions patrol horizontally and deal high damage.
     * 
     * @param x The X-coordinate for the new scorpion.
     * @param y The Y-coordinate for the new scorpion.
     * @return A new Scorpion enemy instance.
     */
    public Enemy createScorpion(double x, double y) {
        return new Scorpion(x, y);
    }

    /**
     * Creates a Vulture enemy at the specified position.
     * Vultures swoop vertically and deal medium damage.
     * 
     * @param x The X-coordinate for the new vulture.
     * @param y The Y-coordinate for the new vulture.
     * @return A new Vulture enemy instance.
     */
    public Enemy createVulture(double x, double y) {
        return new Vulture(x, y);
    }

    /**
     * Creates a Snake enemy at the specified position.
     * Snakes lunge horizontally and deal low damage.
     * 
     * @param x The X-coordinate for the new snake.
     * @param y The Y-coordinate for the new snake.
     * @return A new Snake enemy instance.
     */
    public Enemy createSnake(double x, double y) {
        return new Snake(x, y);
    }

    /**
     * Creates an appropriate enemy for the specified level at the given position.
     * The enemy type distribution changes based on level:
     * - Levels 1-3: Only scorpions
     * - Levels 4-7: Mix of scorpions and snakes
     * - Levels 8+: Equal distribution of all enemy types
     * 
     * @param levelNumber The current game level number.
     * @param x The X-coordinate for the new enemy.
     * @param y The Y-coordinate for the new enemy.
     * @return A new Enemy instance appropriate for the current level.
     */
    public Enemy createEnemyForLevel(int levelNumber, double x, double y) {
        if (levelNumber <= 3) {
            return createScorpion(x, y);
        } else if (levelNumber <= 7) {
            return random.nextBoolean() ? createScorpion(x, y) : createSnake(x, y);
        } else {
            int type = random.nextInt(3);
            switch (type) {
                case 0: return createScorpion(x, y);
                case 1: return createVulture(x, y);
                default: return createSnake(x, y);
            }
        }
    }
}