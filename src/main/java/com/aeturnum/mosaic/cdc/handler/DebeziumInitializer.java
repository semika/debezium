package com.aeturnum.mosaic.cdc.handler;

import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.debezium.data.Envelope.FieldName.*;

@Slf4j
public class DebeziumInitializer {

    //private DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
    private DebeziumEngine<ChangeEvent<String, String>>  debeziumEngine;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void init() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        try {
            this.executor.shutdown();
            while(!executor.awaitTermination(15, TimeUnit.SECONDS)) {
                log.info("Waiting another 5 seconds for the embedded engine to shut down");
                if (this.debeziumEngine != null) {
                    this.debeziumEngine.close();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public DebeziumInitializer(io.debezium.config.Configuration cdcConfig) {
        final Properties _props = cdcConfig.asProperties();
//        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
//                .using(_props)
//                .notifying(this::handleChangeEvent)
//                .using((success, message, error) -> {
//                    log.info("Debezium engine terminated....!");
//                })
//                .build();

        this.debeziumEngine = DebeziumEngine.create(Json.class)
                .using(_props)
                .notifying(this::_handleChangeEvent)
                .using((success, message, error) -> log.info("Debezium engine terminated....!"))
                .build();

//        this.debeziumEngine = DebeziumEngine.create(Json.class)
//                .using(_props)
//                .notifying(((records, committer) -> {
//                    for (ChangeEvent<String, String> r : records) {
//                        System.out.println("Key = '" + r.key() + "' value = '" + r.value() + "'");
//                        committer.markProcessed(r);
//                    }
//                }))
//                .using((success, message, error) -> log.info("Debezium engine terminated....!"))
//                .build();

    }

    /**
     * Handler function should not throw any exception. If the handler function throws an exception, the engine will
     * log the exception and move to the next record. If there is an exception, the best practice is to handle it.
     * @param changeEvent
     */
    private void _handleChangeEvent(ChangeEvent<String, String> changeEvent) {
        String key = changeEvent.key();
        String value = changeEvent.value();
        System.out.println(changeEvent.key());
        System.out.println(changeEvent.value());
    }

    private void __handleChangeEvent(ChangeEvent<String, String> changeEvent, DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) {
        String key = changeEvent.key();
        String value = changeEvent.value();
        System.out.println(changeEvent.key());
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        System.out.print(sourceRecord);
        Struct sourceRecordChangeValue= (Struct) sourceRecord.value();

        if (sourceRecordChangeValue != null) {
            Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));

            if(operation != Envelope.Operation.READ) {
                String record = operation == Envelope.Operation.DELETE ? BEFORE : AFTER;
                Struct struct = (Struct) sourceRecordChangeValue.get(record);
//                Map<String, Object> payload = struct.schema().fields().stream()
//                        .map(Field::name)
//                        .filter(fieldName -> struct.get(fieldName) != null)
//                        .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
//                        .collect(toMap(Pair::getKey, Pair::getValue));

                //this.customerService.replicateData(payload, operation);
            }
        }
    }
}
