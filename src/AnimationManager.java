import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages animations for game objects.
 * Provides methods to create different types of animations for player and enemies,
 * and handles cancellation and cleanup of active animations.
 * 
 * @author @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */

public class AnimationManager {
    // Map to track all active animations with their associated game objects
    private static final Map<GameObject, Timeline> activeAnimations = new HashMap<>();
    
    /**
     * Creates a jump animation for the player.
     * This animation temporarily makes the player invisible and then visible again
     * to create a visual effect during jumps.
     * 
     * @param player The player object to animate.
     */
    public static void createPlayerJumpAnimation(Player player) {
        try {
            cancelExistingAnimation(player);
            
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    try {
                        player.setActive(false);
                       
                    } catch (Exception ex) {
                        System.err.println("Exception" + ex.getMessage());
                        ex.printStackTrace();
                    }
                }),
                new KeyFrame(Duration.millis(200), e -> {
                    try {
                        if (!player.isDying()) { 
                            player.setActive(true);
                            
                        }
                    } catch (Exception ex) {
                        System.err.println("Exception" + ex.getMessage());
                        ex.printStackTrace();
                    }
                })
            );
            
            timeline.setCycleCount(1);
            activeAnimations.put(player, timeline);
            timeline.play();
            
            timeline.setOnFinished(e -> {
                activeAnimations.remove(player);
            });
        } catch (Exception e) {
            System.err.println("Exception" + e.getMessage());
            e.printStackTrace();
            
            if (player != null) {
                player.setActive(true);
            }
        }
    }
    
    /**
     * Creates an attack animation for an enemy.
     * This animation briefly makes the enemy invisible and then visible again
     * to create a visual "flash" effect during attacks.
     * 
     * @param enemy The enemy object to animate.
     */
    
    public static void createEnemyAttackAnimation(Enemy enemy) {
        try {
            cancelExistingAnimation(enemy);
            
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> enemy.setActive(false)),
                new KeyFrame(Duration.millis(200), e -> enemy.setActive(true))
            );
            
            timeline.setCycleCount(1);
            activeAnimations.put(enemy, timeline);
            timeline.play();
            
            timeline.setOnFinished(e -> activeAnimations.remove(enemy));
        } catch (Exception e) {
            System.err.println("Exception" + e.getMessage());
            e.printStackTrace();
            
            if (enemy != null) {
                enemy.setActive(true);
            }
        }
    }
    
    /**
     * Creates a death animation for a game object.
     * For Player objects, this delegates to the player's own death animation.
     * For other objects, it creates a simple animation that makes the object disappear.
     * 
     * @param object The game object to animate during its death.
     */
    
    public static void createDeathAnimation(GameObject object) {
        try {
            cancelExistingAnimation(object);
            
            if (object instanceof Player) {
                Player player = (Player) object;
                if (!player.isDying()) {
                    
                    player.startDeathAnimation();
                } 
            } else {
                Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300), e -> {
                        object.setActive(false);
                    })
                );
                
                timeline.setCycleCount(1);
                activeAnimations.put(object, timeline);
                timeline.play();
                
                timeline.setOnFinished(e -> activeAnimations.remove(object));
            }
        } catch (Exception e) {
            System.err.println("Exception" + e.getMessage());
            e.printStackTrace();
            
            if (object != null) {
                object.setActive(false);
            }
        }
    }
    
     /**
     * Cancels any existing animation for a game object.
     * 
     * @param object The game object whose animation should be cancelled.
     */
    private static void cancelExistingAnimation(GameObject object) {
        try {
            Timeline existingAnimation = activeAnimations.get(object);
            if (existingAnimation != null) {
                existingAnimation.stop();
                activeAnimations.remove(object);
            }
        } catch (Exception e) {
            System.err.println("Exception" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stops and removes all active animations.
     */
    public static void cleanupAllAnimations() {
        try {
            for (Timeline timeline : activeAnimations.values()) {
                timeline.stop();
            }
            activeAnimations.clear();
        } catch (Exception e) {
            System.err.println("Exception" + e.getMessage());
            e.printStackTrace();
        }
    }
}