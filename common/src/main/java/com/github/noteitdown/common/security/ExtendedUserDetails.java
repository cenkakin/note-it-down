package com.github.noteitdown.common.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by cenkakin
 */
public interface ExtendedUserDetails extends UserDetails {

    String getId();
}
