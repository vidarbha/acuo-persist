package com.acuo.persistence;

import com.acuo.common.app.AppId;
import com.acuo.common.app.Configuration;
import com.acuo.common.app.Environment;
import com.acuo.common.util.ArgChecker;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesHelper {

    private static final String DEFAULTS_PROPERTIES_TEMPLATE = "/%s.properties";
    private static final String OVERRIDES_PROPERTIES_TEMPLATE = "/%s-%s.properties";

    public static final String NEO4J_OGM_URL = "neo4j.ogm.url";
    public static final String NEO4J_OGM_USERNAME = "neo4j.ogm.username";
    public static final String NEO4J_OGM_PASSWORD = "neo4j.ogm.password";
    public static final String NEO4J_OGM_DRIVER = "neo4j.ogm.driver";
    public static final String NEO4J_OGM_PACKAGES = "neo4j.ogm.packages";

    private final Configuration configuration;

    private PropertiesHelper(Configuration configuration) {
        this.configuration = configuration;
        configuration.getAppId();
    }

    public static PropertiesHelper of(Configuration configuration) {
        ArgChecker.notNull(configuration, "configuration");
        return new PropertiesHelper(configuration);
    }

    public Properties getOverrides() {
        return getPropertiesFrom(overrideFilePath());
    }

    public Properties getDefaultProperties() {
        return getPropertiesFrom(defaultFilePath());
    }

    private String overrideFilePath() {
        AppId appId = configuration.getAppId();
        Environment environment = configuration.getEnvironment();
        return String.format(OVERRIDES_PROPERTIES_TEMPLATE, appId.toString(), environment.toString());
    }

    private String defaultFilePath() {
        AppId appId = configuration.getAppId();
        return String.format(DEFAULTS_PROPERTIES_TEMPLATE, appId.toString());
    }

    private Properties getPropertiesFrom(String propertiesFilePath) {
        final Properties properties = new Properties();
        try (final InputStream stream = PropertiesHelper.class.getResourceAsStream(propertiesFilePath)) {
            if (stream != null)
                properties.load(stream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return properties;
    }
}
