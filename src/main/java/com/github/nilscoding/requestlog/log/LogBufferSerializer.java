package com.github.nilscoding.requestlog.log;

/**
 * Serializer for LogBuffer objects
 *
 * @author NilsCoding
 */
public interface LogBufferSerializer {

    public LogBufferSerializer startOutput();

    public LogBufferSerializer writeEntries(LogBuffer logBuffer);

    public LogBufferSerializer endOutput();

    public String getAndClearOutput();

}
