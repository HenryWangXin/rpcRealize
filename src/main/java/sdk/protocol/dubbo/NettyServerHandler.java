package sdk.protocol.dubbo;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import sdk.LocalRegister;
import sdk.Invocation;

import java.lang.reflect.Method;

/**
 *
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Invocation invocation = JSONObject.parseObject((String) msg,Invocation.class);
        Object instance = LocalRegister.get(invocation.getInterfaceName());
        Class implClass = instance.getClass();
        Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());
        String invoke = (String) method.invoke(instance, invocation.getParams());
        System.out.println("Netty===============" + invoke);
        ctx.writeAndFlush("Netty: " + invoke);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
