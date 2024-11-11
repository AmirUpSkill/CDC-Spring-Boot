package com.amir.cdc.listener;

import java.io.IOException;
import java.sql.Struct;
import java.util.concurrent.Executor;

import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Service;

import io.debezium.config.Configuration;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DebeziumSourceEventListener {

    //This will be used to run the engine asynchronously
    private final Executor executor;

    //DebeziumEngine serves as an easy-to-use wrapper around any Debezium connector
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;




    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        Struct sourceRecordKey = (Struct) sourceRecord.key();
        Struct sourceRecordValue = (Struct) sourceRecord.value();
        if (sourceRecordValue != null) {
            try {

                String operation = HandlerUtils.getOperation(sourceRecordValue);

                String documentId = HandlerUtils.getDocumentId(sourceRecordKey);

                String collection = HandlerUtils.getCollection(sourceRecordValue);

                Product product = HandlerUtils.getData(sourceRecordValue);

                productService.handleEvent(operation, documentId, collection, product);

                log.info("Collection : {} , DocumentId : {} , Operation : {}", collection, documentId, operation);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }
}
