package com.frs.ReviewMS.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review-ms")
public class RestaurantController {

    @GetMapping()
    public ResponseEntity<?> getRestaurant(){
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
