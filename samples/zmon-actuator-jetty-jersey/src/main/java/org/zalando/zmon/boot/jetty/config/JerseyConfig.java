package org.zalando.zmon.boot.jetty.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.zalando.zmon.boot.jetty.resources.SimpleResource;
import org.zalando.zmon.jaxrs.jersey.BaseJerseyConfig;
import org.zalando.zmon.jaxrs.jersey.BestMatchingPatternFilter;

import javax.annotation.PostConstruct;

/**
 * Auto-Registration of {@link BestMatchingPatternFilter}.
 *
 * @author jbellmann
 *
 */
@Configuration
@Profile("!example2")
public class JerseyConfig extends BaseJerseyConfig {

    @PostConstruct
    public void init() {
        register(SimpleResource.class);
    }
}
