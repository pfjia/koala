package com.koala.gateway.listener.connection;

import com.koala.gateway.connection.ConnectionParam;
import com.koala.gateway.server.NettyServer;
import io.netty.channel.Channel;


/**
 * <pre>
 * 服务端{@link Stream}生命周期监听器
 * 当链接建立和销毁的时候，会进行事件通知
 * 服务端的Listener的处理工作，需要线程安全  </pre>
 *
 * @author XiuYang
 * @date 2019/10/21
 */
public interface ServerLifecycleListener {

    /**
     * <pre>
     * 服务端绑定成功     * </pre>
     * * @param server 服务端
     */
    void bindSuccess(NettyServer server);

    /**
     * <pre>
     * 服务端绑定失败     * </pre>
     * * @param server 服务端     * @param cause  绑定失败的原因
     */
    void bindFailed(NettyServer server, Throwable cause);

    /**
     * <pre>
     *     服务端关闭     * </pre>
     *
     * @param server
     */
    void serverClosed(NettyServer server);

    /**
     * <pre>
     * 当前的Server建立了一个连接Stream     * </pre>
     *  @param server 建立连接的Server
     *  @param stream 连接Stream
     */
    void accept(NettyServer server, Channel stream);

    /**
     * <pre>
     * 当前的Server关闭了一个连接Stream     * </pre>
     * * @param server 连接的Server     * @param stream 连接Stream
     */
    void close(NettyServer server, Channel stream);

    /**
     * <pre>
     * 当前的Server上Stream处于空闲状态    </pre>
     *
     * @param server 连接的Server
     * @param stream 连接Stream
     */
    void idle(NettyServer server, Channel stream);

    /**
     * <pre>
     * 当前的Server上的Stream出现了异常     * </pre>
     * * @param server 连接的Server     * @param stream 连接Stream     * @param cause  问题
     */
    void exceptionCaught(NettyServer server, Channel stream, Throwable cause);
}