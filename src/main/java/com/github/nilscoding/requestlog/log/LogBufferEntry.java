package com.github.nilscoding.requestlog.log;

/**
 * Log buffer entry
 *
 * @author NilsCoding
 */
public class LogBufferEntry {

    private String name;
    private String key;
    private Object value;

    public LogBufferEntry() {
    }

    public LogBufferEntry(String name, String key, Object value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
