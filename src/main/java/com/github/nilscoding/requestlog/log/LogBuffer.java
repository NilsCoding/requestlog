package com.github.nilscoding.requestlog.log;

import java.util.LinkedList;
import java.util.List;

/**
 * Log buffer
 *
 * @author NilsCoding
 */
public class LogBuffer {

    protected List<LogBufferEntry> entries = new LinkedList<>();

    public LogBuffer() {
    }

    public LogBuffer append(String name, String key, Object value) {
        if ((name == null) || (name.isEmpty())) {
            name = "key_value_pair";
        }
        this.entries.add(new LogBufferEntry(name, key, value));
        return this;
    }

    public List<LogBufferEntry> getEntries() {
        return entries;
    }

    public void clear() {
        this.entries.clear();
    }

}
