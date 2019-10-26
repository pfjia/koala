package com.koala.gateway.constants;

/**
 * @author XiuYang
 * @date 2019/09/30
 */

public class GatewayConstants {

    /**
     * 默认超时时间
     */
    public static final int DEFAULT_TIMEOUT = 3000;

    // 最大协议包长度
    public static final int MAX_FRAME_LENGTH = 1024 * 10; // 10k
    //
    public static final int MAX_AGGREGATED_CONTENT_LENGTH = 65536;


    /**
     * 若在 SERVER_IDLE_TIME_IN_SECONDS s 内未读写，则为idle
     */
    public static final int SERVER_IDLE_TIME_IN_SECONDS=90;
}
