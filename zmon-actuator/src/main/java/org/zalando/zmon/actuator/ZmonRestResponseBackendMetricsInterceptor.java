package org.zalando.zmon.actuator;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.zalando.zmon.actuator.metrics.MetricsWrapper;

import java.io.IOException;

public class ZmonRestResponseBackendMetricsInterceptor implements ClientHttpRequestInterceptor {

    private final MetricsWrapper metricsWrapper;

    public ZmonRestResponseBackendMetricsInterceptor(final MetricsWrapper metricsWrapper) {
        this.metricsWrapper = metricsWrapper;
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
                                        final ClientHttpRequestExecution execution) throws IOException {

        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        ClientHttpResponse response = execution.execute(request, body);
        stopwatch.stop();
        metricsWrapper.recordBackendRoundTripMetrics(request, response, stopwatch);
        return response;
    }
}
