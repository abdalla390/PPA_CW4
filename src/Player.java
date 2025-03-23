import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;

public class Player extends GameObject {
    private float health = 3.0f;
    private double speed = 200;
    private boolean isJumping = false;
    private Point2D velocity = new Point2D(0, 0);
    private double gravity = 500;
    private boolean isInvincible = false;
    private double invincibilityTimer = 0;
    private double invincibilityDuration = 1.5;
    private boolean isFacingRight = true;
    
    private boolean isDying = false;
    private double deathTimer = 0;
    private double deathAnimationDuration = 1.0; // Reduced from 2.0 to 1.0 for faster animation
    private double rotationAngle = 0;
    private double fadeOut = 1.0;
    
    private double minX = 0;
    private double maxX = 2000;
    
    private int animationFrame = 0;
    private double frameTimer = 0;
    private double frameDuration = 0.2;
    private PlayerState state = PlayerState.IDLE;
    
    private double accelerationRate = 1500;
    private double decelerationRate = 2000;
    private double targetSpeedX = 0;
    
    private long lastDebugOutput = 0;
    
    enum PlayerState {
        IDLE, RUNNING, JUMPING, DYING
    }

    public Player(double x, double y) {
        super(x, y, 30, 50);
    }

    public void setLevelBounds(double levelWidth) {
        this.maxX = levelWidth - width;
    }

    @Override
    public void update(double deltaTime) {
        if (!isActive) return;
        
        if (isDying) {
            updateDeathAnimation(deltaTime);
            return;
        }
        
        if (isInvincible) {
            invincibilityTimer -= deltaTime;
            if (invincibilityTimer <= 0) {
                isInvincible = false;
            }
        }
        
        frameTimer += deltaTime;
        if (frameTimer >= frameDuration) {
            frameTimer = 0;
            if (state == PlayerState.RUNNING) {
                animationFrame = (animationFrame + 1) % 3;
            } else if (state == PlayerState.JUMPING) {
                animationFrame = (animationFrame + 1) % 2;
            }
        }
        
        if (isJumping) {
            state = PlayerState.JUMPING;
        } else if (Math.abs(velocity.getX()) > 30) {
            state = PlayerState.RUNNING;
        } else {
            state = PlayerState.IDLE;
        }
        
        if (velocity.getX() > 5) {
            isFacingRight = true;
        } else if (velocity.getX() < -5) {
            isFacingRight = false;
        }
        
        double currentSpeedX = velocity.getX();
        if (Math.abs(currentSpeedX - targetSpeedX) > 5) {
            if (currentSpeedX < targetSpeedX) {
                currentSpeedX += accelerationRate * deltaTime;
                if (currentSpeedX > targetSpeedX) currentSpeedX = targetSpeedX;
            } else {
                currentSpeedX -= decelerationRate * deltaTime;
                if (currentSpeedX < targetSpeedX) currentSpeedX = targetSpeedX;
            }
        } else {
            currentSpeedX = targetSpeedX;
        }
        
        velocity = new Point2D(currentSpeedX, velocity.getY() + gravity * deltaTime);
        
        double newX = x + velocity.getX() * deltaTime;
        double newY = y + velocity.getY() * deltaTime;
        
        if (newX < minX) {
            newX = minX;
            velocity = new Point2D(0, velocity.getY());
            targetSpeedX = 0;
        } else if (newX > maxX) {
            newX = maxX;
            velocity = new Point2D(0, velocity.getY());
            targetSpeedX = 0;
        }
        
        x = newX;
        y = newY;

        double groundLevel = 620;
        if (y + height > groundLevel) { 
            y = groundLevel - height;
            velocity = new Point2D(velocity.getX(), 0);
            isJumping = false;
        }
        
        long now = System.currentTimeMillis();
        if (now - lastDebugOutput > 1000) {
            lastDebugOutput = now;
            DebugLogger.logPlayerState(this); // Log state for debugging
        }
    }
    
    private void updateDeathAnimation(double deltaTime) {
        deathTimer += deltaTime;
        rotationAngle += deltaTime * 360;
        y += 150 * deltaTime;
        fadeOut = Math.max(0, 1.0 - (deathTimer / deathAnimationDuration));
        
        if (deathTimer >= deathAnimationDuration) {
            isDying = false;
            isActive = false;
            DebugLogger.log("Player death animation completed"); // Added for debugging
        }
    }

    public void jump() {
        if (!isJumping && !isDying) {
            isJumping = true;
            velocity = new Point2D(velocity.getX(), -375);
            state = PlayerState.JUMPING;
            animationFrame = 0;
        }
    }

    public void moveLeft() { 
        if (!isDying) {
            targetSpeedX = -speed;
            isFacingRight = false;
        }
    }
    
    public void moveRight() {
        if (!isDying) {
            targetSpeedX = speed;
            isFacingRight = true;
        }
    }
    
    public void stopMoving() { 
        if (!isDying) {
            targetSpeedX = 0;
        }
    }
    
    public void takeDamage(float damage) { 
        if (!isInvincible && !isDying) {
            health -= damage; 
            if (health < 0) health = 0;
            
            isInvincible = true;
            invincibilityTimer = invincibilityDuration;
            
            // Reduced knockback effect
            velocity = new Point2D(velocity.getX() * 0.3, -150);
            isJumping = true;
            
            if (health <= 0) {
                startDeathAnimation();
                DebugLogger.log("Player health depleted, starting death animation");
            } else {
                // Flash animation but don't disable player
                DebugLogger.log("Player took damage, health: " + health);
            }
        }
    }
    
    public void startDeathAnimation() {
        isDying = true;
        deathTimer = 0;
        state = PlayerState.DYING;
        velocity = new Point2D(0, -250);
        isJumping = true;
    }
    
    public boolean isDying() {
        return isDying;
    }
    
    public int getHealth() { return (int) Math.ceil(health); }
    
    public void setHealth(float health) {
        this.health = health;
        if (health <= 0) {
            startDeathAnimation();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive) return;
        
        if (isInvincible && (int)(invincibilityTimer * 10) % 2 == 0 && !isDying) {
            return;
        }
        
        gc.save();
        
        if (isDying) {
            gc.translate(x + width/2, y + height/2);
            gc.rotate(rotationAngle);
            gc.setGlobalAlpha(fadeOut);
            
            gc.setFill(Color.RED);
            gc.fillRect(-width/2, -height/2, width, height);
            
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeLine(-10, -15, -5, -10);
            gc.strokeLine(-10, -10, -5, -15);
            gc.strokeLine(5, -15, 10, -10);
            gc.strokeLine(5, -10, 10, -15);
        } else {
            if (state == PlayerState.IDLE) {
                drawSimpleNinja(gc, false);
            } else if (state == PlayerState.RUNNING) {
                drawSimpleNinja(gc, true);
            } else if (state == PlayerState.JUMPING) {
                drawSimpleNinja(gc, true);
            }
        }
        
        gc.restore();
    }
    
    private void drawSimpleNinja(GraphicsContext gc, boolean isMoving) {
        gc.setFill(Color.web("#C19A6B"));
        double drawX = x;
        if (!isFacingRight) {
            gc.translate(x + width, 0);
            gc.scale(-1, 1);
            drawX = 0;
        }
        
        gc.fillRect(drawX + 5, y + 10, width - 10, height - 10);
        gc.fillOval(drawX + 5, y, width - 10, 20);
        
        gc.setFill(Color.web("#000000"));
        gc.fillRect(drawX + 5, y + 8, width - 10, 4);
        
        gc.setFill(Color.web("#D22F27"));
        gc.fillRect(drawX + 20, y + 8, 5, 4);
        
        if (isMoving) {
            double offset = (System.currentTimeMillis() / 100) % 4 - 2;
            gc.setFill(Color.web("#C19A6B"));
            gc.fillRect(drawX, y + 15 + offset, 5, 15);
            gc.fillRect(drawX + width - 5, y + 15 - offset, 5, 15);
            gc.fillRect(drawX + 8, y + height - 20 + offset, 5, 20);
            gc.fillRect(drawX + width - 13, y + height - 20 - offset, 5, 20);
        } else {
            gc.setFill(Color.web("#C19A6B"));
            gc.fillRect(drawX, y + 15, 5, 15);
            gc.fillRect(drawX + width - 5, y + 15, 5, 15);
            gc.fillRect(drawX + 8, y + height - 20, 5, 20);
            gc.fillRect(drawX + width - 13, y + height - 20, 5, 20);
        }
        
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(drawX + 5, y + 10, width - 10, height - 10);
        gc.strokeOval(drawX + 5, y, width - 10, 20);
    }

    public void setIsJumping(boolean jumping) { this.isJumping = jumping; }
    public void setVelocity(Point2D velocity) { this.velocity = velocity; }
    public Point2D getVelocity() { return velocity; }
    public boolean isInvincible() { return isInvincible; }
    public PlayerState getState() { return state; }
}