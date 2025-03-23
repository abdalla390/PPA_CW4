import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private Player player;
    private Scene scene;
    
    public InputHandler(Scene scene, Player player) {
        this.player = player;
        this.scene = scene;
        
        setupInputHandling();
    }
    
    private void setupInputHandling() {
        // Key press events
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            pressedKeys.add(code);
            
            // Handle immediate actions
            if (code == KeyCode.SPACE && player != null) {
                player.jump();
            }
            
            if (debugMode()) {
                System.out.println("Key pressed: " + code);
            }
        });
        
        // Key release events
        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            pressedKeys.remove(code);
            
            // If left/right released, stop horizontal movement only if no other directional keys are pressed
            if ((code == KeyCode.LEFT || code == KeyCode.A || 
                 code == KeyCode.RIGHT || code == KeyCode.D) &&
                !pressedKeys.contains(KeyCode.LEFT) && 
                !pressedKeys.contains(KeyCode.A) &&
                !pressedKeys.contains(KeyCode.RIGHT) && 
                !pressedKeys.contains(KeyCode.D) &&
                player != null) {
                
                player.stopMoving();
            }
            
            if (debugMode()) {
                System.out.println("Key released: " + code);
            }
        });
    }
    
    // Update player reference after restart
    public void setPlayer(Player player) {
        this.player = player;
        if (debugMode()) {
            System.out.println("InputHandler player updated");
        }
    }
    
    // Update player movement based on currently pressed keys
    public void processInput() {
        if (player == null || !player.isActive()) return; // Safety check
        
        // Handle movement based on currently pressed keys
        if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A)) {
            player.moveLeft();
        } 
        if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
            player.moveRight();
        }
    }
    
    // Clear all pressed keys and reset player state
    public void clearInputs() {
        pressedKeys.clear();
        if (player != null) {
            player.stopMoving();
        }
        if (debugMode()) {
            System.out.println("Input handler cleared!");
        }
    }
    
    // Helper method for debug output
    private boolean debugMode() {
        return false; // Change to true to enable input debugging
    }
}