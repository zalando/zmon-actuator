package org.zalando.zmon.actuator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.zalando.zmon.actuator.metrics.MetricsWrapper;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ZmonRestResponseBackendMetricsInterceptorTest {

    private static final int PREFERRED_SLEEP_TIME = 200;
  private final MetricsWrapper mockedWrapper = mock(MetricsWrapper.class);
  private final ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);

    @Test
    public void testTimeElapsing() throws IOException {
      final ZmonRestResponseBackendMetricsInterceptor interceptor = new ZmonRestResponseBackendMetricsInterceptor(
              this.mockedWrapper);

      when(this.execution.execute(
              isNull(),
                isNull())).then(
              (Answer<ClientHttpResponse>) invocationOnMock -> {
                    Thread.sleep(PREFERRED_SLEEP_TIME);
                    return null;
              }
      );

      final ArgumentCaptor<StopWatch> stopwatchArgumentCaptor = ArgumentCaptor.forClass(StopWatch.class);
      interceptor.intercept(null, null, this.execution);

      verify(this.mockedWrapper).recordBackendRoundTripMetrics(isNull(),
              isNull(), stopwatchArgumentCaptor.capture());

      Assert.assertTrue("We have set thread sleep at 200", this.takesAtLeastSleepTime(stopwatchArgumentCaptor));

    }

    private boolean takesAtLeastSleepTime(final ArgumentCaptor<StopWatch> stopwatchArgumentCaptor) {
        return stopwatchArgumentCaptor.getValue().getTotalTimeMillis() >= PREFERRED_SLEEP_TIME;
    }
}
