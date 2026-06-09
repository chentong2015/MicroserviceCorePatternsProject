package org.example.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SpringApplicationProperties.class})
public class PropertyConfiguration {

}
