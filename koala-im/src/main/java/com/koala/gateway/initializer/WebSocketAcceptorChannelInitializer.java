package com.koala.gateway.initializer;

import java.util.List;

import com.koala.gateway.handler.NettyBindHandler;
import com.koala.gateway.listener.connection.ServerLifecycleListener;
import com.koala.gateway.server.Server;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.ServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pfjia
 * @since 2019/10/26 11:37
 */
@Slf4j
public class WebSocketAcceptorChannelInitializer extends ChannelInitializer<ServerSocketChannel> {

    private List<ServerLifecycleListener> serverLifecycleListenerList;

    private Server server;

    public WebSocketAcceptorChannelInitializer(
        Server server, List<ServerLifecycleListener> serverLifecycleListenerList) {
        this.server = server;
        this.serverLifecycleListenerList = serverLifecycleListenerList;
    }

    @Override
    protected void initChannel(ServerSocketChannel ch) throws Exception {
        ch.pipeline()
            .addLast("serverBindHandler",
                new NettyBindHandler(server,
                    serverLifecycleListenerList));
    }
}
