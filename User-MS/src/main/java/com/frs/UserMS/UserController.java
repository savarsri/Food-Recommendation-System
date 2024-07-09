package com.frs.UserMS;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user-ms")
public class UserController {

    @GetMapping("1")
    public ResponseEntity<List<String>> user(HttpServletRequest request){
        System.out.println(request.getHeader("Api-Token"));
        ArrayList<String> users = new ArrayList<String>();
        users.add("Savar");
        users.add("Harshit");
        users.add("Sanju");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
