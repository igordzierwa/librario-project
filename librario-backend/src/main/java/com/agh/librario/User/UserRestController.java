package com.agh.librario.User;

import com.agh.librario.Role.Role;
import com.agh.librario.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/rest/registration")
    public String registration(Model model) {
        return "To register user perform a POST request";
    }


    @PostMapping(value = "/rest/test")
    public ResponseEntity<String> postFoos() {
        return new ResponseEntity<>("Your message here", HttpStatus.OK);

    }

    @PostMapping(value = "/rest/user/registration")
    public String registerUserAccount(@Valid User userData, HttpServletRequest request) {
        log.info("Registering user account with information: {}", userData);
        userService.save(userData, true);
        return "user added";
    }

    @GetMapping("/rest/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }


        //TEST SESSION
    @RequestMapping(value = "/rest/session", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<String>> currentUserNameSimple(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = auth.getAuthorities().stream().map(r->r.getAuthority()).collect(Collectors.toSet());

        return ResponseEntity.ok(roles);
    }

    //getID
    @RequestMapping(value = "/rest/getID", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Long> getUserID(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        log.info(username);
        return ResponseEntity.ok(userRepository.findByUsername(username).getId());
    }



    @GetMapping("/rest/test_tylko_zalogowany")
    public String login() {
        return "OK - dostep tylko dla zalogowanego";
    }
}
