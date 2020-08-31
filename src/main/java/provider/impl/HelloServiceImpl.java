package provider.impl;

import api.HelloService;


/**
 * HelloServiceImpl简介
 * RPC具体实现类
 * @author wangxin119
 * @date 2020-08-29 23:35
 */
public class HelloServiceImpl implements HelloService {
    /**
     *
     * @param username
     * @return
     */
    @Override
    public String sayHello(String username) {
        return "hello, 8080" + username;
    }
}
