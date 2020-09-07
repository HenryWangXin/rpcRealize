package provider.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import provider.impl.HelloServiceImpl;

/**
 * AppConfigPro简介
 * 负责spring包的扫描及 ServiceConfig 初始化
 * @author wangxin119
 * @date 2020-08-29 23:30
 */
@Configuration
@ComponentScan("provider")
@EnableAspectJAutoProxy
public class AppConfigPro {
    @Bean
    public HelloServiceImpl helloServiceImpl() {
        return new HelloServiceImpl();
    }
    @Bean
    public ServiceConfig serviceConfig(HelloServiceImpl helloServiceImpl) {
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setHost("127.0.0.1");
        serviceConfig.setPort(8081);
        serviceConfig.setProtocol("http");
        serviceConfig.setWeight(3);
        serviceConfig.setInterfaceName("provider.api.HelloService");
        serviceConfig.setRef(helloServiceImpl);
        return serviceConfig;
    }
}
