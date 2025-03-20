import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D; // Use JavaFX's Point2D instead of AWT Point

public class Player extends GameObject {
    private int health = 3;
    private double speed = 200; // pixels per second
    private boolean isJumping = false;
    private Point2D velocity = new Point2D(5, 0); // Use Point2D for double precision
    
    @Override
    public void update(double deltaTime) {
        // Update player position based on velocity
        x += velocity.getX() * deltaTime;
        y += velocity.getY() * deltaTime;
        
        // Implement other update logic as needed
    }
    
    
    public void jump() {
        if (!isJumping) {
            isJumping = true;
            velocity = new Point2D(velocity.getX(), -350); // Jump velocity
        }
    }
    
    public void moveLeft() {
        velocity = new Point2D(-speed, velocity.getY());
    }
    
    public void moveRight() {
        velocity = new Point2D(speed, velocity.getY());
    }
    
    public void stopMoving() {
        velocity = new Point2D(0, velocity.getY());
    }
    
    public void takeDamage() {
        health--;
    }
    
    public int getHealth() {
        return health;
    }
    
    // Other player-specific methods
    
    @Override
    public void render(GraphicsContext gc) {
        // Save the current graphics context state
        gc.save();
        
        // Define colors based on the specification
        Color tanColor = Color.web("#C19A6B");
        Color blackColor = Color.web("#000000");
        Color redColor = Color.web("#D22F27");
        Color whiteColor = Color.web("#FFFFFF");
        
        renderIdlePlayer(gc, tanColor, blackColor, redColor, whiteColor);
        
    
        // Restore the graphics context to its original state
        gc.restore();
        
        // Debug hitbox
        //gc.setStroke(Color.RED);
        //gc.strokeRect(x, y, width, height);
    }
    
    private void renderIdlePlayer(GraphicsContext gc, Color tanColor, Color blackColor, Color redColor, Color whiteColor) {
        // Head
        gc.setFill(tanColor);
        gc.fillRect(x + 10, y - 80, 50, 80);
        
        // Body
        gc.setFill(whiteColor);
        gc.fillRect(x + 11, y - 10, 50, 105);
        
        // Headscarf
        gc.setFill(redColor);
        gc.fillRect(x + 9, y - 80, 12, 80);
        gc.fillRect(x + 9, y - 80, 60, 12);
        
        
        // Eye
        gc.setFill(blackColor);
        gc.fillRect(x + 10, y - 88, 60, 8);
        gc.fillOval(x + 12, y + 3, 2, 2);
        
    }
    
    
}
