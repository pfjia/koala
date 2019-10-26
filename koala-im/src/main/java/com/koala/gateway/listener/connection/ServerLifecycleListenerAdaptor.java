package com.koala.gateway.listener.connection;

import com.koala.gateway.connection.ConnectionParam;
import com.koala.gateway.server.NettyServer;
import io.netty.channel.Channel;

/**
 * @author pfjia
 * @since 2019/10/26 11:49
 */
public class ServerLifecycleListenerAdaptor implements ServerLifecycleListener {

    @Override
    public void bindSuccess(NettyServer server) {

    }

    @Override
    public void bindFailed(NettyServer server, Throwable cause) {

    }

    @Override
    public void serverClosed(NettyServer server) {

    }

    @Override
    public void accept(NettyServer server, Channel stream) {

    }

    @Override
    public void close(NettyServer server, Channel stream) {

    }

    @Override
    public void idle(NettyServer server, Channel stream) {

    }

    @Override
    public void exceptionCaught(NettyServer server, Channel stream, Throwable cause) {

    }
}
