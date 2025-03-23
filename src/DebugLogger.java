import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugLogger {
    private static final String LOG_FILE = "bedouin_madness_debug.log";
    private static PrintWriter logWriter;
    private static boolean initialized = false;
    
    public static void init() {
        if (initialized) return;
        
        try {
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logWriter.println("\n----- Session started at " + dateFormat.format(new Date()) + " -----");
            logWriter.flush();
            initialized = true;
            
            // Add shutdown hook to close log file when the application exits
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (logWriter != null) {
                    logWriter.println("----- Session ended at " + dateFormat.format(new Date()) + " -----\n");
                    logWriter.close();
                }
            }));
            
        } catch (IOException e) {
            System.err.println("Failed to initialize debug logger: " + e.getMessage());
        }
    }
    
    public static void log(String message) {
        if (!initialized) init();
        
        if (logWriter != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
            logWriter.println(timeFormat.format(new Date()) + " - " + message);
            logWriter.flush();
        }
    }
    
    public static void logPlayerState(Player player) {
        if (player == null) {
            log("Player: NULL");
            return;
        }
        
        log(String.format("Player state: active=%b, dying=%b, health=%d, pos=(%.1f,%.1f), vel=(%.1f,%.1f)",
                player.isActive(), player.isDying(), player.getHealth(), 
                player.getX(), player.getY(), 
                player.getVelocity().getX(), player.getVelocity().getY()));
    }
    
    public static void logMemoryUsage() {
        Runtime rt = Runtime.getRuntime();
        long totalMemory = rt.totalMemory() / 1024 / 1024;
        long freeMemory = rt.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        log(String.format("Memory usage: %d MB used, %d MB total, %d MB free", 
                usedMemory, totalMemory, freeMemory));
    }
    
    public static void logException(Exception e) {
        if (!initialized) init();
        
        if (logWriter != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
            logWriter.println(timeFormat.format(new Date()) + " - EXCEPTION: " + e.getMessage());
            e.printStackTrace(logWriter);
            logWriter.flush();
        }
    }
}