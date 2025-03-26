import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages user input processing for the game.
 * Handles keyboard events, tracks pressed keys, and translates
 * user input into player actions like movement and jumping.
 * Supports multiple simultaneous key presses for smooth control.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class InputHandler {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    
    private Player player;
    
    private Scene scene;
    
    /**
     * Constructs a new input handler for the specified scene and player.
     * Sets up event handlers for key press and release events.
     * 
     * @param scene The JavaFX scene to monitor for input events.
     * @param player The player object to control based on input.
     */
    public InputHandler(Scene scene, Player player) {
        this.player = player;
        this.scene = scene;
        
        setupInputHandling();
    }
    
    /**
     * Sets up event handlers for key press and release events.
     * Maps keyboard inputs to appropriate player actions.
     */
    private void setupInputHandling() {
         scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            pressedKeys.add(code);
            
            if ((code == KeyCode.SPACE || code == KeyCode.W || code == KeyCode.UP) && player != null) {
                player.jump();
            }
        });
        
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
            
        });
    }
    
    /**
     * Updates the player reference after game restart or player reset.
     * Ensures that input events control the current player instance.
     * 
     * @param player The new player instance to control.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    /**
     * Processes currently pressed keys and applies corresponding actions.
     * Called each frame to handle continuous actions like movement.
     * Supports simultaneous key presses (e.g., moving and jumping at the same time).
     */
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
    
    /**
     * Clears all pressed keys and resets player movement state.
     * Useful when transitioning between game states or after dialog interactions.
     */
    public void clearInputs() {
        pressedKeys.clear();
        if (player != null) {
            player.stopMoving();
        }
    }
    
}