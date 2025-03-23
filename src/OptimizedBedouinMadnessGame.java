import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class OptimizedBedouinMadnessGame extends Application {
    private GameEngine gameEngine;
    private GameView gameView;
    private UIManager uiManager;
    private InputHandler inputHandler;
    
    // Debug mode flag
    private boolean debugMode = false;
    
    @Override
    public void start(Stage stage) {
        // Create canvas with appropriate size
        Canvas canvas = new Canvas(1280, 720);
        
        // Initialize core components
        ScoreManager scoreManager = new ScoreManager();
        uiManager = new UIManager(scoreManager);
        uiManager.setPrimaryStage(stage);
        
        // Create GameView first (but don't set GameEngine yet)
        gameView = new GameView(canvas, null);
        
        // Now create GameEngine with the GameView
        gameEngine = new GameEngine(uiManager, scoreManager, canvas.getGraphicsContext2D(), gameView);
        
        // Connect GameView to GameEngine
        gameView.setGameEngine(gameEngine);
        
        // Set debug mode
        gameEngine.setDebugMode(debugMode);
        gameView.setDebugMode(debugMode);
        
        // Setup layout
        BorderPane root = new BorderPane();
        root.setTop(uiManager.createMenuBar());
        
        // Debug controls - only visible in development
        HBox debugControls = new HBox(10);
        debugControls.setStyle("-fx-background-color: #333333; -fx-padding: 5px;");
        CheckBox debugCheckbox = new CheckBox("Debug Mode");
        debugCheckbox.setTextFill(javafx.scene.paint.Color.WHITE);
        debugCheckbox.setSelected(debugMode);
        debugCheckbox.setOnAction(e -> {
            debugMode = debugCheckbox.isSelected();
            gameEngine.setDebugMode(debugMode);
            gameView.setDebugMode(debugMode);
        });
        
        Button gcButton = new Button("Force GC");
        gcButton.setOnAction(e -> System.gc());
        
        Label memoryLabel = new Label("Memory: ");
        memoryLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        
        debugControls.getChildren().addAll(debugCheckbox, gcButton, memoryLabel);
        
        // Optional: Memory usage display
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                if (debugMode) {
                    Runtime rt = Runtime.getRuntime();
                    long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
                    memoryLabel.setText(String.format("Memory: %d MB", usedMB));
                }
            }
        }.start();
        
        // Create VBox for HUD at the top of the center area
        VBox centerVBox = new VBox();
        if (debugMode) centerVBox.getChildren().add(debugControls);
        centerVBox.getChildren().addAll(uiManager.createHUD(1), canvas);
        VBox.setVgrow(canvas, Priority.ALWAYS);
        
        // Use a StackPane to allow overlay of instructions panel
        StackPane gameAreaStack = new StackPane();
        gameAreaStack.getChildren().add(centerVBox);
        root.setCenter(gameAreaStack);
        
        // Create the scene
        Scene scene = new Scene(root, 1280, 720);
        
        // Add input handling - create Player first, then initialize the game
        gameEngine.initializeGame();
        
        // Pause the game initially
        gameEngine.pauseGame();
        
        // Create input handler
        inputHandler = new InputHandler(scene, gameEngine.getPlayer());
        gameEngine.setInputHandler(inputHandler);
        
        // Configure stage
        stage.setTitle("Bedouin Madness (Optimized)");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
        // Handle close request
        stage.setOnCloseRequest(e -> {
            if (gameEngine != null) {
                gameEngine.kill();
            }
        });
        
        // Connect menu actions
        uiManager.setResetGameHandler(e -> gameEngine.resetGame());
        
        // Create a simple instructions panel (since we may not have the GameInstructions class)
        VBox instructionsPanel = createInstructionsPanel();
        
        // Add start button functionality
        Button startButton = new Button("Start Game");
        startButton.setPrefWidth(150);
        startButton.setOnAction(e -> {
            gameAreaStack.getChildren().remove(instructionsPanel);
            gameEngine.resumeGame();
        });
        
        instructionsPanel.getChildren().add(startButton);
        
        // Show instructions panel
        gameAreaStack.getChildren().add(instructionsPanel);
        StackPane.setAlignment(instructionsPanel, Pos.CENTER);
    }
    
    // Simple method to create an instructions panel if GameInstructions is not available
    private VBox createInstructionsPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new javafx.geometry.Insets(20));
        panel.setAlignment(Pos.CENTER);
        panel.setBackground(new javafx.scene.layout.Background(
            new javafx.scene.layout.BackgroundFill(
                javafx.scene.paint.Color.web("#333333", 0.85),
                new javafx.scene.layout.CornerRadii(10),
                javafx.geometry.Insets.EMPTY
            )
        ));
        panel.setMaxWidth(500);
        panel.setMaxHeight(400);
        
        Label titleLabel = new Label("Bedouin Madness - Game Instructions");
        titleLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        
        Label controlsLabel = new Label(
            "Controls:\n" +
            "Move Left: ← or A\n" +
            "Move Right: → or D\n" +
            "Jump: SPACE"
        );
        controlsLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        
        Label objectiveLabel = new Label(
            "Objective: Guide your ninja through the desert levels, " +
            "avoid enemies and obstacles, and reach the golden flag at the end of each level."
        );
        objectiveLabel.setWrapText(true);
        objectiveLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        
        panel.getChildren().addAll(titleLabel, controlsLabel, objectiveLabel);
        
        return panel;
    }
    
    @Override
    public void stop() {
        // Cleanup resources
        if (gameEngine != null) {
            gameEngine.kill();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}