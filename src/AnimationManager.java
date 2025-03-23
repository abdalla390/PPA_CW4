import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

public class AnimationManager {
    private static final Map<GameObject, Timeline> activeAnimations = new HashMap<>();

    public static void createPlayerJumpAnimation(Player player) {
        try {
            DebugLogger.log("Creating player jump animation");
            cancelExistingAnimation(player);
            
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    try {
                        player.setActive(false);
                        DebugLogger.log("Player jump animation - setting active=false");
                    } catch (Exception ex) {
                        DebugLogger.logException(ex);
                    }
                }),
                new KeyFrame(Duration.millis(200), e -> {
                    try {
                        if (!player.isDying()) { // Skip reactivation if dying
                            player.setActive(true);
                            DebugLogger.log("Player jump animation - setting active=true");
                        } else {
                            DebugLogger.log("Player jump animation - player dying, skipping reactivation");
                        }
                    } catch (Exception ex) {
                        DebugLogger.logException(ex);
                    }
                })
            );
            
            timeline.setCycleCount(1);
            activeAnimations.put(player, timeline);
            timeline.play();
            
            timeline.setOnFinished(e -> {
                activeAnimations.remove(player);
                DebugLogger.log("Player jump animation completed and removed from tracking");
            });
        } catch (Exception e) {
            DebugLogger.logException(e);
            // Ensure player is active even if animation fails
            if (player != null) {
                player.setActive(true);
            }
        }
    }

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
            DebugLogger.logException(e);
            // Ensure enemy is active even if animation fails
            if (enemy != null) {
                enemy.setActive(true);
            }
        }
    }

    public static void createDeathAnimation(GameObject object) {
        try {
            DebugLogger.log("Creating death animation for " + object.getClass().getSimpleName());
            cancelExistingAnimation(object);
            
            if (object instanceof Player) {
                Player player = (Player) object;
                if (!player.isDying()) {
                    DebugLogger.log("Starting player death animation via AnimationManager");
                    player.startDeathAnimation();
                } else {
                    DebugLogger.log("Player already dying, skipping AnimationManager death animation");
                }
            } else {
                Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300), e -> {
                        object.setActive(false);
                        DebugLogger.log("Death animation completed for " + object.getClass().getSimpleName());
                    })
                );
                
                timeline.setCycleCount(1);
                activeAnimations.put(object, timeline);
                timeline.play();
                
                timeline.setOnFinished(e -> activeAnimations.remove(object));
            }
        } catch (Exception e) {
            DebugLogger.logException(e);
            if (object != null) {
                object.setActive(false);
            }
        }
    }
    
    private static void cancelExistingAnimation(GameObject object) {
        try {
            Timeline existingAnimation = activeAnimations.get(object);
            if (existingAnimation != null) {
                DebugLogger.log("Cancelling existing animation for " + 
                               object.getClass().getSimpleName());
                existingAnimation.stop();
                activeAnimations.remove(object);
            }
        } catch (Exception e) {
            DebugLogger.logException(e);
        }
    }
    
    public static void cleanupAllAnimations() {
        try {
            DebugLogger.log("Cleaning up all animations. Active count: " + activeAnimations.size());
            for (Timeline timeline : activeAnimations.values()) {
                timeline.stop();
            }
            activeAnimations.clear();
            DebugLogger.log("All animations cleaned up");
        } catch (Exception e) {
            DebugLogger.logException(e);
        }
    }
}