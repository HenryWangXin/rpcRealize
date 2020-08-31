package consumer;

import api.HelloService;
import consumer.component.AppConfigCon;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * ConsumerStart简介
 * 消费端启动类
 *
 * @author wangxin119
 * @date 2020-08-29 23:27
 */
public class ConsumerStart {
    public static void main(String[] args) throws NoSuchMethodException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfigCon.class);
        HelloService helloService = (HelloService) context.getBean("helloService");
        for (int i = 0; i < 10; i++) { //调用10次
            String zz = helloService.sayHello("zz");
            System.out.println(zz);
        }
    }
}
