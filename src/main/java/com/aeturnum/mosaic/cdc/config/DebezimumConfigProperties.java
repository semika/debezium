package com.aeturnum.mosaic.cdc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cdc")
public class DebezimumConfigProperties {

    private final Engine engine = new Engine();
    private final Connector connector = new Connector();

    @Data
    public static class Engine {
        private String name;
        private String connectorClass;
        private String offsetStorage;
        private String offsetStorageFileName;
        private Integer offsetFlushInterval;
    }

    @Data
    public static class Connector {
        private String hostname;
        private Integer port;
        private String user;
        private String password;
        private Integer serverId;
        private String serverName;
        //private String history;
        private String topicPrefix;
        private String schemaHistoryInternal;
        private String schemaHistoryInternalFileName;
    }
}
