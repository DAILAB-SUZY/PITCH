package org.cosmic.backend.globals.filters;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Assert;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final Long principal;
  private Object credentials;

  public static final JwtAuthenticationToken NONE = new JwtAuthenticationToken(0L, null);

  public JwtAuthenticationToken(Long principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(true);
  }

  public JwtAuthenticationToken(Long principal, Object credentials) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(false);
  }

  public static JwtAuthenticationToken unauthenticated(Long principal) {
    return new JwtAuthenticationToken(principal, null);
  }

  public static JwtAuthenticationToken authenticated(Long principal) {
    return new JwtAuthenticationToken(principal, null, AuthorityUtils.NO_AUTHORITIES);
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    Assert.isTrue(!isAuthenticated,
        "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    super.setAuthenticated(false);
  }

  public void eraseCredentials() {
    super.eraseCredentials();
    this.credentials = null;
  }
}
