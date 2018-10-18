package com.crud.service.impl;

import com.crud.model.Role;
import com.crud.model.User;
import com.crud.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@ComponentScan("com.crud.config")
public class UserServiceImpl implements UserService {

    @Autowired
    private RestTemplate restTemplate;

    private String Rest_URI;

    public UserServiceImpl() {
        try {
            Properties properties = new Properties();
            properties.load(ClassLoader.getSystemResourceAsStream("client.properties"));
            Rest_URI = properties.getProperty("rest_URI");
        }
        catch (IOException ignore) {
        }
    }

    @Override
    public ResponseEntity<List<User>> getAll() throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("",headers);

        return restTemplate.exchange(Rest_URI, HttpMethod.GET, entity, new ParameterizedTypeReference<List<User>>() {
        });


//        ResponseEntity<List<User>> entity = restTemplate.getForEntity(Rest_URI , List.class);
//        ResponseEntity<? extends ArrayList<User>> entity = restTemplate.getForEntity(Rest_URI, (Class<? extends ArrayList<User>>)ArrayList<User>.class);
//        return responseEntity;
//        return userDao.getAll();
    }

    @Override
    public ResponseEntity<User> get(long id) throws HttpServerErrorException {
        return restTemplate.getForEntity(Rest_URI + "/" + id, User.class);
    }

    @Override
    public ResponseEntity<User> getByLogin(String login) throws HttpServerErrorException {
        return restTemplate.getForEntity(Rest_URI + "/login/" + login, User.class);
    }

    @Override
    public ResponseEntity<User> save(User user) throws HttpServerErrorException {
        return restTemplate.postForEntity(Rest_URI, user, User.class);
//        userDao.save(user);
    }

    @Override
    public ResponseEntity<User> delete(long id) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(null,headers);

        ResponseEntity<User> responseEntity = null;
        responseEntity = restTemplate.exchange(Rest_URI + "/" + id,HttpMethod.DELETE,entity,User.class);

        return responseEntity;
//        userDao.delete(user);
    }

    @Override
    public ResponseEntity<User> update(User user, List<Integer> rolesId) throws HttpServerErrorException {

        Set<Role> roles =  rolesId.stream().map(id -> {
            Role role = new Role();
            role.setId(id);
            return role;
        }).collect(Collectors.toSet());
        user.setRoles(roles);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        return restTemplate.exchange(Rest_URI,HttpMethod.PUT,entity,User.class);

//        restTemplate.put(Rest_URI, user);
//        userDao.update(user);
    }


    @Override
    public ResponseEntity<Void> getAny() throws HttpServerErrorException {
        return restTemplate.getForEntity(Rest_URI + "/any",Void.class);
    }

}
