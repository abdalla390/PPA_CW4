import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Helper class for displaying debug information during development
 */
public class DebugHelper {
    private static boolean enabled = true;
    
    /**
     * Enable or disable all debug visualizations
     */
    public static void setEnabled(boolean isEnabled) {
        enabled = isEnabled;
    }
    
    /**
     * Draw the bounding box of a game object
     */
    public static void drawBoundingBox(GraphicsContext gc, GameObject obj) {
        if (!enabled) return;
        
        gc.save();
        gc.setStroke(Color.LIME);
        gc.setLineWidth(1);
        gc.setLineDashes(4, 4);
        gc.strokeRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
        gc.restore();
    }
    
    /**
     * Draw coordinate axes at the object's position
     */
    public static void drawCoordinateAxes(GraphicsContext gc, GameObject obj) {
        if (!enabled) return;
        
        double x = obj.getX();
        double y = obj.getY();
        
        gc.save();
        
        // X-axis (red)
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeLine(x, y, x + 50, y);
        
        // Y-axis (green)
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(1);
        gc.strokeLine(x, y, x, y - 50);
        
        gc.restore();
    }
    
    /**
     * Draw position information text
     */
    public static void drawPositionInfo(GraphicsContext gc, GameObject obj) {
        if (!enabled) return;
        
        gc.save();
        gc.setFill(Color.WHITE);
        gc.fillText(
            "(" + (int)obj.getX() + "," + (int)obj.getY() + ")", 
            obj.getX(), obj.getY() - 5
        );
        gc.restore();
    }
    
    /**
     * Display values in the top-left corner (for performance metrics, etc.)
     */
    public static void displayValues(GraphicsContext gc, String[] labels, Object[] values) {
        if (!enabled) return;
        
        gc.save();
        gc.setFill(Color.BLACK);
        gc.fillRect(10, 10, 200, 20 * labels.length);
        
        gc.setFill(Color.WHITE);
        for (int i = 0; i < labels.length; i++) {
            gc.fillText(labels[i] + ": " + values[i], 15, 25 + (i * 20));
        }
        gc.restore();
    }
}