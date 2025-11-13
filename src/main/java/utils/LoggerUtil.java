package utils;

public class LoggerUtil {

    private static synchronized void log(String level, String message) {
        String threadName = Thread.currentThread().getName();
        System.out.printf("[%s][%s] %s%n", level, threadName, message);
    }

    public static void info(String message) {
        log("INFO", message);
    }

    public static void warn(String message) {
        log("WARN", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }
}
