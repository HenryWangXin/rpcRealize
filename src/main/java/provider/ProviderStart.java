package provider;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import provider.component.AppConfigPro;


/**
 * 服务端启动类
 * @author wangxin119
 * @date 2020-08-29 23:36
 */
public class ProviderStart {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfigPro.class);
    }
}
