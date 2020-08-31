package sdk.protocol.http;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import sdk.LocalRegister;
import sdk.Invocation;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;

/**
 * @author tanghf
 * @className protocol.http.HttpServletHandler.java
 * @createTime 2019/8/21 10:40
 */
public class HttpServletHandler {

    public void handle(HttpServletRequest req, HttpServletResponse resp){
        //处理请求，返回结果
        try {
            ServletInputStream inputStream = req.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            Invocation invocation = (Invocation) ois.readObject();
            System.out.println("wangxin:"+JSONObject.toJSONString(invocation));
            System.out.println("wangxin InterfaceName:"+invocation.getInterfaceName());
            Object instance = LocalRegister.get(invocation.getInterfaceName());
            Class implClass = instance.getClass();
            Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());
            String result = (String) method.invoke(instance, invocation.getParams());
            IOUtils.write(result, resp.getOutputStream(), "utf-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
