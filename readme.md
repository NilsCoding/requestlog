# requestlog

This is a sample project for a Servlet Filter which logs request data like headers and parameters. Logging is only be done if the request method is *POST*.

It also allows you to add a unique value to the request attributes. This works independently from the logging. In Tomcat server you can use the pattern `%{X-uniqueRequest}r` for accessing this unique request attribute (assuming you did not change the default value as described below). When using the (optional) `uri_pattern` is specified, the unique value is only added to the request for those uris matching the pattern.

## Configuration

The filter **does not use automatic configuration** via the `@WebFilter` annotation. Therefore you must manually enable it in the `web.xml` configuration file. You can specify optional configuration parameters in the filter configuration's `init-param` sections as described below.

### Example (default settings)

    <filter>
        <filter-class>com.github.nilscoding.requestlog.LogPostRequestsFilter</filter-class>
        <filter-name>LogPostsFilter</filter-name>
    </filter>
    <filter-mapping>
        <filter-name>LogPostsFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>


## Supported filter initialization parameters

Optional parameters do not need to be specified in the `web.xml` file, in that case useful default values are taken.

### serializer (optional)

Full-qualified class name for a class implementing `LogBufferSerializer`, a simple interface to serialize the internally collected entries to the string that will be written to the log. See default implementation for details if you really want to write a custom serializer.

**Default:** *the provided XML-like implementation*

### unique_id (optional)

The HTTP request attribute to be filled with a unique ID.

Set it to an empty string value to disable it.

**Default:** X-uniqueRequest

### target (optional)

The target to write the log entries to. Can be one of the following values (or a concatinated version of those values to write to multiple targets):

- system.out for `System.out`
- system.err for `System.err`
- internal for logging via `ServletContext.log()`

**Default:** system.out

### uri_pattern (optional)

A regular expression to limit the filter handling. If present and not empty, the `HttpServletRequest.getRequestURI()` will be checked against this pattern via `String.matches()`. If it does not match, then the filter will not do anything. This is useful if you only want to log certain pages that cannot be specified using the Servlet Filter's `url-pattern`.

**Default:** *no pattern given*

# Notice

This is a proof-of-concept project which is not intended for production usage, but it might come in handy for debugging purpose during development. Please use it carefully because it might disclose submitted passwords in the server side log files.

It is also not fully tested with different web-application servers and configurations.

Anyway, feel free to fork and extend it.