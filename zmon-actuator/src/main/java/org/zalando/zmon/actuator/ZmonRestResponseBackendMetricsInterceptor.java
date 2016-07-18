/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.zmon.actuator;

import com.google.common.base.Stopwatch;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
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

        Stopwatch stopwatch = Stopwatch.createStarted();
        ClientHttpResponse response = execution.execute(request, body);
        stopwatch.stop();
        metricsWrapper.recordBackendRoundTripMetrics(request, response, stopwatch);
        return response;
    }
}
