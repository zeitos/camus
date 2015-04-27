package com.linkedin.camus.coders;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * Container for messages.  Enables the use of a custom message decoder with knowledge
 * of where these values are stored in the message schema
 *
 * @author kgoodhop
 *
 * @param <R> The type of decoded payload
 */
public class CamusWrapper<R> {
    private R record;
    private long timestamp;
    private MapWritable partitionMap;
    private final String outputPrefix;

    public CamusWrapper(R record) {
        this(record, System.currentTimeMillis());
    }

    public CamusWrapper(R record, long timestamp) {
        this(record, timestamp, "unknown_server", "unknown_service");
    }
    public CamusWrapper(R record, long timestamp, String prefix) {
        this(record, timestamp, "unknown_server", "unknown_service", prefix);
    }

    public CamusWrapper(R record, long timestamp, String server, String service) {
        this(record, timestamp, server, service, "");
    }

    public CamusWrapper(R record, long timestamp, String server, String service,
                        String prefix) {
        this.record = record;
        this.timestamp = timestamp;
        this.partitionMap = new MapWritable();
        this.outputPrefix = prefix;
        partitionMap.put(new Text("server"), new Text(server));
        partitionMap.put(new Text("service"), new Text(service));
    }

    /**
     * Returns the payload record for a single message
     * @return
     */
    public R getRecord() {
        return record;
    }

    /**
     * Returns current if not set by the decoder
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Add a value for partitions
     */
    public void put(Writable key, Writable value) {
        partitionMap.put(key, value);
    }

    /**
     * Get a value for partitions
     * @return the value for the given key
     */
    public Writable get(Writable key) {
        return partitionMap.get(key);
    }

    /**
     * Get all the partition key/partitionMap
     */
    public MapWritable getPartitionMap() {
        return partitionMap;
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }
}
