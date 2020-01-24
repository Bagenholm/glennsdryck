package iths.glenn.drick.controller;

import iths.glenn.drick.entity.UserEntity;
import iths.glenn.drick.exception.UserNotFoundException;
import iths.glenn.drick.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("")
    public void register(@RequestBody UserEntity user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @GetMapping("/{username}")
    public UserEntity findUserByUsername(@PathVariable String username) {
        return userRepository.findById(username).orElseThrow(() -> new UserNotFoundException("Couldn't find " + username));
    }

    @PostMapping("/registeradmin/{username}")
    public UserEntity registerAdmin(@PathVariable String username){
        UserEntity user = userRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("Couldn't find " + username));
        if(user.getRoles().contains("admin")){
            return user;
        }
        user.addRole("admin");
        return userRepository.save(user);
    }
}
