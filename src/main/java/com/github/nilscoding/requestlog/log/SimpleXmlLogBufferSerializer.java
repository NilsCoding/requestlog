package com.github.nilscoding.requestlog.log;

import com.github.nilscoding.requestlog.utils.StringUtils;
import static com.github.nilscoding.requestlog.utils.StringUtils.containsAny;
import java.util.Formattable;
import java.util.List;

/**
 * Writes a LogBuffer in XML format
 *
 * @author NilsCoding
 */
public class SimpleXmlLogBufferSerializer implements LogBufferSerializer {

    protected StringBuilder buffer = new StringBuilder();
    protected String name;

    public SimpleXmlLogBufferSerializer() {
        this.name = "logging";
    }

    public SimpleXmlLogBufferSerializer(String name) {
        if ((name == null) || (name.length() == 0)) {
            name = "logging";
        }
        this.name = name;
    }

    @Override
    public LogBufferSerializer startOutput() {
        this.buffer.setLength(0);
        this.buffer.append("<").append(this.name).append(">").append(StringUtils.NEWLINE);
        return this;
    }

    @Override
    public LogBufferSerializer writeEntries(LogBuffer logBuffer) {
        if (logBuffer == null) {
            return this;
        }
        List<LogBufferEntry> entries = logBuffer.getEntries();
        if ((entries == null) || (entries.isEmpty() == true)) {
            return this;
        }
        for (LogBufferEntry entry : entries) {
            String keyStr = StringUtils.removeAny(entry.getKey(), StringUtils.XML_TO_ESCAPE);
            String nameStr = StringUtils.removeAny(entry.getName(), StringUtils.XML_TO_ESCAPE).trim();
            if (nameStr == null) {
                nameStr = "key_value_pair";
            }
            buffer.append("\t").append("<").append(nameStr).append(" key=\"").append(keyStr).append("\">");
            if (entry.getValue() == null) {

            } else if (entry.getValue() instanceof Formattable) {
                String tmpStr = String.format("%s", entry.getValue());
                if (containsAny(tmpStr, StringUtils.XML_TO_ESCAPE)) {
                    buffer.append("<![CDATA[").append(tmpStr).append("]]>");
                } else {
                    buffer.append(tmpStr);
                }
            } else {
                String tmpStr = entry.getValue().toString();
                if (containsAny(tmpStr, StringUtils.XML_TO_ESCAPE)) {
                    buffer.append("<![CDATA[").append(tmpStr).append("]]>");
                } else {
                    buffer.append(tmpStr);
                }
            }
            buffer.append("</").append(nameStr).append(">");
            buffer.append(StringUtils.NEWLINE);
        }
        return this;
    }

    @Override
    public LogBufferSerializer endOutput() {
        this.buffer.append("</").append(this.name).append(">").append(StringUtils.NEWLINE);
        return this;
    }

    @Override
    public String getAndClearOutput() {
        String tmpStr = this.buffer.toString();
        this.buffer.setLength(0);
        return tmpStr;
    }

}
