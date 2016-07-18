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
package org.zalando.zmon.actuator.config;

import com.codahale.metrics.MetricRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.zalando.zmon.actuator.ZmonRestFilterBeanPostProcessor;

/**
 * @author jbellmann
 */
@Configuration
@ConditionalOnClass(MetricRegistry.class)
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class ZmonPostProcessorAutoConfiguration {

    @Bean
    public static ZmonRestFilterBeanPostProcessor zmonRestFilterBeanPostProcessor() {
        return new ZmonRestFilterBeanPostProcessor();
    }

}
