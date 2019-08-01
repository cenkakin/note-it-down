package com.github.noteitdown.gateway.configuration;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by cenkakin
 */
public interface Identity extends UserDetails {

    String getId();
}
