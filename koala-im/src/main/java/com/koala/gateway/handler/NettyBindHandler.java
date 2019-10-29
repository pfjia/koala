package com.koala.gateway.handler;

import java.net.SocketAddress;
import java.util.List;
import java.util.function.Consumer;

import com.koala.gateway.listener.connection.ServerLifecycleListener;
import com.koala.gateway.server.Server;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 将服务端{@link Server}的链接{@link Stream}事件映射回{@link ServerStreamLifecycleListener}中 * * 该handler负责监听ServerSocketChannel
 * 相关的信息 * </pre>
 * Created by Arthur.xqw on 2017/2/8.
 */

/**
 * @author pfjia
 * @since 2019/10/26 11:39
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyBindHandler extends ChannelDuplexHandler {

    /**
     * 服务端
     */
    private Server server;
    /**
     * 服务端生命周期监听器
     */
    private List<ServerLifecycleListener> listeners;

    public NettyBindHandler(Server server, List<ServerLifecycleListener> listeners) {
        this.server = server;
        this.listeners = listeners;
    }

    @Override
    public void bind(ChannelHandlerContext ctx, final SocketAddress localAddress,
        ChannelPromise future) throws Exception {
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    callBindSuccessListeners(server);
                } else {
                    callBindFailedListeners(server, future.cause());
                }
            }
        });
        ctx.bind(localAddress, future);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        callServerClosedListeners(server);
        ctx.close(future);
    }

    /**
     * 通知服务端关闭的监听器
     *
     * @param server 服务端
     */
    private void callServerClosedListeners(Server server) {
        listeners.forEach(new Consumer<ServerLifecycleListener>() {
            @Override
            public void accept(ServerLifecycleListener listener) {
                try {
                    listener.serverClosed(server);
                } catch (Exception ex) {
                    String errorCodeStr = LoggerHelper.getErrorCodeStr("HSF", "HSF-0085", "HSF",
                        "invoke LifecycleListener#serverClosed" + listener.getClass() + " got exception");
                    log.error("HSF-0085", errorCodeStr, ex);
                }
            }
        });
    }

    /**
     * 通知绑定成功的监听器
     *
     * @param server 服务端
     */
    private void callBindSuccessListeners(Server server) {

        listeners.forEach(new Consumer<ServerLifecycleListener>() {
            @Override
            public void accept(ServerLifecycleListener listener) {
                try {
                    listener.bindSuccess(server);
                } catch (Exception ex) {
                    String errorCodeStr = LoggerHelper.getErrorCodeStr("HSF", "HSF-0085", "HSF",
                        "invoke LifecycleListener#bindSuccess" + listener.getClass() + " got exception");
                    log.error("HSF-0085", errorCodeStr, ex);
                }
            }
        });

    }

    /**
     * 通知绑定失败的监听器
     *
     * @param server 服务端
     * @param cause  问题
     */
    private void callBindFailedListeners(Server server, Throwable cause) {

        listeners.forEach(new Consumer<ServerLifecycleListener>() {
            @Override
            public void accept(ServerLifecycleListener listener) {
                try {
                    listener.bindFailed(server, cause);
                } catch (Exception ex) {
                    String errorCodeStr = LoggerHelper.getErrorCodeStr("HSF", "HSF-0085", "HSF",
                        "invoke LifecycleListener#bindFailed " + listener.getClass() + " got exception");
                    log.error("HSF-0085", errorCodeStr, ex);
                }
            }
        });
    }
}