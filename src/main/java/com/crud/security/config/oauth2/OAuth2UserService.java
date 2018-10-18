package com.crud.security.config.oauth2;

import com.crud.model.Role;
import com.crud.model.User;
import com.crud.service.abstraction.RoleService;
import com.crud.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class OAuth2UserService  extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

//    private Logger log;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try{
            OAuth2User oAuth2User = super.loadUser(userRequest);

            return buildUser(oAuth2User, userRequest.getClientRegistration().getRegistrationId());
        }catch (Exception e){
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_oauth_user_request"), "Unable to laod user from OAuth2UserRequest.");
        }
    }


    private User buildUser(OAuth2User oAuth2User, String registrationId) throws OAuth2AuthenticationException {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get(StandardClaimNames.EMAIL);

        if(email == null ||  email.isEmpty()){
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_oauth_user_email"),
                    "Unable to retrieve email from attribute for user: "+oAuth2User.getName());
        }

        User user = null;
        try {
            ResponseEntity<User> entity = userService.getByLogin(email);
            user = entity.getBody();
        }
        catch (Exception ex) {
            //user not found in database, let's save him
        }
        if (user == null) {
            user = new User(
                    (String) attributes.get("name"),
                    (String) attributes.get("email"),
                    "secretpassword"
            );

            ResponseEntity<Role> entity = roleService.get(2);
            Role role = entity.getBody();

            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            userService.save(user);
        }

        return user;
    }
}
