/**
 * UI Manager class that handles all user interface elements in the game.
 * Creates and manages menus, HUDs, dialogs and other visual components.
 * 
 * @author Abdalla Alhajeri, Mohamed Alketbi, Ali Alharmoodi, Abdelrahman Almatrooshi, Hussain Albeshri
 * @version 1.0
 */
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.effect.DropShadow;

public class UIManager {
    private ScoreManager scoreManager;
    private HBox heartContainer;
    private Text scoreText;
    private Text levelText;
    private Stage primaryStage;
    private javafx.event.EventHandler<ActionEvent> resetGameHandler;
    private javafx.event.EventHandler<ActionEvent> continueHandler;
    private boolean darkMode = true;
    private VBox hudContainer;
    private Runnable applicationShutdownHandler;

    /**
     * Constructs a new UIManager with the specified score manager.
     * 
     * @param scoreManager The score manager to use for updating score displays.
     */
    public UIManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    /**
     * Sets the primary stage for displaying dialogs.
     * 
     * @param stage The primary stage.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Sets the handler for game reset actions.
     * 
     * @param handler The event handler for reset game actions.
     */
    public void setResetGameHandler(javafx.event.EventHandler<ActionEvent> handler) {
        this.resetGameHandler = handler;
    }

    /**
     * Sets the handler for continue game actions.
     * 
     * @param handler The event handler for continue game actions.
     */
    public void setContinueHandler(javafx.event.EventHandler<ActionEvent> handler) {
        this.continueHandler = handler;
    }

    /**
     * Sets dark mode display setting.
     * 
     * @param darkMode True to enable dark mode, false otherwise.
     */
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
        Platform.runLater(() -> updateUIColors());
    }

    /**
     * Updates UI colors based on dark mode setting.
     */
    private void updateUIColors() {
        if (hudContainer == null) return;

        // Changed text colors to black for better readability
        if (levelText != null) levelText.setFill(Color.BLACK);
        if (scoreText != null) scoreText.setFill(Color.BLACK);
    }

    /**
     * Creates the game menu bar.
     * 
     * @return The created menu bar.
     */
    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newGameItem = new MenuItem("New Game");
        newGameItem.setOnAction(e -> {
                    if (resetGameHandler != null) resetGameHandler.handle(e);
            });

        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(e -> applicationShutdownHandler.run());

        fileMenu.getItems().addAll(newGameItem, new SeparatorMenuItem(), quitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());

        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    /**
     * Creates the heads-up display (HUD).
     * 
     * @param levelNumber The current level number.
     * @return The created HUD container.
     */
    public VBox createHUD(int levelNumber) {
        hudContainer = new VBox(10);
        hudContainer.setPadding(new Insets(10));
                               
        levelText = new Text("LEVEL " + levelNumber);
        levelText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        levelText.setFill(Color.BLACK);
        StackPane levelPane = new StackPane(levelText);
        levelPane.setAlignment(Pos.CENTER);

        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);

        heartContainer = new HBox(5);
        updateHearts(3);

        scoreText = new Text("SCORE: 0");
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        scoreText.setFill(Color.BLACK); 

        statsRow.getChildren().addAll(heartContainer, scoreText);
        hudContainer.getChildren().addAll(levelPane, statsRow);
        updateUIColors();
        return hudContainer;
    }

    /**
     * Updates the score display.
     * 
     * @param score The score to display.
     */
    public void updateScore(int score) {
        if (scoreText != null) scoreText.setText("SCORE: " + score);
    }

    /**
     * Updates the heart display based on player health.
     * 
     * @param health The number of hearts to display.
     */
    public void updateHearts(int health) {
        if (heartContainer != null) {
            heartContainer.getChildren().clear();
            for (int i = 0; i < health; i++) {
                heartContainer.getChildren().add(createHeart());
            }
        }
    }

    /**
     * Creates a heart shape for the health display.
     * 
     * @return A pane containing the heart shape.
     */
    private Pane createHeart() {
        Pane heart = new Pane();
        heart.setPrefSize(30, 30);
        javafx.scene.shape.Path heartPath = new javafx.scene.shape.Path();
        heartPath.getElements().addAll(
            new javafx.scene.shape.MoveTo(15, 6),
            new javafx.scene.shape.CubicCurveTo(15, 6, 12, 0, 7.5, 0),
            new javafx.scene.shape.CubicCurveTo(3, 0, 0, 4, 0, 9),
            new javafx.scene.shape.CubicCurveTo(0, 14, 5, 19, 15, 27),
            new javafx.scene.shape.CubicCurveTo(25, 19, 30, 14, 30, 9),
            new javafx.scene.shape.CubicCurveTo(30, 4, 27, 0, 22.5, 0),
            new javafx.scene.shape.CubicCurveTo(18, 0, 15, 6, 15, 6)
        );
        heartPath.setFill(Color.web("#D22F27"));
        heartPath.setStroke(Color.BLACK);
        heartPath.setStrokeWidth(1);
        heart.getChildren().add(heartPath);
        return heart;
    }

    /**
     * Updates the level display.
     * 
     * @param level The level number to display.
     */
    public void updateLevel(int level) {
        if (levelText != null) levelText.setText("LEVEL " + level);
    }

    /**
     * Shows a dialog when the player dies.
     * 
     * @param remainingHearts Number of hearts player has left.
     * @param continueAction Action to execute if player chooses to continue.
     * @param quitAction Action to execute if player chooses to quit.
     */
    public void showDeathDialog(int remainingHearts, Runnable continueAction, Runnable quitAction) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("You Died!");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(350);
        dialogPane.setPrefHeight(250);

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #b03030;"); // Simple red background

        Text headerText = new Text("YOU DIED!");
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerText.setFill(Color.WHITE);

        HBox heartsBox = new HBox(5);
        heartsBox.setAlignment(Pos.CENTER);

        // Add heart icons for remaining hearts
        for (int i = 0; i < remainingHearts; i++) {
            heartsBox.getChildren().add(createHeart());
        }

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button continueButton = new Button("CONTINUE");
        continueButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 16; -fx-background-color: #f0f0f0;");
        continueButton.setOnAction(e -> {
                    dialogPane.getScene().getWindow().hide();
                    if (continueAction != null) {
                        continueAction.run();
                    }
            });

        Button quitButton = new Button("QUIT");
        quitButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 16; -fx-background-color: #f0f0f0;");
        quitButton.setOnAction(e -> {
                    dialogPane.getScene().getWindow().hide();
                    if (quitAction != null) {
                        quitAction.run();
                    }
            });

        buttonBox.getChildren().addAll(continueButton, quitButton);

        content.getChildren().addAll(headerText, heartsBox, buttonBox);

        dialogPane.getButtonTypes().clear();
        dialogPane.setContent(content);

        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setOnCloseRequest(event -> event.consume());

        dialog.showAndWait();
    }

    /**
     * Shows a game over dialog with the final score.
     * 
     * @param isWin Whether the player won or lost.
     * @param finalScore The final score to display.
     * @param lastLevel The last level reached.
     */
    public void showGameOverDialog(boolean isWin, int finalScore, int lastLevel) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(isWin ? "Level Complete!" : "Game Over");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(400);
        dialogPane.setPrefHeight(300);

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));

        // Simple background color instead of gradient
        if (isWin) {
            content.setStyle("-fx-background-color: #3a7e3a;"); // Green for win
        } else {
            content.setStyle("-fx-background-color: #b03030;"); // Red for game over
        }

        Text headerText = new Text(isWin ? "LEVEL COMPLETE!" : "GAME OVER");
        headerText.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerText.setFill(Color.WHITE);

        Text scoreText = new Text("SCORE: " + finalScore);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        scoreText.setFill(Color.WHITE);

        Text levelText = new Text("LEVEL: " + lastLevel);
        levelText.setFont(Font.font("Arial", 18));
        levelText.setFill(Color.WHITE);

        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button playAgainButton = new Button("PLAY AGAIN");
        playAgainButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 16; -fx-background-color: #f0f0f0;");
        playAgainButton.setOnAction(e -> {
                    dialogPane.getScene().getWindow().hide();
                    if (resetGameHandler != null) resetGameHandler.handle(e);
            });

        Button quitButton = new Button("QUIT");
        quitButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 16; -fx-background-color: #f0f0f0;");
        quitButton.setOnAction(e -> {
                    dialogPane.getScene().getWindow().hide();
                    applicationShutdownHandler.run();
            });

        buttonBox.getChildren().addAll(playAgainButton, quitButton);

        // Simple layout
        content.getChildren().addAll(headerText, scoreText, levelText, buttonBox);

        dialogPane.getButtonTypes().clear();
        dialogPane.setContent(content);

        dialog.initOwner(primaryStage);
        dialog.setOnCloseRequest(event -> event.consume());
        dialog.showAndWait();
    }

    /**
     * Shows a game over dialog with the final score.
     * Overloaded method that doesn't include level information.
     * 
     * @param isWin Whether the player won or lost.
     * @param finalScore The final score to display.
     */
    public void showGameOverDialog(boolean isWin, int finalScore) {
        showGameOverDialog(isWin, finalScore, 1);
    }

    /**
     * Shows an about dialog with game and team information.
     */
    public void showAboutDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("About Bedouin Madness");

        DialogPane dialogPane = dialog.getDialogPane();

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        Text gameTitle = new Text("Bedouin Madness");
        gameTitle.setFont(Font.font(24));

        Text teamMembers = new Text(
                "Team Members:\n" +
                "Hussain - Engine & Game Loop\n" +
                "Abdalla - Player Character & Controls\n" +
                "Abdelrahman - Level Design & Environment\n" +
                "Mohammed - Enemy Systems\n" +
                "Ali - UI & Visual Effects"
            );
        teamMembers.setFont(Font.font(14));

        content.getChildren().addAll(gameTitle, teamMembers);

        dialogPane.setContent(content);
        dialogPane.getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
    
    public void setApplicationShutdownHandler(Runnable handler) {
        this.applicationShutdownHandler = handler;
    }
}