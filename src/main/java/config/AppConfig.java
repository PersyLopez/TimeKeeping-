package config;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import exception.ConfigurationException;

public class AppConfig {
    private static final Properties config = new Properties();
    private static final String DEFAULT_CONFIG_PATH = "src/main/resources/config.properties";
    
    static {
        loadDefaultConfig();
    }

    public static void loadConfig(String path) {
        try (InputStream input = new FileInputStream(path)) {
            config.load(input);
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load configuration from: " + path, e);
        }
    }

    private static void loadDefaultConfig() {
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                createDefaultConfig();
            } else {
                config.load(input);
            }
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load default configuration", e);
        }
    }

    private static void createDefaultConfig() {
        // Default settings
        config.setProperty("data.directory", "intake_data");
        config.setProperty("backup.directory", "intake_backups");
        config.setProperty("backup.retention.days", "30");
        config.setProperty("export.directory", "exports");
        config.setProperty("log.level", "INFO");
        
        try {
            Path configPath = Paths.get(DEFAULT_CONFIG_PATH);
            Files.createDirectories(configPath.getParent());
            try (OutputStream output = new FileOutputStream(configPath.toFile())) {
                config.store(output, "Default Intake Tracker Configuration");
            }
        } catch (IOException e) {
            throw new ConfigurationException("Failed to create default configuration", e);
        }
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }

    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(config.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static void setProperty(String key, String value) {
        config.setProperty(key, value);
    }

    public static void saveConfig() {
        try (OutputStream output = new FileOutputStream(DEFAULT_CONFIG_PATH)) {
            config.store(output, "Updated Configuration");
        } catch (IOException e) {
            throw new ConfigurationException("Failed to save configuration", e);
        }
    }
} 