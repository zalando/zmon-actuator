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
package org.zalando.zmon.actuator.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Stopwatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MetricsWrapper {

    private static final String UNKNOWN_PATH_SUFFIX = "/unmapped";
    private final MetricRegistry metricRegistry;

    private static final Log logger = LogFactory.getLog(MetricsWrapper.class);

    @Autowired
    public MetricsWrapper(final MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    public void recordClientRequestMetrics(final HttpServletRequest request, final String path, final int status,
            final long time) {
        String suffix = getFinalStatus(request);
        submitToTimer(getKey("zmon.response." + status + "." + request.getMethod().toUpperCase() + suffix), time);
    }

    public void recordBackendRoundTripMetrics(final String requestMethod, final String host, final int status,
            final long time) {

        submitToTimer(getKey(String.format("zmon.request.%s.%s.%s", status, requestMethod.toUpperCase(), host)), time);
    }

    public void recordBackendRoundTripMetrics(final HttpRequest request, final ClientHttpResponse response,
            final Stopwatch stopwatch) {

        try {
            recordBackendRoundTripMetrics(request.getMethod().name(), getHost(request), response.getRawStatusCode(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (IOException e) {
            logger.warn("Could not detect status for " + response);
        }
    }

    private String getHost(final HttpRequest request) {
        return request.getURI().getHost() + getPort(request);
    }

    private String getPort(final HttpRequest request) {
        final int port = request.getURI().getPort();
        return port > 0 ? ":" + port : "";
    }

    private String getFinalStatus(final HttpServletRequest request) {
        Object bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (bestMatchingPattern != null) {
            return fixSpecialCharacters(bestMatchingPattern.toString());
        }

        // always return unknown, using the full path leads to an explosion of metrics due to path variables
        return UNKNOWN_PATH_SUFFIX;
    }

    private String fixSpecialCharacters(final String value) {
        String result = value.replaceAll("[{}]", "-");
        result = result.replace("**", "-star-star-");
        result = result.replace("*", "-star-");
        result = result.replace("/-", "/");
        result = result.replace("-/", "/");
        if (result.endsWith("-")) {
            result = result.substring(0, result.length() - 1);
        }

        if (result.startsWith("-")) {
            result = result.substring(1);
        }

        return result;
    }

    private void submitToTimer(final String metricName, final long value) {
        try {
            Timer timer = metricRegistry.timer(metricName);
            timer.update(value, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.warn("Unable to submit timer metric '" + metricName + "'", e);
        }
    }

    private String getKey(final String string) {

        // graphite compatible metric names
        String value = string.replace("/", ".");
        value = value.replace("..", ".");
        if (value.endsWith(".")) {
            value = value + "root";
        }

        if (value.startsWith("_")) {
            value = value.substring(1);
        }

        return value;
    }
}
