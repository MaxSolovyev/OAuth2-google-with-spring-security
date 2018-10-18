package com.crud.controller;

import com.crud.model.Role;
import com.crud.service.abstraction.RoleService;
import com.crud.service.abstraction.UserService;
import com.crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("userAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

//    @Autowired
//    private RoleService roleService;

    @GetMapping("/")
    public String view() {
        return "/index";
    }

    @GetMapping("/admin")
    public String showUsers(Model model) {
        if (model.containsAttribute("isError") && (boolean)model.asMap().get("isError")) {
            model.addAttribute("users",null);
        } else {
            ResponseEntity<List<User>> entity = null;
            try {
                entity = userService.getAll();
                model.addAttribute("users",entity.getBody());
            }
            catch (HttpServerErrorException|HttpClientErrorException ex) {
                HttpStatus httpStatus = ex.getStatusCode();
                model.addAttribute("Annotation",
                        "Result of reading: " + httpStatus.getReasonPhrase());
            }
        }

        return "user";
    }

    @GetMapping("/any")
    public ResponseEntity Conf() {
        ResponseEntity entity = userService.getAny();
        return entity;

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Custom header", "max");

//        return new ResponseEntity("Custom header set", headers,HttpStatus.OK);
//        return ResponseEntity.ok()
//                .header("Custom-Header", "foo")
//                .body("Custom header set");

//        userService.getAny();
//        return new ModelAndView();
    }


    @GetMapping("/admin/register")
    public String getNewUserForm(Model model) {
        ResponseEntity<List<Role>> entity = roleService.getAll();
        List<Role> roles =  entity.getBody();
        model.addAttribute("allRoles", roles);
        model.addAttribute("user", new User());
//        model.addAttribute("selectedRoles",new int[]{2});

//        List<Role> roleSelected = new ArrayList<>();
//        model.addAttribute("roleSelected", roleSelected);

        return "new";
    }

    @PostMapping("admin/register")
    public String AddUser(Model model,
                          @ModelAttribute User user,
                          @RequestParam(value="selectedRoles") List<Integer> rolesId,
                          RedirectAttributes redirectAttributes) {


        Set<Role> roles =  rolesId.stream().map(id -> {
            Role role = new Role();
            role.setId(id);
            return role;
        }).collect(Collectors.toSet());

//        Set<Role> setRoles = roles.stream().collect(Collectors.toSet());
//        Role role = new Role(roleName);
//        Set<Role> roles = new HashSet<>();
//        roles.add(role);
        user.setRoles(roles);

        ResponseEntity<User> entity = null;
        HttpStatus httpStatus = null;
        try {
            entity = userService.save(user);
            httpStatus = entity.getStatusCode();
        }
        catch (HttpServerErrorException|HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        }
        redirectAttributes.addFlashAttribute("isError", (httpStatus != HttpStatus.CREATED));
        redirectAttributes.addFlashAttribute("Annotation", "Result of create: " + httpStatus.getReasonPhrase());

        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String getEditUser(@RequestParam(value = "id") long id, Model model,
                              RedirectAttributes redirectAttributes) {
        ResponseEntity<User> entity = null;
        HttpStatus httpStatus = null;
        try {
            entity = userService.get(id);
            httpStatus = entity.getStatusCode();
        }
        catch (HttpServerErrorException|HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        }

        if (httpStatus == HttpStatus.OK) {
            User user = entity.getBody();
            model.addAttribute("user", user);

            ResponseEntity<List<Role>> entityRoles = roleService.getAll();
            List<Role> roles =  entityRoles.getBody();
            model.addAttribute("allRoles", roles);
            model.addAttribute("selectedRoles", user.getRoles().stream().map(role -> role.getId()).collect(Collectors.toList()));

            return "edit";
        } else {
            redirectAttributes.addFlashAttribute("isError", true);
            redirectAttributes.addFlashAttribute("Annotation", "Error of reading user");
            return "redirect:/admin";
        }
    }

    @PostMapping("/admin/edit")
    public String EditUser(@ModelAttribute User user,
                           @RequestParam(value="selectedRoles") List<Integer> rolesId,
                           RedirectAttributes redirectAttributes) {

        ResponseEntity<User> entity = null;
        HttpStatus httpStatus = null;
        try {
            entity = userService.update(user, rolesId);
            httpStatus = entity.getStatusCode();
        }
        catch (HttpServerErrorException|HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        }
        redirectAttributes.addFlashAttribute("isError", (httpStatus != HttpStatus.OK));
        redirectAttributes.addFlashAttribute("Annotation", "Result of update: " + httpStatus.getReasonPhrase());

        return "redirect:/admin";
    }

    @GetMapping("/admin/delete")
    public String DeleteUser(@RequestParam(value = "id") long id,
                             RedirectAttributes redirectAttributes) {
        ResponseEntity<User> entity = null;
        HttpStatus httpStatus = null;
        try {
            entity = userService.delete(id);
            httpStatus = entity.getStatusCode();
        }
        catch (HttpServerErrorException|HttpClientErrorException ex) {
            httpStatus = ex.getStatusCode();
        }
        redirectAttributes.addFlashAttribute("isError", (httpStatus != HttpStatus.NO_CONTENT));
        redirectAttributes.addFlashAttribute("Annotation", "Result of delete: " + httpStatus.getReasonPhrase());

        return "redirect:/admin";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/main")
    public String getMainPage(Model model) {
        return "main";
    }

    @ModelAttribute("user")
    public User createUser() {
        return new User();
    }

//    @PostMapping("/login")
//    public String Login(@RequestParam("login") String login,
//                               @RequestParam("password") String password,
//                               Model model) {
//
//        User user = userService.getByLogin(login);
//        String destination = "redirect:/login";
//        if (user!=null) {
//            if (password.equals(user.getPassword())) {
//                if ("admin".equals(user.getRole())) {
//                    destination = "redirect:/admin";
//                } else {
//                    destination = "redirect:/main";
//                }
//            } else {
//                user = null;
//            }
//        }
//        model.addAttribute("userAuth",user);
//
//        return destination;
//    }

    @GetMapping("/accessDenied")
    public String accessDeniedPage(Model model) {
        return "accessDenied";
    }

//    @GetMapping("/default")
//    public String redirectDefault(HttpServletRequest request) {
//        if (request.isUserInRole("admin")) {
//            return "redirect: /admin";
//        }
//        return "redirect: /main";
//    }

}