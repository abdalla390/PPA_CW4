import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class BedouinMadnessGame extends Application {
    private GameEngine gameEngine;
    private GameView gameView;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(1280, 720); // Matches viewport from README
        gameEngine = new GameEngine();
        gameView = new GameView(canvas, gameEngine);

        // Initialize a test level with enemies (your part)
        EnemyFactory enemyFactory = new EnemyFactory();
        gameEngine.startLevel(1); // Start level 1, will spawn Scorpions

        Scene scene = new Scene(new javafx.scene.layout.Pane(canvas), 1280, 720);
        stage.setTitle("Bedouin Madness");
        stage.setScene(scene);
        stage.show();

        // Animation loop (simplified for now)
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                gameEngine.update(1.0 / 60.0); // 60 FPS assumption
                gameView.render();
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}