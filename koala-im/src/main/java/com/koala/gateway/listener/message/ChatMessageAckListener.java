package com.koala.gateway.listener.message;

import com.koala.gateway.dto.KoalaMessageAckRequest;
import com.koala.gateway.dto.KoalaRequest;
import com.koala.gateway.dto.KoalaResponse;
import com.koala.gateway.server.Server;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author XiuYang
 * @date 2019/10/16
 */
@Slf4j
@Component("CHAT_MSG_ACK")
public class ChatMessageAckListener implements ServerMessageListener {


    @Override
    public void write(Server server, Channel stream, KoalaResponse message) {

    }

    @Override
    public void writeSuccess(Server server, Channel stream, KoalaResponse message) {

    }

    @Override
    public void writeFailed(Server server, Channel stream, KoalaResponse message, Throwable cause) {

    }


    /**
     * {
     "requestId": 123,
     "type": "CHAT_MSG_SEND",
     "msgType": "text",
     "content": "hello",
     "senderId": "2123",
     "sessionId": "123123",
     "clientMessageId": "SDFSDFSDFSFSDFsdf"
     }

     {
     "requestId": 123,
     "type": "CHAT_MSG_ACK",
     "messageIds":[
     "123123",
     "223sdlfsd"
     ],
     "sessionId": "123123",
     "clientMessageId": "SDFSDFSDFSFSDFsdf"
     }
     * @param koalaRequest
     * @return
     */
    @Override
    public KoalaResponse received(Server server, Channel stream, KoalaRequest koalaRequest) {
        KoalaMessageAckRequest messageAckRequest = (KoalaMessageAckRequest)koalaRequest;

        log.info("ChatMessageAckHandler ack message success messageAckRequest={}",messageAckRequest);

        return new KoalaResponse();
    }
}
