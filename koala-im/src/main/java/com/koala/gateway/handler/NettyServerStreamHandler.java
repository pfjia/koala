package com.koala.gateway.handler;

import java.util.List;
import java.util.function.Consumer;

import com.koala.gateway.dto.KoalaRequest;
import com.koala.gateway.dto.KoalaResponse;
import com.koala.gateway.listener.connection.ServerLifecycleListener;
import com.koala.gateway.listener.message.ServerMessageListener;
import com.koala.gateway.server.Server;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 将服务端{@link Server}的链接{@link Stream}事件映射回{@link ServerStreamLifecycleListener}中
 *
 * 负责监听客户端的SocketChannel连接
 *
 * </pre>
 *
 * @author miaoshuo
 * @date 2019-10-27 15:34
 */
@Slf4j
public class NettyServerStreamHandler extends ChannelDuplexHandler {

    /**
     * 服务端
     */
    private Server server;
    /**
     * 服务端生命周期监听器
     */
    private List<ServerLifecycleListener> serverStreamLifecycleListeners;
    /**
     * 服务端事件监听器
     */
    private List<ServerMessageListener> serverStreamMessageListeners;

    public NettyServerStreamHandler(Server server,
        List<ServerLifecycleListener> serverStreamLifecycleListeners,
        List<ServerMessageListener> serverStreamMessageListeners) {
        this.server = server;
        this.serverStreamLifecycleListeners = serverStreamLifecycleListeners;
        this.serverStreamMessageListeners = serverStreamMessageListeners;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        callConnectionAcceptListeners(server, ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        callConnectionCloseListeners(server, ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        callConnectionExceptionListeners(server, ctx.channel();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            callConnectionIdleListeners(server, ctx.channel();
        }

        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void write(ChannelHandlerContext ctx, final Object writeRequest, ChannelPromise promise) throws Exception {
        final Channel stream =ctx.channel();

        // TODO: 2019-10-27 是否需要抽象出StreamWriteRequest ？
        //StreamWriteRequest streamWriteRequest = (StreamWriteRequest)writeRequest;
        //final KoalaResponse responsePacket;
        //RPCResult rpcResult = streamWriteRequest.getRpcResult();
        //if (rpcResult == null) {
        //    responsePacket = streamWriteRequest.getResponsePacket();
        //} else {
        //    byte protocolType = rpcResult.getResponseContext().getProtocolType();
        //    PacketFactory packetFactory = PacketFactorySelector.getInstance().select(protocolType);
        //    responsePacket = packetFactory.serverCreate(rpcResult, stream);
        //}

        KoalaResponse responsePacket=(KoalaResponse) writeRequest;
        callConnectionWriteListeners(server, stream, responsePacket);
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    callConnectionWriteSuccessListeners(server, stream, responsePacket);
                } else {
                    callConnectionWriteFailedListeners(server, stream, responsePacket, future.cause());
                }
            }
        });

        ctx.write(responsePacket, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        callConnectionReceivedListeners(server, ctx.channel(),
            (KoalaRequest)msg);
    }

    /**
     * 通知数据发送的监听器
     *
     * @param server  服务端
     * @param stream  连接
     * @param message 数据
     */
    private void callConnectionWriteListeners(Server server, Channel stream, KoalaResponse message) {
        serverStreamMessageListeners.forEach(new Consumer<ServerMessageListener>() {
            @Override
            public void accept(ServerMessageListener listener) {
                try {
                    listener.write(server, stream, message);
                } catch (Exception ex) {
                    error(listener.getClass().getName(), "write", ex);
                }
            }
        });

    }

    /**
     * 通知数据发送成功的监听器
     *
     * @param server  服务端
     * @param stream  连接
     * @param message 数据
     */
    private void callConnectionWriteSuccessListeners(Server server, Channel stream, KoalaResponse message) {
        serverStreamMessageListeners.forEach(new Consumer<ServerMessageListener>() {
            @Override
            public void accept(ServerMessageListener listener) {
                try {
                    listener.writeSuccess(server, stream, message);
                } catch (Exception ex) {
                    error(listener.getClass().getName(), "writeSuccess", ex);
                }
            }
        });
    }

    /**
     * 通知数据发送失败的监听器
     *
     * @param server  服务端
     * @param stream  连接
     * @param message 数据
     * @param cause   原因
     */
    private void callConnectionWriteFailedListeners(Server server, Channel stream, KoalaResponse message,
        Throwable cause) {

        serverStreamMessageListeners.forEach(new Consumer<ServerMessageListener>() {
            @Override
            public void accept(ServerMessageListener listener) {
                try {
                    listener.writeFailed(server, stream, message, cause);
                } catch (Exception ex) {
                    error(listener.getClass().getName(), "writeFailed", ex);
                }
            }
        });
    }

    /**
     * 通知数据接受的监听器
     *
     * @param server  服务端
     * @param stream  连接
     * @param message 数据
     */
    private void callConnectionReceivedListeners(Server server, Channel stream, KoalaRequest message) {
        serverStreamMessageListeners.forEach(new Consumer<ServerMessageListener>() {
            @Override
            public void accept(ServerMessageListener listener) {
                try {
                    listener.received(server, stream, message);
                } catch (Exception ex) {
                    error(listener.getClass().getName(), "received", ex);
                }
            }
        });
    }

    /**
     * 通知连接接受监听器，远端一个连接联通上来
     *
     * @param server 服务端
     * @param stream 连接
     */
    private void callConnectionAcceptListeners(Server server, Channel stream) {
        serverStreamLifecycleListeners.forEach(new Consumer<ServerLifecycleListener>() {
            @Override
            public void accept(ServerLifecycleListener listener) {
                try {
                    listener.accept(server, stream);
                } catch (Exception ex) {
                    error(listener.getClass().getName(), "accept", ex);
                }
            }
        });
    }

    /**
     * 通知连接空闲监听器
     *
     * @param server 服务端
     * @param stream 连接
     */
    private void callConnectionIdleListeners(Server server, Channel stream) {

        serverStreamLifecycleListeners.forEach(new Consumer<ServerLifecycleListener>() {
            @Override
            public void accept(ServerLifecycleListener listener) {
                try {
                    listener.idle(server, stream);
                } catch (Exception ex) {
                    error(listener.getClass().getName(), "idle", ex);
                }
            }
        });
    }

    /**
     * 通知连接异常监听器
     *
     * @param server 服务端
     * @param stream 连接
     * @param cause  问题
     */
    private void callConnectionExceptionListeners(Server server, Channel stream, Throwable cause) {
        serverStreamLifecycleListeners.forEach(new Consumer<ServerLifecycleListener>() {
            @Override
            public void accept(ServerLifecycleListener listener) {
                try {
                    listener.exceptionCaught(server, stream, cause);
                } catch (Exception ex) {
                    error(listener.getClass().getName(), "exceptionCaught", ex);
                }
            }
        });
    }

    /**
     * 通知连接关闭监听器
     *
     * @param server 服务端
     * @param stream 连接
     */
    private void callConnectionCloseListeners(Server server, Channel stream) {
        serverStreamLifecycleListeners.forEach(new Consumer<ServerLifecycleListener>() {
            @Override
            public void accept(ServerLifecycleListener listener) {
                try {
                    listener.close(server, stream);
                } catch (Exception ex) {
                    error(listener.getClass().getName(), "close", ex);
                }
            }
        });
    }

    private void error(String className, String methodName, Exception ex) {
        String errorCodeStr = LoggerHelper.getErrorCodeStr("HSF", "HSF-0085", "HSF",
            "exception caught when invoke MessageListener " + className + "'s method " + methodName);
        log.error("HSF-0085", errorCodeStr, ex);
    }
}