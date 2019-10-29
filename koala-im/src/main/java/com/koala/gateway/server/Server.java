package com.koala.gateway.server;

/**
 * @author miaoshuo
 * @date 2019-10-26 12:57
 */
public interface Server {

    /**
     * 是否启动HTTP
     */
    String HSF_HTTP_ENABLE_KEY = "hsf.http.enable";
    /**
     * 服务端接受的backlog
     */
    String HSF_BACKLOG_KEY = "hsf.backlog";
    /**
     * HTTP服务端的头最大长度
     */
    String HSF_HTTP_HEADER_MAX_SIZE_KEY = "hsf.http.header.max.size";
    /**
     * HTTP服务端接收到body的最大长度
     */
    String HSF_HTTP_BODY_MAX_SIZE_KEY = "hsf.http.body.max.size";

    /**
     * 服务端channel#isWritable高低水位 单位kb
     */
    String HSF_SERVER_LOW_WATER_MARK = "hsf.server.low.water.mark";
    String HSF_SERVER_HIGH_WATER_MARK = "hsf.server.high.water.mark";

    /**
     * permit idle time in second
     */
    String HSF_SERVER_IDLE_TIME = "hsf.server.idle.time";

    /**
     * 是否开启HTTP Gzip
     */
    String HSF_HTTP_GIZ = "hsf.http.gzip";
    /**
     * Gzip压缩等级
     */
    String HSF_HTTP_COMPRESS_LEVEL = "hsf.http.compress.level";
    /**
     * 启用Gzip压缩的返回值最小byte数，低于这个数值则不做压缩
     */
    String HSF_HTTP_MIN_COMPRESS_SIZE = "hsf.http.min.compress.size";

    /**
     * <pre>
     * 绑定Server到hostName的指定端口port上
     * </pre>
     *
     * @param hostName ip
     * @param port     端口
     */
    void bind(String hostName, int port);

    /**
     * <pre>
     * 获取指定的ip
     * </pre>
     *
     * @return ip
     */
    String getHostName();

    /**
     * <pre>
     * 获取上报的端口
     * </pre>
     *
     * @return 端口
     */
    int getPort();

    /**
     * 关闭当前Server
     */
    void close();

    void closing();

    boolean isNeedExport();

    /**
     * write water mark enabled
     *
     * @return if check water mark when writing
     */
    boolean isWaterMarkEnabled();

    int getHighWaterMark();

    int getLowWaterMark();
}
