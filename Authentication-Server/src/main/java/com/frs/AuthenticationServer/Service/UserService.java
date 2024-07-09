package com.frs.AuthenticationServer.Service;

import com.frs.AuthenticationServer.Model.ErrorResponse;
import com.frs.AuthenticationServer.Model.User;
import com.frs.AuthenticationServer.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public ResponseEntity<?> createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>(new ErrorResponse(406, "User already exists"), HttpStatus.NOT_ACCEPTABLE);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }
    }

}
