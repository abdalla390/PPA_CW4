import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

/**
 * Game View class defining and drawing the game content.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
public class GameView {
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private GameEngine gameEngine;
    private Camera camera;
    
    /**
     * The GameView Constructor defining the canvans and engine of the game.
     * 
     * @param canvas The canvas of the game where the game content to be draw on.
     * @param engine The engine of the game where all the contents of the game are called from.
     */
    public GameView(Canvas canvas, GameEngine engine) {
        this.gameCanvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.gameEngine = engine;
        this.camera = new Camera(canvas.getWidth(), canvas.getHeight());
    }
    
    
     /**
     * Sets the game engine reference.
     * Used when the game engine is created after the view.
     * 
     * @param engine The game engine to associate with this view.
     */
    public void setGameEngine(GameEngine engine) {
        this.gameEngine = engine;
    }
    
    /**
     * Gets the camera object used for view transformations.
     * 
     * @return The camera instance used by this view.
     */
    public Camera getCamera() {
        return camera;
    }
    
     /**
     * Renders the game world onto the canvas.
     * Clears the canvas, updates camera position to follow the player,
     * applies camera transformation, renders the background with parallax,
     * and draws only the visible game objects for better performance.
     * Also draws debug information when debug mode is enabled.
     */
    public void render() {
        
        if (gameEngine == null) return;
        
        // Clear the canvas
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        
        // Get player and level
        Player player = gameEngine.getPlayer();
        Level currentLevel = gameEngine.getCurrentLevel();
        
        if (player == null || currentLevel == null) return;
        
        // Update camera to follow player
        camera.setLevelBounds(currentLevel.getLevelWidth(), currentLevel.getLevelHeight());
        camera.follow(player);
        
        // Save the current state
        gc.save();
        
        // Apply camera transformation
        gc.translate(-camera.getX(), 0); // Only transform horizontally
        
        // Draw background with parallax effect
        drawBackground(currentLevel);
        
        // Get only visible objects for rendering
        List<GameObject> visibleObjects = currentLevel.getVisibleObjects(
            camera.getX(), gameCanvas.getWidth());
        
        // Draw all visible objects
        for (GameObject obj : visibleObjects) {
            obj.render(gc);
        }
        
        // Draw player on top of other objects
        if (player.isActive()) {
            player.render(gc);
        }
        
        // Restore the original state
        gc.restore();
        
    }
    
    /**
     * Draws the background with a parallax scrolling effect.
     * Creates a layered background with sky, distant mountains that move slower
     * than the player (parallax), and a ground section that moves with the camera.
     * The parallax effect creates a sense of depth in the 2D world.
     * 
     * @param level The current level being rendered.
     */
    private void drawBackground(Level level) {
        // Use a more efficient background rendering method
        
        // Sky (fixed, doesn't move with camera)
        gc.setFill(Color.web("#87CEEB")); // Sky blue
        gc.fillRect(camera.getX(), 0, gameCanvas.getWidth(), gameCanvas.getHeight() * 0.86);
        
        // Distant mountains (slow parallax) - only draw what's visible
        double parallaxFactor = 0.4;
        double mountainOffset = camera.getX() * parallaxFactor;
        gc.setFill(Color.web("#808080", 0.5)); // Light gray mountains
        
        // Draw only visible mountain ranges
        int start = (int)(mountainOffset / 500) - 1;
        int end = start + (int)(gameCanvas.getWidth() / 500) + 2;
        
        for (int i = start; i < end; i++) {
            double baseX = i * 500 - (mountainOffset % 500);
            gc.fillPolygon(
                new double[]{baseX, baseX + 250, baseX + 500},
                new double[]{620, 450, 620},
                3
            );
        }
        
        // Ground (moves with camera)
        gc.setFill(Color.web("#E9C893")); // Sand color
        gc.fillRect(camera.getX(), gameCanvas.getHeight() * 0.86, 
                    gameCanvas.getWidth(), gameCanvas.getHeight() * 0.14);
        
        // Ground line - only draw what's visible
        gc.setStroke(Color.web("#A69185")); // Darker line for ground
        gc.setLineWidth(2);
        gc.strokeLine(camera.getX(), gameCanvas.getHeight() * 0.86, 
                    camera.getX() + gameCanvas.getWidth(), gameCanvas.getHeight() * 0.86);
    }
}