import javafx.scene.canvas.GraphicsContext;

/**
 * Game Object class that defines all objects in the game.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */

abstract class GameObject {
    protected double x, y;
    protected double width, height;
    protected double initialX;
    protected boolean isActive;

    /**
     * Constructs a new object with the specified dimensions and coordinates.
     * 
     * @param x The x cordinate of the object.
     * @param y The y cordinate of the object.
     * @param width The width of the object.
     * @param height The height of the object.
     */
    
    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.width = width;
        this.height = height;
        this.isActive = true;
    }
    
    /**
     * Updates the object state.
     * The method is defined abstractly.
     * 
     * @param deltaTime Time elapsed since the last update in seconds.
     */
    public abstract void update(double deltaTime);
    
    /**
     * Renders the object graphics.
     * The method is defined abstractly.
     * 
     * @param GraphicsContext The graphics context to be draw on.
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Renders the object graphics.
     * The method is defined abstractly.
     * 
     * @param GameObject The game object that the object collides with.
     * @return True if the other object collides with current object false otherwise.
     */
    public boolean collidesWith(GameObject other) {
    if (other == null || !isActive || !other.isActive) return false;
    
    double xOverlap = Math.max(0, Math.min(x + width, other.x + other.width) - Math.max(x, other.x));
    double yOverlap = Math.max(0, Math.min(y + height, other.y + other.height) - Math.max(y, other.y));
    double overlapArea = xOverlap * yOverlap;
    
    
    return xOverlap > 0 && yOverlap > 0;
}
    
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}