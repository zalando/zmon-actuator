package org.zalando.zmon.actuator;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

public class ZmonRestFilterBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private static final Logger LOGGER = getLogger(ZmonRestFilterBeanPostProcessor.class);

    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName)
            throws BeansException {

        if (bean instanceof RestTemplate) {

            final RestTemplate restTemplateBean = (RestTemplate) bean;

            restTemplateBean.getInterceptors().add(beanFactory.getBean(ZmonRestResponseBackendMetricsInterceptor.class));
            LOGGER.info("Added " + ZmonRestFilterBeanPostProcessor.class.getCanonicalName() + " instance to "
                    + beanName);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object o, final String s) throws BeansException {
        return o;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
