package org.cosmic.backend.domain.login;

public interface UserDetails {
    String getPassword();

    String getUsername();

    boolean isAccountNonExpired();

    boolean isAccountNonLocked(); //계정 잠김 여부

    boolean isCredentialsNonExpired(); //credentials(password) 만료 여부


    boolean isEnabled(); //유저 사용 가능 여부
}
