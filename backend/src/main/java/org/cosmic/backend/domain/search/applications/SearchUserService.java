package org.cosmic.backend.domain.search.applications;

import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SearchUserService {
    @Autowired
    private UsersRepository usersRepository;

    public List<UserDetail> searchUser(String name){
        Optional<List<User>> optionalUsers = usersRepository.findByUsername(name);
        if(optionalUsers.isEmpty()){
            throw new NotFoundUserException();
        }
        return User.toUserDetail(optionalUsers.get());
    }
}
