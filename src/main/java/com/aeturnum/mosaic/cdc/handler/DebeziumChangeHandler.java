package com.aeturnum.mosaic.cdc.handler;

import io.debezium.engine.RecordChangeEvent;
import org.apache.kafka.connect.source.SourceRecord;

public class DebeziumChangeHandler {

    public void handleChange(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        System.out.println(sourceRecordRecordChangeEvent);
    }
}
