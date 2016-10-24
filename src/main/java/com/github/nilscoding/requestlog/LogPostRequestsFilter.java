package com.github.nilscoding.requestlog;

import com.github.nilscoding.requestlog.utils.StringUtils;
import com.github.nilscoding.requestlog.log.LogBuffer;
import com.github.nilscoding.requestlog.log.LogBufferSerializer;
import com.github.nilscoding.requestlog.log.SimpleXmlLogBufferSerializer;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A web Filter which logs all post requests
 *
 * @author NilsCoding
 */
public class LogPostRequestsFilter implements Filter {

    protected FilterConfig filterCfg = null;
    protected LogBufferSerializer serializer;
    protected String uniqueAttribute = null;
    protected String logTarget = null;
    protected String uriPattern = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterCfg = filterConfig;
        String serializerClassname = this.filterCfg.getInitParameter("serializer");
        if (serializerClassname != null) {
            try {
                Object tmpSerializer = Class.forName(serializerClassname);
                if (tmpSerializer instanceof LogBufferSerializer) {
                    this.serializer = (LogBufferSerializer) tmpSerializer;
                }
            } catch (Exception ex) {
            }
        }
        if (this.serializer == null) {
            this.serializer = new SimpleXmlLogBufferSerializer("request");
        }
        String uniqueAttributeStr = this.filterCfg.getInitParameter("unique_id");
        if (uniqueAttributeStr != null) {
            if (uniqueAttributeStr.trim().length() == 0) {
                this.uniqueAttribute = null;
            } else {
                this.uniqueAttribute = uniqueAttributeStr;
            }
        } else {
            this.uniqueAttribute = "X-uniqueRequest";
        }
        // use %{xyz}r in logging to access the unique id
        this.logTarget = this.filterCfg.getInitParameter("target");
        this.uriPattern = this.filterCfg.getInitParameter("uri_pattern");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if ((request instanceof HttpServletRequest) == false) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpReq = (HttpServletRequest) request;

        if ((this.uriPattern != null) && (this.uriPattern.length() > 0)) {
            // get url path
            String requestUri = httpReq.getRequestURI();
            if (requestUri.matches(this.uriPattern) == false) {
                // skip filter processing if pattern does not match
                chain.doFilter(request, response);
                return;
            }
        }

        String uniqueRequestId = null;
        if (this.uniqueAttribute != null) {
            uniqueRequestId = StringUtils.createUniqueId();
            httpReq.setAttribute(this.uniqueAttribute, uniqueRequestId);
        }
        String httpMethod = httpReq.getMethod();
        if (httpMethod.equalsIgnoreCase("POST")) {
            // only capture POST requests here
            // (Servlet Spec 3.0 supports file uploads without external libraries, so it should be safe to
            //  access the request parameters here)
            LogBuffer logBuffer = new LogBuffer();
            logBuffer.append("meta", "now", StringUtils.formatDate(null));
            HttpSession httpSession = httpReq.getSession(false);
            if (httpSession == null) {
                logBuffer.append("meta", "session_id", "");
            } else {
                logBuffer.append("meta", "session_id", httpSession.getId());
            }
            logBuffer.append("meta", "auth_type", httpReq.getAuthType());
            logBuffer.append("meta", "url", httpReq.getRequestURL());
            logBuffer.append("meta", "querystring", httpReq.getQueryString());
            if (this.uniqueAttribute != null) {
                logBuffer.append("meta", "unique_request_id", uniqueRequestId);
            }
            for (Enumeration<String> headerNames = httpReq.getHeaderNames(); headerNames.hasMoreElements();) {
                String headerName = headerNames.nextElement();
                String headerValue = httpReq.getHeader(headerName);
                logBuffer.append("header", headerName, headerValue);
            }
            for (Enumeration<String> paramNames = httpReq.getParameterNames(); paramNames.hasMoreElements();) {
                String paramName = paramNames.nextElement();
                String[] paramValues = httpReq.getParameterValues(paramName);
                if ((paramValues == null) || (paramValues.length == 0)) {
                    logBuffer.append("parameter", paramName, "");
                } else {
                    for (String paramValue : paramValues) {
                        logBuffer.append("parameter", paramName, paramValue);
                    }
                }
            }

            String logOutput = this.serializer.startOutput().writeEntries(logBuffer).endOutput().getAndClearOutput();
            writeToLog(logOutput);
        }
        chain.doFilter(request, response);
    }

    protected void writeToLog(String str) {
        if (str == null) {
            return;
        }
        String tmpTarget = this.logTarget;
        if (tmpTarget == null) {
            tmpTarget = "system.out";
        } else {
            tmpTarget = tmpTarget.toLowerCase();
        }
        // using contains() allows to write to multiple targets at once
        if (tmpTarget.contains("system.out")) {
            System.out.println(str);
        }
        if (tmpTarget.contains("system.err")) {
            System.out.println(str);
        }
        if (tmpTarget.contains("internal")) {
            if ((this.filterCfg != null) && (this.filterCfg.getServletContext() != null)) {
                this.filterCfg.getServletContext().log(str);
            }
        }
    }

    @Override
    public void destroy() {
        // nothing to do here now
    }

}
