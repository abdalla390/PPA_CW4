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
        // Sky
        gc.setFill(Color.web("#87CEEB")); 
        gc.fillRect(camera.getX(), 0, gameCanvas.getWidth(), gameCanvas.getHeight() * 0.86);
        
        double parallaxFactor = 0.4;
        double duneOffset = camera.getX() * parallaxFactor;
        
        // Draw only visible dune ranges
        int start = (int)(duneOffset / 500) - 1;
        int end = start + (int)(gameCanvas.getWidth() / 500) + 2;
        
        for (int i = start; i < end; i++) {
            double baseX = i * 500 - (duneOffset % 500);
            
            // First sand dune (darker)
            gc.setFill(Color.web("#D4B483", 0.7));  
            gc.beginPath();
            gc.moveTo(baseX, 620);
            gc.bezierCurveTo(
                baseX + 125, 520,
                baseX + 375, 520, 
                baseX + 500, 620  
            );
            gc.lineTo(baseX + 500, 620);
            gc.lineTo(baseX, 620);
            gc.closePath();
            gc.fill();
            
            // Add a subtle highlight for depth
            gc.setStroke(Color.web("#F5DEB3", 0.3));
            gc.setLineWidth(1);
            gc.beginPath();
            gc.moveTo(baseX + 100, 590);
            gc.quadraticCurveTo(
                baseX + 250, 540,
                baseX + 400, 590
            );
            gc.stroke();
            
            // Smaller overlapping dune with slightly different color for variety
            if (i % 2 == 0) {
                gc.setFill(Color.web("#C19A6B", 0.5)); 
                gc.beginPath();
                gc.moveTo(baseX + 150, 620);
                gc.bezierCurveTo(
                    baseX + 250, 570, 
                    baseX + 350, 570, 
                    baseX + 450, 620  
                );
                gc.lineTo(baseX + 450, 620);
                gc.lineTo(baseX + 150, 620);
                gc.closePath();
                gc.fill();
                }
            }
        
        // Ground (moves with camera)
        gc.setFill(Color.web("#E9C893")); // Sand color
        gc.fillRect(camera.getX(), gameCanvas.getHeight() * 0.86, 
                    gameCanvas.getWidth(), gameCanvas.getHeight() * 0.14);
        
        // Ground line - only draw what's visible
        gc.setStroke(Color.web("#A69185")); 
        gc.setLineWidth(2);
        gc.strokeLine(camera.getX(), gameCanvas.getHeight() * 0.86, 
                    camera.getX() + gameCanvas.getWidth(), gameCanvas.getHeight() * 0.86);
    }
}