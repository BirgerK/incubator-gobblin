package com.linkedin.uif.source.extractor.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkedin.uif.configuration.ConfigurationKeys;
import com.linkedin.uif.configuration.State;
import com.linkedin.uif.configuration.WorkUnitState;
import com.linkedin.uif.source.extractor.Extractor;
import com.linkedin.uif.source.extractor.filebased.FileBasedHelperException;
import com.linkedin.uif.source.extractor.filebased.FileBasedSource;

public class HadoopSource extends FileBasedSource<Schema, GenericRecord> {
    private Logger log = LoggerFactory.getLogger(HadoopSource.class);

    @Override
    public Extractor<Schema, GenericRecord> getExtractor(WorkUnitState state) throws IOException {
        return new HadoopExtractor(state);
    }

    @Override
    public void initFileSystemHelper(State state) throws FileBasedHelperException {
        this.fsHelper = new HadoopFsHelper(state);
        this.fsHelper.connect();
    }

    @Override
    public List<String> getcurrentFsSnapshot(State state) {
        List<String> results = new ArrayList<String>();
        String path = state.getProp(ConfigurationKeys.SOURCE_FILEBASED_DATA_DIRECTORY);

        try {
            log.info("Running ls command with input " + path);
            results = this.fsHelper.ls(path);
        } catch (FileBasedHelperException e) {
            log.error("Not able to run ls command due to " + e.getMessage()
                    + " will not pull any files", e);
        }
        return results;
    }
}