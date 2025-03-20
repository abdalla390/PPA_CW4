abstract class GameObject {
    protected double x, y;
    protected double width, height;
    protected double initialX; // Added for Snake compatibility

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.initialX = x; // Initialize initialX
        this.width = width;
        this.height = height;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}