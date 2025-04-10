Project Overview

Game Name: Bedouin Madness
Team Members:

Member 1: [Hussain] - Engine & Game Loop
Member 2: [Abdalla] - Player Character & Controls
Member 3: [Abdelrahman] - Level Design & Environment
Member 4: [Mohammed] - Enemy Systems
Member 5: [Ali] - UI & Visual Effects


Timeline: March 12 - March 28, 2025
Repository: https://github.com/abdalla390/PPA_CW4.git

Class Structure & Responsibilities
Core System (Hussain)
GameEngine
	├── update(deltaTime): void - Main game loop
	├── startLevel(levelNumber): void - Initialize level
	├── endLevel(): void - Handle level completion
	├── calculateScore(): int - Calculate score based on time
	└── checkGameOver(): boolean - Check if player has lost all hearts

Integration Points:

	Will call Player.update(), Level.update(), and Enemy.update() each frame
	Will track time spent in level for score calculation
	Will notify UIManager when hearts, score, or game state changes

Player System (Abdalla)
Player extends GameObject
	├── update(deltaTime): void - Update player state
	├── render(gc): void - Draw player using vector graphics
	├── jump(): void - Handle jump mechanics
	├── takeDamage(): void - Reduce health, trigger invincibility
	└── getHealth(): int - Return current heart count
	
Vector Drawing Specifications:

	Player character: 30px × 50px ninja silhouette
	Core colors: #C19A6B (tan), #000000 (black), #D22F27 (red accent)
	Animation frames: 3 frames for running, 2 for jumping, 1 for idle

Animation Constants:

	Jump height: 150px
	Jump duration: 0.8 seconds
	Movement speed: 200px/second

Level System (Abdelrahman)
Level
	├── initialize(): void - Set up level layout
	├── update(deltaTime): void - Update all level elements
	├── getAllObjects(): List<GameObject> - Return collidable objects
	├── isCompleted(): boolean - Check if level is complete
	└── getLevelNumber(): int - Return current level number

LevelFactory
	└── createLevel(levelNumber): Level - Generate specific level
	
Level Design Guidelines:

	All levels: 1280px wide × 720px high viewport
	Level progression: Each level extends 2000-3000px horizontally
	Desert theme elements: sand dunes, cacti, rock formations, quicksand
	Level 1-3: Basic platforming, few enemies
	Level 4-7: More complex jumping patterns, moving platforms
	Level 8-10: Advanced enemy patterns, hazardous terrain

Level Completion Trigger:
	Golden desert flag at the end of each level
	Must be touched by player to complete level

Enemy System (Mohammed)

Enemy extends GameObject
	├── update(deltaTime): void - Update enemy state
	├── render(gc): void - Draw enemy using vector graphics
	├── attack(): void - Execute attack pattern
	└── getDamage(): int - Return damage value

EnemyFactory
	├── createScorpion(x, y): Enemy - Create scorpion enemy
	├── createVulture(x, y): Enemy - Create flying enemy
	└── createSnake(x, y): Enemy - Create snake enemy

Enemy Specifications:

Scorpion: Ground-based, moves slowly, high damage (1 heart)
Vulture: Air-based, swoops down at player, medium damage (1 heart)
Snake: Ground-based, lunges forward, low damage (½ heart)

Enemy Scaling:

Level 1-3: Primarily scorpions, slow movement
Level 4-7: Mix of scorpions and snakes, medium speed
Level 8-10: All enemy types, faster movement patterns

UI System (Ali)

UIManager
	├── createMenuBar(): MenuBar - Create File and Help menus
	├── updateScore(score): void - Update score display
	├── updateHearts(health): void - Update heart display
	├── showGameOverDialog(isWin, finalScore): void - Display end screen
	└── showAboutDialog(): void - Show game credits

ScoreManager
	├── startLevelTimer(): void - Start tracking time for level
	├── calculateLevelScore(timeSpent): int - Calculate points based on time
	└── getTotalScore(): int - Return cumulative score
	

UI Element Specifications:

Hearts: 3 red heart icons (30px × 30px each)
Score: White text on transparent background, upper right corner
Level indicator: "LEVEL X" text, centered top
Menu Bar: Standard JavaFX MenuBar with File and Help options

Vector Graphics Standards
All game elements should be drawn using JavaFX vector graphics, following these guidelines:

Use Shapes: Prefer Path, Rectangle, Circle, and Polygon for all elements
Color Palette:

Sky: #87CEEB (light blue)
Sand: #E9C893 (light tan)
Rocks: #A69185 (gray-brown)
Cacti: #2E8B57 (sea green)
Enemies: #D22F27 (scorpion red), #4F7942 (snake green), #8B4513 (vulture brown)


Line Styles:

Environment outlines: 1.5px black lines
Character outlines: 2px black lines
Interior details: 1px dashed lines


Animation:

Use TranslateTransition for movement
Use RotateTransition for spinning elements
Use Timeline for complex animations
