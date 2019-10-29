package com.koala.gateway.listener.message;

import com.koala.gateway.dto.KoalaRequest;
import com.koala.gateway.dto.KoalaResponse;
import com.koala.gateway.server.Server;
import io.netty.channel.*;

/**
 * <pre>
 * 服务端Stream消息监听器
 *
 * 服务端的Listener的处理工作，需要线程安全
 *
 * 因为HTTP的写出原因，写出的数据暂时为Object，后续看能否迁移到ResponsePacket上 </pre>
 * <p>
 *
 * @author XiuYang
 * @date 2019/10/21
 */
public interface ServerMessageListener {

    /**
     * <pre>
     * 服务端{@link Server} 开始通过 {@link Channel} 写出一个数据
     *
     * </pre>
     *
     * @param server  服务端
     * @param stream  连接
     * @param message 数据
     */
    void write(Server server, Channel stream, KoalaResponse message);

    /**
     * <pre>
     * 服务端{@link Server} 通过{@link Channel} 写出一个数据完毕
     *
     * </pre>
     *
     * @param server  服务端
     * @param stream  连接
     * @param message 数据
     */
    void writeSuccess(Server server, Channel stream, KoalaResponse message);

    /**
     * <pre>
     * 服务端{@link Server} 通过{@link Channel} 写出一个数据失败
     *
     * </pre>
     *
     * @param server  服务端
     * @param stream  连接
     * @param message 数据
     * @param cause   错误原因
     */
    void writeFailed(Server server, Channel stream,  KoalaResponse message, Throwable cause);

    /**
     * <pre>
     * 服务端{@link Server} 通过{@link Channel} 收到一个数据
     *
     * </pre>
     *
     * @param server        服务端
     * @param stream        连接
     * @param requestPacket 数据
     * @return koalaResponse
     */
    KoalaResponse received(Server server, Channel stream, KoalaRequest requestPacket);

}