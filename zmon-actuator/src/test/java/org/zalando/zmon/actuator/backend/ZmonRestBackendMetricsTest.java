package org.zalando.zmon.actuator.backend;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.zalando.zmon.actuator.ExampleApplication;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ExampleApplication.class}, value = {"debug=false"}, webEnvironment = RANDOM_PORT)
public class ZmonRestBackendMetricsTest {

    private static final int REPEATS = 100;
    private final Logger logger = LoggerFactory.getLogger(ZmonRestBackendMetricsTest.class);

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private MeterRegistry meterRegistry;

    private RestTemplate externalClient = new RestTemplate();

    private final Random random = new Random(System.currentTimeMillis());

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(9999);

    @Before
    public void setUp() {
        expectDeleteCall();
    }

    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < REPEATS; i++) {

            externalClient.getForObject("http://localhost:" + port + "/timeConsumingCall", String.class);
            TimeUnit.MILLISECONDS.sleep(random.nextInt(30));
        }

        assertThat(meterRegistry.get("zmon.request.204.DELETE.localhost:9999").timer()).isNotNull();

        String metricsEndpointResponse = externalClient.getForObject("http://localhost:" + port + "/actuator/prometheus",
                String.class);

        logger.info(metricsEndpointResponse);
    }

    private void expectDeleteCall() {
        WireMock.addRequestProcessingDelay(200);
        stubFor(delete(urlEqualTo("/something")).willReturn(
                aResponse().withStatus(HttpStatus.NO_CONTENT.value()).withFixedDelay(100)));
    }

}
