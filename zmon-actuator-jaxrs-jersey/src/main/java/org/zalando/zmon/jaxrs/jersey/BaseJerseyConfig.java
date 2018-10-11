package org.zalando.zmon.jaxrs.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;

import javax.annotation.PostConstruct;

/**
 * @author jbellmann
 *
 */
public class BaseJerseyConfig extends ResourceConfig {

    @Autowired
    private JerseyProperties jerseyProperties;

    public BaseJerseyConfig() {
    }

    @PostConstruct
    public void registerBestMatchingPatternFilter() {
        register(new BestMatchingPatternFilter(getJerseyProperties().getApplicationPath()));
    }

    protected JerseyProperties getJerseyProperties() {
        return this.jerseyProperties;
    }

}
