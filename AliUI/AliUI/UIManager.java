import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;

public class UIManager {
    private Label scoreLabel;
    private HBox heartsBox;
    private ScoreManager scoreManager;
    private AnimationTimer uiUpdater;

    public UIManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    // Create the menu bar (File -> Quit, Help -> About)
    public MenuBar createMenuBar() {
        Menu fileMenu = new Menu("File");
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(quitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        return new MenuBar(fileMenu, helpMenu);
    }

    // Create the HUD (Score + Hearts)
    public VBox createHUD() {
        scoreLabel = new Label("Score: 1000");
        scoreLabel.setFont(new Font("Arial", 20));
        scoreLabel.setTextFill(Color.WHITE);

        heartsBox = new HBox(5);
        heartsBox.setAlignment(Pos.CENTER_LEFT);
        updateHearts(3);

        // Update the score UI every frame
        uiUpdater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateScore(scoreManager.getCurrentScore());
            }
        };
        uiUpdater.start();

        VBox hud = new VBox(10, scoreLabel, heartsBox);
        hud.setAlignment(Pos.TOP_LEFT);
        return hud;
    }

    // Update the score display
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    // Update hearts display
    public void updateHearts(int health) {
        heartsBox.getChildren().clear();
        for (int i = 0; i < health; i++) {
            Label heart = new Label("❤");
            heart.setFont(new Font(20));
            heartsBox.getChildren().add(heart);
        }
    }

    // Show Game Over Dialog
    public void showGameOverDialog(boolean isWin, int finalScore) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(isWin ? "You Win!" : "Game Over");
        alert.setContentText("Final Score: " + finalScore);
        alert.showAndWait();
    }

    // Show About Dialog
    private void showAboutDialog() {
        Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutDialog.setTitle("About");
        aboutDialog.setHeaderText("Bedouin Madness");
        aboutDialog.setContentText("Proudly presented by Team Bedouin:\n  Member 1: [Hussain] - Engine & Game Loop\n  Member 2: [Abdalla] - Player Character & Controls\n  Member 3: [Abdelrahman] - Level Design & Environment\n  Member 4: [Mohammed] - Enemy Systems\n  Member 5: [Ali Alharmoodi] - UI & Visual Effects\n\n          A desert-themed ninja platformer.");
        aboutDialog.showAndWait();
    }
}
