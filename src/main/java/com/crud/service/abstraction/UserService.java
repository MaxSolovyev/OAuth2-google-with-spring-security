package com.crud.service.abstraction;


import com.crud.model.User;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

public interface UserService {
    ResponseEntity<User> get(long id);
    ResponseEntity<Void> getAny();
    ResponseEntity<User> getByLogin(String login);
    ResponseEntity<User> save(User user);
    ResponseEntity<User> delete(long id);
    ResponseEntity<User> update(User user, List<Integer> rolesId);
    ResponseEntity<List<User>> getAll();
}
