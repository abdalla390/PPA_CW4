import java.util.Random;

class EnemyFactory {
    private Random random = new Random();

    public Enemy createScorpion(double x, double y) {
        return new Scorpion(x, y);
    }

    public Enemy createVulture(double x, double y) {
        return new Vulture(x, y);
    }

    public Enemy createSnake(double x, double y) {
        return new Snake(x, y);
    }

    // Method to create enemies based on level
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