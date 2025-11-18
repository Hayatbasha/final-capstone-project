package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final ThreadLocal<Properties> prop = ThreadLocal.withInitial(() -> {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(
                    System.getProperty("user.dir") + "/src/main/resources/config.properties"
            );
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config.properties", e);
        }
        return properties;
    });

    public static String get(String key) {
        return prop.get().getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(prop.get().getProperty(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(prop.get().getProperty(key));
    }
}
