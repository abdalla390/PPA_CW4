
/**
 * Represents a 2D camera that follows a game object.
 * The camera manages the viewport to show a portion of the level,
 * with smooth scrolling effects and boundary constraints.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */

public class Camera {
    private double x;
    private double y;
    private double viewportWidth;
    private double viewportHeight;
    private double levelWidth;
    private double levelHeight;
    
    private double smoothingFactor = 0.1;
    private double targetX;
    
    /**
     * Constructs a new camera with the specified viewport dimensions.
     * The camera starts at position (0,0).
     * 
     * @param viewportWidth The width of the visible area in pixels.
     * @param viewportHeight The height of the visible area in pixels.
     */
    
    public Camera(double viewportWidth, double viewportHeight) {
        this.x = 0;
        this.y = 0;
        this.targetX = 0;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }
    
    /**
     * Sets the boundaries of the level that constrain camera movement.
     * The camera cannot move beyond these boundaries, preventing it from
     * showing areas outside the level.
     * 
     * @param levelWidth The total width of the level in pixels.
     * @param levelHeight The total height of the level in pixels.
     */
    public void setLevelBounds(double levelWidth, double levelHeight) {
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }
    
    
    /**
     * Updates the camera position to follow a target object.
     * 
     * @param target The game object that the camera should follow.
     */
    public void follow(GameObject target) {
        // Center the camera on the target with smooth movement
        targetX = target.getX() - viewportWidth / 2 + target.getWidth() / 2;
        
        // Smooth camera movement
        x += (targetX - x) * smoothingFactor;
        
        // Keep camera within level bounds
        if (x < 0) x = 0;
        if (x > levelWidth - viewportWidth) x = levelWidth - viewportWidth;
    }
    
    /**
     * Gets the current X position.
     * @return The X coordinate of the camera in world space.
     */
    public double getX() {
        return x;
    }
    
    /**
     * Gets the current Y position.
     * @return The Y coordinate of the camera in world space.
     */
    public double getY() {
        return y;
    }
    
    /**
     * Converts a world X coordinate to screen coordinate.
     * 
     * @param worldX The X coordinate in world space.
     * @return The corresponding X coordinate in screen space.
     */
    public double worldToScreenX(double worldX) {
        return worldX - x;
    }
    
    /**
     * Converts a world Y coordinate to screen coordinate.
     * Used for rendering objects at the correct position on screen.
     * 
     * @param worldY The Y coordinate in world space.
     * @return The corresponding Y coordinate in screen space.
     */
    public double worldToScreenY(double worldY) {
        return worldY - y;
    }
    
    /**
     * Determines if a game object is visible within the current viewport.
     * 
     * @param obj The game object to check for visibility.
     * @return True if the object is at least partially visible, false otherwise.
     */
    public boolean isVisible(GameObject obj) {
        return !(obj.getX() + obj.getWidth() < x || 
                obj.getX() > x + viewportWidth ||
                obj.getY() + obj.getHeight() < y ||
                obj.getY() > y + viewportHeight);
    }
}