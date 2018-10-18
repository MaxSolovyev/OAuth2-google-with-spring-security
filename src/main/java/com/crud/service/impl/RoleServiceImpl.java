package com.crud.service.impl;

import com.crud.model.Role;
import com.crud.model.User;
import com.crud.service.abstraction.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RestTemplate restTemplate;

    private String Rest_URI;

    public RoleServiceImpl() {
        try {
            Properties properties = new Properties();
            properties.load(ClassLoader.getSystemResourceAsStream("client.properties"));
            Rest_URI = properties.getProperty("rest_URI_role");
        }
        catch (IOException ignore) {
        }
    }

    @Override
    public ResponseEntity<Role> get(int id) {
        return restTemplate.getForEntity(Rest_URI + "/" + id, Role.class);
    }

    @Override
    public ResponseEntity<Role> getByName(String name) {
        return restTemplate.getForEntity(Rest_URI + "/name/" + name, Role.class);
    }

    @Override
    public ResponseEntity<List<Role>> getAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("",headers);

        return restTemplate.exchange(Rest_URI, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Role>>() {
        });
    }
}
