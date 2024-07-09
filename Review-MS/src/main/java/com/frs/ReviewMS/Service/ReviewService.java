package com.frs.ReviewMS.Service;

import com.frs.ReviewMS.DTO.ReviewDTO;
import com.frs.ReviewMS.Model.Review;
import com.frs.ReviewMS.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ResponseEntity<?> saveReview(Review review){
        try {
            reviewRepository.save(review);
            return new ResponseEntity<>("Review Added!", HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>("Error Occurred will creating review", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ReviewDTO> findReviewsOfUserWithRestaurantInfo(int userId, Pageable pageable) {
        try {
            return reviewRepository.findReviewsWithRestaurantInfoUsingUserId(userId, pageable);
        } catch (Exception ex) {
            // Log the exception for debugging purposes
            ex.printStackTrace();
            // Propagate the exception to the caller or rethrow a more specific exception
            throw new RuntimeException("Error occurred while fetching reviews with restaurant info for user with ID: " + userId, ex);
        }
    }
}
