package com.zalando.zmon.boot.jetty;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zalando.zmon.boot.jetty.JettyApplication;
import org.zalando.zmon.boot.jetty.config.JerseyConfigExampleTwo;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Uses {@link JerseyConfigExampleTwo} to register everything by-hand.
 *
 * @author jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JettyApplication.class}, webEnvironment = RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("example2")
public class JerseyConfigurationIT extends AbstractRunner {

}
