package org.zalando.zmon.actuator;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;
import org.zalando.zmon.actuator.metrics.MetricsWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Absolute the same as in the original. But instead of using CounterService and GaugeService a Timer fom Codahale is
 * used.
 *
 * @author jbellmann
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ZmonMetricsFilter extends OncePerRequestFilter {
    private static final int UNDEFINED_HTTP_STATUS = 999;

    private final MetricsWrapper metricsWrapper;

    public ZmonMetricsFilter(final MetricsWrapper metricsWrapper) {
        this.metricsWrapper = metricsWrapper;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String path = new UrlPathHelper().getPathWithinApplication(request);
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        try {
            chain.doFilter(request, response);
            status = getStatus(response);
        } finally {
            stopWatch.stop();
            metricsWrapper.recordClientRequestMetrics(request, path, status, stopWatch.getTotalTimeMillis());
        }
    }

    private int getStatus(final HttpServletResponse response) {
        try {
            return response.getStatus();
        } catch (Exception ex) {
            return UNDEFINED_HTTP_STATUS;
        }
    }

}
