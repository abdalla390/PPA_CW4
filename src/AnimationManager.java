import javafx.scene.canvas.GraphicsContext;

public class AnimationManager {
    public static void createPlayerJumpAnimation(Player player) {
        // Member 2’s responsibility, placeholder
    }

    public static void createEnemyAttackAnimation(Enemy enemy) {
        // Simple flash effect for attack
        new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(200),
                e -> enemy.setInactive() // Temporary disable for visibility
            ),
            new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(400),
                e -> enemy.setInactive() // Reset
            )
        ).play();
    }

    public static void createDeathAnimation(GameObject object) {
        // Placeholder for death effect
    }
}