package provider;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import provider.component.ServiceConfig;


/**
 * MyProPostProcessor简介
 * 后置处理器对 ServiceConfig 执行服务暴露
 * @author wangxin119
 * @date 2020-08-29 23:36
 */
@Component
public class MyProPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("serviceConfig".equals(beanName)) {
            ServiceConfig serviceConfig = (ServiceConfig) bean;
            //
            serviceConfig.doExport();
        }
        return bean;
    }
}