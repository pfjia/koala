package com.koala.gateway.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.ServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author pfjia
 * @since 2019/10/26 11:37
 */
@Slf4j
@Component
public class WebSocketAcceptorChannelInitializer extends ChannelInitializer<ServerSocketChannel> {
    @Override
    protected void initChannel(ServerSocketChannel ch) throws Exception {

    }
}
