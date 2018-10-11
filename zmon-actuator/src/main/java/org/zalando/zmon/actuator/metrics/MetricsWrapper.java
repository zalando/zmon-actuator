package org.zalando.zmon.actuator.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MetricsWrapper {

  private static final Logger LOGGER = getLogger(MetricsWrapper.class);
    private static final String UNKNOWN_PATH_SUFFIX = "/unmapped";

    private final MeterRegistry meterRegistry;

    @Autowired
    public MetricsWrapper(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordClientRequestMetrics(final HttpServletRequest request, final String path, final int status,
                                           final long time) {
      final String suffix = this.getFinalStatus(request);

      final String metricName = metricNameFrom("zmon.response", status, request.getMethod(), suffix);
      this.submitToTimer(metricName, time);
    }

    public void recordBackendRoundTripMetrics(final String requestMethod, final String host, final int status,
                                              final long time) {

      final String metricName = metricNameFrom("zmon.request", status, requestMethod, host);
      this.submitToTimer(metricName, time);
    }

    public void recordBackendRoundTripMetrics(final HttpRequest request, final ClientHttpResponse response,
                                              final StopWatch stopwatch) {

        try {
          this.recordBackendRoundTripMetrics(request.getMethod().name(), this.getHost(request), response.getRawStatusCode(), stopwatch.getTotalTimeMillis());
        } catch (final IOException e) {
            LOGGER.warn("Could not detect status for " + response);
        }
    }

    private String getHost(final HttpRequest request) {
      return request.getURI().getHost() + this.getPort(request);
    }

    private String getPort(final HttpRequest request) {
        final int port = request.getURI().getPort();
        return port > 0 ? ":" + port : "";
    }

    private String getFinalStatus(final HttpServletRequest request) {
      final Object bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (bestMatchingPattern != null) {
            return fixSpecialCharacters(bestMatchingPattern.toString());
        }

        // always return unknown, using the full path leads to an explosion of metrics due to path variables
        return UNKNOWN_PATH_SUFFIX;
    }

    private static String fixSpecialCharacters(final String value) {
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
          final Timer timer = this.meterRegistry.timer(metricName);
            timer.record(value, TimeUnit.MILLISECONDS);
        } catch (final Exception e) {
            LOGGER.warn("Unable to submit timer metric '" + metricName + "'", e);
        }
    }

    private static String metricNameFrom(final String prefix, final int status, final String requestMethod, final String suffix) {
      final String dirtyName = prefix + "." + status + "." + requestMethod.toUpperCase() + "." + suffix;
        return sanitizeMetricName(dirtyName);
    }

    private static String sanitizeMetricName(final String string) {

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
