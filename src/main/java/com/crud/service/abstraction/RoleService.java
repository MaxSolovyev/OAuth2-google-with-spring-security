package com.crud.service.abstraction;


import com.crud.model.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    ResponseEntity<Role> get(int id);
    ResponseEntity<Role> getByName(String name);
    ResponseEntity<List<Role>> getAll();
}
