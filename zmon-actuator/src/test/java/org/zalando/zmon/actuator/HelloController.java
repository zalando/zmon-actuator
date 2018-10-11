package org.zalando.zmon.actuator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jbellmann
 */
@RestController
public class HelloController {

    private AtomicInteger counter = new AtomicInteger(0);

    @RequestMapping(value = "/hello")
    public String hello() {
        int count = counter.incrementAndGet();
        if (count % 3 == 0) {
            throw new RuntimeException("ERROR");
        }

        return "hello";
    }
}
