package consumer;

import consumer.component.ReferenceConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import provider.component.ServiceConfig;

/**
 *
 */
@Component
public class MyConPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
       /* if ("referenceConfig".equals(beanName)) {
            ReferenceConfig referenceConfig = (ReferenceConfig) bean;
            Object o = referenceConfig.get();
            referenceConfig.setRef(o);
        }*/
        return bean;
    }
}