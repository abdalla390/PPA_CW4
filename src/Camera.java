public class Camera {
    private double x;
    private double y;
    private double viewportWidth;
    private double viewportHeight;
    private double levelWidth;
    private double levelHeight;
    
    // Smoothing factor for camera movement (lower = smoother)
    private double smoothingFactor = 0.1;
    private double targetX;
    
    public Camera(double viewportWidth, double viewportHeight) {
        this.x = 0;
        this.y = 0;
        this.targetX = 0;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }
    
    public void setLevelBounds(double levelWidth, double levelHeight) {
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }
    
    public void follow(GameObject target) {
        // Center the camera on the target with smooth movement
        targetX = target.getX() - viewportWidth / 2 + target.getWidth() / 2;
        
        // Smooth camera movement
        x += (targetX - x) * smoothingFactor;
        
        // Keep camera within level bounds
        if (x < 0) x = 0;
        if (x > levelWidth - viewportWidth) x = levelWidth - viewportWidth;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    // Transform world coordinates to screen coordinates
    public double worldToScreenX(double worldX) {
        return worldX - x;
    }
    
    public double worldToScreenY(double worldY) {
        return worldY - y;
    }
    
    // Check if an object is visible on screen
    public boolean isVisible(GameObject obj) {
        return !(obj.getX() + obj.getWidth() < x || 
                obj.getX() > x + viewportWidth ||
                obj.getY() + obj.getHeight() < y ||
                obj.getY() > y + viewportHeight);
    }
}