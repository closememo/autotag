package com.closememo.autotag.config.security.authentication;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ServiceAuthentication extends UsernamePasswordAuthenticationToken {

  private static final long serialVersionUID = -6492557187586204546L;

  public ServiceAuthentication(Object principal, Object details,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, null, authorities);
    super.setDetails(details);
  }
}
