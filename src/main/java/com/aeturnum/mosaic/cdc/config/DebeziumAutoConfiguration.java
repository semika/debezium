package com.aeturnum.mosaic.cdc.config;

import com.aeturnum.mosaic.cdc.handler.DebeziumInitializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(DebezimumConfigProperties.class)
public class DebeziumAutoConfiguration {

    @Bean
    public io.debezium.config.Configuration customerConnector(DebezimumConfigProperties cdcConfig) {
        io.debezium.config.Configuration config = io.debezium.config.Configuration.create()
                .with("name", cdcConfig.getEngine().getName())
                .with("connector.class", cdcConfig.getEngine().getConnectorClass())
                .with("offset.storage", cdcConfig.getEngine().getOffsetStorage())
                .with("offset.storage.file.filename", cdcConfig.getEngine().getOffsetStorageFileName())
                .with("offset.flush.interval.ms", cdcConfig.getEngine().getOffsetFlushInterval())
                .with("database.hostname", cdcConfig.getConnector().getHostname())
                .with("database.port", cdcConfig.getConnector().getPort())
                .with("database.user", cdcConfig.getConnector().getUser())
                .with("database.password", cdcConfig.getConnector().getPassword())
                .with("database.server.id", cdcConfig.getConnector().getServerId())
                .with("database.server.name", "customer-mysql-db-server")
                .with("schema.history.internal", cdcConfig.getConnector().getSchemaHistoryInternal())
                .with("converter.schemas.enable", "false") // don't include schema in message
                //.with("debezium.source.database.history", cdcConfig.getConnector().getHistory())
                .with("schema.history.internal.file.filename", cdcConfig.getConnector().getSchemaHistoryInternalFileName())
                .with("topic.prefix",  cdcConfig.getConnector().getTopicPrefix())
                //.with("transforms", "unwrap")
                //.with("transforms.unwrap.type", "io.debezium.transforms.ExtractNewRecordState")
                .build();
        return config;
    }

    @Bean
    public DebeziumInitializer debeziumInitializer(io.debezium.config.Configuration config) throws IOException {
        return new DebeziumInitializer(config);
    }

}
