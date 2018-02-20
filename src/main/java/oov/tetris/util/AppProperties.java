package oov.tetris.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class AppProperties {
    private static volatile Properties properties;
    private static final String NAME = "application.properties";

    public static String get(String key) {
        if (properties == null) {
            synchronized (AppProperties.class) {
                if (properties == null) {
                    InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream(NAME);
                    Properties properties = new Properties();
                    try {
                        properties.load(inputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AppProperties.properties = properties;
                }
            }
        }
        return properties.getProperty(key);
    }
}
