package com.github.noteitdown.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cenkakin
 */
@SuppressWarnings("ConfigurationProperties")
@ConfigurationProperties("security.jwt")
@Configuration
public class JwtProperties {

    private String uri = "/auth/**";

    private String header = "Authorization";

    private String prefix = "Bearer ";

    private int expiration = 24*60*60;

    private String secret = "JwtSecretKey";

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
