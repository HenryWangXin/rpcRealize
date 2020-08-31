package consumer.component;

import api.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AppConfigCon简介
 * 负责spring包的扫描及 referenceConfig 初始化
 * @author wangxin119
 * @date 2020-08-29 22:41
 */
@Configuration
@ComponentScan("consumer")
@EnableAspectJAutoProxy
public class AppConfigCon {
    @Bean //等同于<jsf:consumer id="HelloService" interface="xxx.HelloService" timeout="400" retries="1"  cluster="failover" loadbalance="roundRobin"/>
    public HelloService helloService() {
        ReferenceConfig<HelloService> referenceConfig = new ReferenceConfig();
        referenceConfig.setInterfaceClass(HelloService.class);//执行类信息（接口）
        referenceConfig.setMethod("sayHello");
        referenceConfig.setRetry(1);//重试次数
        referenceConfig.setTimeout(400);//超时时间
        referenceConfig.setCluster("failover");//容错策略（SPI）
        referenceConfig.setLoadbalance("random");//负载均衡（SPI）
        return referenceConfig.get();
    }
}
