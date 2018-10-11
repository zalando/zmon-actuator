package com.zalando.zmon.boot.jetty;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zalando.zmon.boot.jetty.JettyApplication;
import org.zalando.zmon.jaxrs.jersey.BaseJerseyConfig;
import org.zalando.zmon.jaxrs.jersey.BestMatchingPatternFilter;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Uses den {@link BaseJerseyConfig} to register {@link BestMatchingPatternFilter} by default.
 *
 * @author jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JettyApplication.class}, webEnvironment = RANDOM_PORT)
@DirtiesContext
public class BaseJerseyConfigIT extends AbstractRunner {

}
