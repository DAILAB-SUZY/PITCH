package org.cosmic.backend.domain.login;

import org.cosmic.backend.domain.user.domain.User;

public class PrincipalDetails implements UserDetails{
    private User user;

    public PrincipalDetails(User user) {
        this.user =user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { //계정 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } //계정 잠김 여부

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } //credentials(password) 만료 여부

    @Override
    public boolean isEnabled() {
        return true;
    } //유저 사용 가능 여부

    public User getUser() {
        return this.user;
    }
}
