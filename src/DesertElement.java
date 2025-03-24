import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a decorative element in the desert environment.
 * These elements can be sand dunes, cacti, or rock formations.
 */
public class DesertElement extends GameObject {
    private ElementType type;
    
    /**
     * Creates a new desert environment element.
     * 
     * @param x The x-coordinate of the element
     * @param y The y-coordinate of the element
     * @param width The width of the element
     * @param height The height of the element
     * @param type The type of desert element
     */
    public DesertElement(double x, double y, double width, double height, ElementType type) {
        super(x, y, width, height);
        this.type = type;
    }
    
    /**
     * @return The type of the desert element
     */
    public ElementType getType() {
        return type;
    }
    
    @Override
    public void update(double deltaTime) {
        // Desert elements are static, no update needed
    }
    
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
    
    private void renderSandDune(GraphicsContext gc) {
        // Draw a sand dune (rounded hill shape)
        gc.setFill(Color.web("#E9C893")); // Sand color
        
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