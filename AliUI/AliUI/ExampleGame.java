import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ExampleGame extends Application {
    private UIManager uiManager;
    private ScoreManager scoreManager;
    private int playerHealth = 3;

    @Override
    public void start(Stage primaryStage) {
        scoreManager = new ScoreManager();
        uiManager = new UIManager(scoreManager);

        BorderPane root = new BorderPane();
        root.setTop(uiManager.createMenuBar());  //  Menu Bar
        root.setLeft(uiManager.createHUD());     //  HUD

        // Button to manually dmg player (simulating external damage)
        Button takeDamageButton = new Button("Take Damage");
        takeDamageButton.setOnAction(e -> takeDamage());

        // Button to restart level (rest health + score)
        Button startLevelButton = new Button("Start Level");
        startLevelButton.setOnAction(e -> startNewLevel());

        VBox buttons = new VBox(10, startLevelButton, takeDamageButton);
        root.setCenter(buttons);

        Scene scene = new Scene(root, 1920, 1080); //game window resolution
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bedouin Madness");
        primaryStage.show();

        startNewLevel();  // Automatically start on launch
    }

    private void startNewLevel() {
        playerHealth = 3;
        uiManager.updateHearts(playerHealth);
        scoreManager.startLevelTimer();
    }

    private void takeDamage() {
        if (playerHealth > 0) {
            playerHealth--;
            uiManager.updateHearts(playerHealth);

            if (playerHealth <= 0) {
                endGame();
            }
        }
    }

    private void endGame() {
        int finalScore = scoreManager.finalizeLevelScore();

        // Run the dialog in the JavaFX UI thread
        Platform.runLater(() -> uiManager.showGameOverDialog(false, finalScore));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
