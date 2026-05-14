package br.com.softhouse.dende.repositories.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private final Properties properties = new Properties();

    public ConfigProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar application.properties", e);
        }
    }

    public String getUrl() { return properties.getProperty("datasource.url"); }
    public String getUsername() { return properties.getProperty("datasource.username"); }
    public String getPassword() { return properties.getProperty("datasource.password"); }
    public String getDriverClassName() { return properties.getProperty("datasource.driver-class-name"); }
    public int getMaximumPoolSize() { return Integer.parseInt(properties.getProperty("datasource.hikari.maximum-pool-size")); }
    public int getMinimumIdle() { return Integer.parseInt(properties.getProperty("datasource.hikari.minimum-idle")); }
    public long getConnectionTimeout() { return Long.parseLong(properties.getProperty("datasource.hikari.connection-timeout")); }
}