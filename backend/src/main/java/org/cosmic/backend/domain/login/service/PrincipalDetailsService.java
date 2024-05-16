package org.cosmic.backend.domain.login.service;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.login.PrincipalDetails;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=usersRepository.findByEmail_Email(email).get();
        if(user==null){
            throw new UsernameNotFoundException("User not found with email: "+ email);
        }
        return (UserDetails) new PrincipalDetails(user);//유의
    }
}
