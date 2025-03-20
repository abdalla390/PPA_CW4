public class LevelFactory {
    public static Level createLevel(int levelNumber) {
        Level level = new Level(levelNumber);
        if (levelNumber >= 1 && levelNumber <= 3) {
            level.setLevelWidth(2000);
        } else if (levelNumber >= 4 && levelNumber <= 7) {
            level.setLevelWidth(2500);
        } else if (levelNumber >= 8 && levelNumber <= 10) {
            level.setLevelWidth(3000);
        }
        level.initialize();
        return level;
    }
}