package com.proje.security.service;
import com.proje.web.model.User;
import com.proje.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("accountDetailService")
public class AccountDetailsService implements UserDetailsService {



    private final UserRepository userRepository;
    private final UserDetailsService memoryUserDetailService;

    public AccountDetailsService(UserRepository userRepository,
                                 @Qualifier("memoryUserDetailService") UserDetailsService memoryUserDetailService){

        this.userRepository = userRepository;
        this.memoryUserDetailService = memoryUserDetailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()){

            return  memoryUserDetailService.loadUserByUsername(username);
        }


        return new AccountDetail(user.get());
    }
}
