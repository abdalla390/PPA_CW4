import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameView {
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private GameEngine gameEngine;

    public GameView(Canvas canvas, GameEngine engine) {
        this.gameCanvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.gameEngine = engine;
    }

    public void render() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        DesertBackground background = new DesertBackground();
        background.render(gc);

        gameEngine.getPlayer().render(gc);

        for (GameObject obj : gameEngine.getCurrentLevel().getAllObjects()) {
            if (obj instanceof Enemy) {
                ((Enemy) obj).render(gc);
            }
        }
    }
}