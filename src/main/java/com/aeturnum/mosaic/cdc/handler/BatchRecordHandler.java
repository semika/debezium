package com.aeturnum.mosaic.cdc.handler;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;

import java.util.List;

public class BatchRecordHandler implements DebeziumEngine.ChangeConsumer<ChangeEvent<String, String>> {

    @Override
    public void handleBatch(List<ChangeEvent<String, String>> records,
                            DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) throws InterruptedException {

    }
}
