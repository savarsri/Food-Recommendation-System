package com.frs.ReviewMS.Controller;

import com.frs.ReviewMS.DTO.ReviewDTO;
import com.frs.ReviewMS.Model.Review;
import com.frs.ReviewMS.Service.RestaurantService;
import com.frs.ReviewMS.Service.ReviewService;
import com.frs.ReviewMS.UserFeign;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/review-ms")
public class ReviewController {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RestaurantService restaurantService;


    @PostMapping("/create-review")
    public ResponseEntity<?> createReview(@RequestBody Review review){
        if (review.getUserId()!=0 && review.getRestaurantId()!=0){
            review.setReviewDate(new Date());
            return reviewService.saveReview(review);
        }
        return new ResponseEntity<>("Error Occurred will adding review",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/fetch-reviews")
    public ResponseEntity<?> fetchReviewsByUser(@RequestBody Review review,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        try {
            if (review.getUserId() != 0) {
                Pageable pageable = PageRequest.of(page, size);
                List<ReviewDTO> reviewDTOs = reviewService.findReviewsOfUserWithRestaurantInfo(review.getUserId(), pageable);
                return new ResponseEntity<>(reviewDTOs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User ID is required", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Error occurred while fetching reviews", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/status")
    public String hello(HttpServletRequest request){
        return "Hello from review";
    }

    @GetMapping("/users")
    public ResponseEntity<List<String>> userList(){
        List<String> users = userFeign.user().getBody();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
