package ca.dheri.digitalizingreceipts.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GoogleClientConfig.class)
public class AppConfig {
}
