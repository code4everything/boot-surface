package org.code4everything.boot.starter.bean;

import java.util.Arrays;
import java.util.List;

/**
 * @author pantao
 * @since 2018/10/30
 **/
public class CorsBean {

    private String path;

    private List<String> origins;

    private List<String> headers;

    private List<String> methods;

    private Boolean credential;

    public CorsBean setOrigins(List<String> origins) {
        this.origins = origins;
        return this;
    }

    public CorsBean setHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    public CorsBean setMethods(List<String> methods) {
        this.methods = methods;
        return this;
    }

    public String getPath() {
        return path;
    }

    public CorsBean setPath(String path) {
        this.path = path;
        return this;
    }

    public List<String> getOrigins() {
        return origins;
    }

    public CorsBean setOrigins(String... origins) {
        this.origins = Arrays.asList(origins);
        return this;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public CorsBean setHeaders(String... headers) {
        this.headers = Arrays.asList(headers);
        return this;
    }

    public List<String> getMethods() {
        return methods;
    }

    public CorsBean setMethods(String... methods) {
        this.methods = Arrays.asList(methods);
        return this;
    }

    public Boolean getCredential() {
        return credential;
    }

    public CorsBean setCredential(Boolean credential) {
        this.credential = credential;
        return this;
    }
}
