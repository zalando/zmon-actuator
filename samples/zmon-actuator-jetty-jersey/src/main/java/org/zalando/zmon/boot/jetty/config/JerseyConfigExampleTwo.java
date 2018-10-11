package org.zalando.zmon.boot.jetty.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.zalando.zmon.boot.jetty.resources.SimpleResource;
import org.zalando.zmon.jaxrs.jersey.BestMatchingPatternFilter;

import javax.annotation.PostConstruct;

/**
 * Self-Registration of {@link BestMatchingPatternFilter}.<br>
 * Like default examples.
 *
 * @author jbellmann
 *
 */
@Configuration
@Profile("example2")
public class JerseyConfigExampleTwo extends ResourceConfig {

    @Autowired
    private JerseyProperties jerseyProperties;

    @PostConstruct
    public void init() {
        register(SimpleResource.class);
        // have a look at 'application.yml' to see the configuration for
        // jerseyProperties#getApplicationPath()
        register(new BestMatchingPatternFilter(jerseyProperties.getApplicationPath()));
    }
}
