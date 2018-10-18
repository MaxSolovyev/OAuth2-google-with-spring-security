package com.crud.security.service;

import com.crud.service.abstraction.UserService;
import com.crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        ResponseEntity<User> entity = userService.getByLogin(login);
        User user = entity.getBody();

        if (user != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        } else {
            throw new UsernameNotFoundException("Invalid username");
        }

        return user;
    }

}
