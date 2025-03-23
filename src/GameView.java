import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class GameView {
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private GameEngine gameEngine;
    private Camera camera;
    
    // Performance optimization
    private boolean debugMode = false;
    private long frameCount = 0;

    public GameView(Canvas canvas, GameEngine engine) {
        this.gameCanvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.gameEngine = engine;
        this.camera = new Camera(canvas.getWidth(), canvas.getHeight());
    }
    
    public void setGameEngine(GameEngine engine) {
        this.gameEngine = engine;
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    public void render() {
        frameCount++;
        
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
        
        // Draw debug info if enabled
        if (debugMode && frameCount % 30 == 0) { // Only every 30 frames
            gc.setFill(Color.WHITE);
            gc.fillText("Camera: " + (int)camera.getX() + ", Player: " + (int)player.getX() + 
                      ", Objects: " + visibleObjects.size(), 10, 20);
        }
    }
    
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