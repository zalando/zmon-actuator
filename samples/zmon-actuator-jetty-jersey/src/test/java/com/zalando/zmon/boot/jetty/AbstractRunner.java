package com.zalando.zmon.boot.jetty;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.zalando.zmon.boot.jetty.JettyApplication;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = JettyApplication.class, webEnvironment = RANDOM_PORT, properties = {"management.security.enabled=false"})
public abstract class AbstractRunner {

    private static final String LINE = "\"counter.status.200.api.simple.id.complex\":100";

    @Value("${local.server.port}")
    private int port;

    @Test
    public void run() throws InterruptedException {
        RestTemplate rest = new RestTemplate();
        for (int i = 0; i < 100; i++) {
            ResponseEntity<String> response = rest
                    .getForEntity("http://localhost:" + port + "/api/simple/" + i + "/complex", String.class);
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        ResponseEntity<String> metrics = rest.getForEntity("http://localhost:" + port + "/metrics", String.class);
        Assertions.assertThat(metrics.getBody()).contains(LINE);
        // to use browser /metrics
        //TimeUnit.MINUTES.sleep(2);
    }
}
