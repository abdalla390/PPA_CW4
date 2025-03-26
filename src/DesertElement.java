import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents decorative elements in the desert environment.
 * These elements include sand dunes, cacti, and rock formations that
 * add visual interest and thematic consistency to the desert landscape.
 * Unlike obstacles, these elements are purely decorative and do not
 * affect gameplay or collision detection.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class DesertElement extends GameObject {
    private ElementType type;
    
    /**
     * Creates a new desert environment element.
     * 
     * @param x The x-coordinate of the element in the game world.
     * @param y The y-coordinate of the element in the game world.
     * @param width The width of the element.
     * @param height The height of the element.
     * @param type The type of desert element (SAND_DUNE, CACTUS, or ROCK).
     */
    public DesertElement(double x, double y, double width, double height, ElementType type) {
        super(x, y, width, height);
        this.type = type;
    }
    
    /**
     * Gets the type of this desert element.
     * 
     * @return The element type (SAND_DUNE, CACTUS, or ROCK).
     */
    public ElementType getType() {
        return type;
    }
    
    /**
     * Updates the element's state.
     * Desert elements are static, so this method doesn't perform any actions.
     * 
     * @param deltaTime Time elapsed since the last update in seconds.
     */
    @Override
    public void update(double deltaTime) {
    }
    
    /**
     * Renders the desert element on the screen.
     * The visual appearance depends on the element type.
     * 
     * @param gc The graphics context to draw on.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        switch(type) {
            case SAND_DUNE:
                renderSandDune(gc);
                break;
            case CACTUS:
                renderCactus(gc);
                break;
            case ROCK:
                renderRock(gc);
                break;
        }
    }
    
    /**
     * Renders a sand dune as a rounded hill shape.
     * Uses quadratic curves to create a smooth, natural-looking dune
     * with subtle details and shading.
     * 
     * @param gc The graphics context to draw on.
     */
    private void renderSandDune(GraphicsContext gc) {
        // Draw a sand dune (rounded hill shape)
        gc.setFill(Color.web("#E9C893")); 
        
        // Create a rounded hill shape
        gc.beginPath();
        gc.moveTo(x, y + height);
        gc.quadraticCurveTo(x + width/2, y, x + width, y + height);
        gc.lineTo(x + width, y + height);
        gc.lineTo(x, y + height);
        gc.closePath();
        gc.fill();
        
        // Add some detail/shadow
        gc.setStroke(Color.web("#D4B483"));
        gc.setLineWidth(1);
        gc.strokeLine(x + width*0.2, y + height*0.7, x + width*0.8, y + height*0.7);
    }
    
    /**
     * Renders a cactus with a main stem and side arms.
     * Uses simple geometric shapes and line details to create
     * a recognizable desert cactus.
     * 
     * @param gc The graphics context to draw on.
     */
    private void renderCactus(GraphicsContext gc) {
        // Draw a simple cactus
        gc.setFill(Color.web("#2D8633")); // Cactus green
        
        // Main stem
        gc.fillRect(x + width/2 - 5, y, 10, height);
        
        // Arms
        gc.fillRect(x, y + height*0.3, width/2, 8);
        gc.fillRect(x + width/2, y + height*0.6, width/2, 8);
        
        // Detail lines
        gc.setStroke(Color.web("#1A5E20"));
        gc.setLineWidth(1);
        gc.strokeLine(x + width/2 - 2, y, x + width/2 - 2, y + height);
        gc.strokeLine(x + width/2 + 2, y, x + width/2 + 2, y + height);
    }
    
    /**
     * Renders a rock formation using overlapping oval shapes.
     * Creates a cluster of rocks with subtle highlights to add
     * visual interest and dimension.
     * 
     * @param gc The graphics context to draw on.
     */
    private void renderRock(GraphicsContext gc) {
        // Draw a rock formation
        gc.setFill(Color.web("#808080")); // Gray for rocks
        
        // Main rock shape
        gc.fillOval(x, y, width, height);
        
        // Add some smaller rocks
        gc.fillOval(x - width*0.1, y + height*0.6, width*0.3, height*0.4);
        gc.fillOval(x + width*0.8, y + height*0.7, width*0.3, height*0.3);
        
        // Add some detail/highlight
        gc.setStroke(Color.web("#A0A0A0"));
        gc.setLineWidth(1);
        gc.strokeOval(x + width*0.3, y + height*0.2, width*0.2, height*0.1);
    }
}