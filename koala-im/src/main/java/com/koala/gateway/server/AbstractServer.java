package com.koala.gateway.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;

/**
 * @author miaoshuo
 * @date 2019-10-26 12:58
 */
@Slf4j
public abstract class AbstractServer implements Server {

    private static final Logger log = LoggerInit.LOGGER;
    private final StreamManager streamManager = HSFServiceContainer.getInstance(StreamManager.class);
    /**
     * 水位配置
     */
    protected int lowWaterMark;
    protected int highWaterMark;
    protected int serverIdleTimeInSeconds;
    /**
     * 维护当前客户端的连接
     */
    private List<Stream> serverStreams = new CopyOnWriteArrayList<Stream>();
    /**
     * 服务端绑定的hostName
     */
    private String hostName;
    /**
     * 服务端绑定的端口
     */
    private int port;
    private boolean waterMarkEnabled;

    public AbstractServer() {
        Config config = HSFServiceContainer.getInstance(ConfigService.class).getConfig();
        int configLowValue = config.getInt(HSF_SERVER_LOW_WATER_MARK);
        int configHighValue = config.getInt(HSF_SERVER_HIGH_WATER_MARK);
        lowWaterMark = configLowValue * 1024;
        highWaterMark = configHighValue * 1024;
        waterMarkEnabled = lowWaterMark > 0 && highWaterMark > 0 && highWaterMark > lowWaterMark;
        if (waterMarkEnabled) {
            log.info("WaterMark is enabled: [{} ~ {}KB]", configLowValue, configHighValue);
        }

        serverIdleTimeInSeconds = config.getInt(HSF_SERVER_IDLE_TIME, 90);
    }

    @Override
    public void bind(String hostName, int port) {
        if (hostName == null) {
            throw new IllegalArgumentException("hostName is null");
        }
        // log
        this.hostName = hostName;
        this.port = port;
        doBind(this.hostName, this.port);
    }

    @Override
    public void close() {
        doClose();
    }

    @Override
    public boolean isWaterMarkEnabled() {
        return waterMarkEnabled;
    }

    @Override
    public int getHighWaterMark() {
        return highWaterMark;
    }

    @Override
    public int getLowWaterMark() {
        return lowWaterMark;
    }

    /**
     * 添加一个Stream到当前的Server，如果当前的Server没有关闭
     *
     * @param stream Stream
     */
    public void addStream(Stream stream) {
        serverStreams.add(stream);
        if (streamManager != null) {
            streamManager.addStream(stream);
        }
    }

    /**
     * 从当前的Server移除一个Stream
     *
     * @param stream Stream
     */
    public void removeStream(Stream stream) {
        if (stream != null) {
            stream.close();
            serverStreams.remove(stream);
        }
    }

    public List<Stream> getServerStreams() {
        return serverStreams;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public int getPort() {
        return port;
    }

    protected abstract void doClose();

    protected abstract void doBind(String hostName, int port);
}
