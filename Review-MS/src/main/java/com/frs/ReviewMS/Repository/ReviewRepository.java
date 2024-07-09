package com.frs.ReviewMS.Repository;

import com.frs.ReviewMS.DTO.ReviewDTO;
import com.frs.ReviewMS.Model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT new com.frs.ReviewMS.DTO.ReviewDTO(" +
            "r.reviewId, r.rating, r.ratingText, r.reviewDate, " +
            "r.restaurant.restaurantId, r.restaurant.restaurantName, " +
            "r.restaurant.localityVerbose, r.restaurant.aggregateRating) " +
            "FROM Review r JOIN r.restaurant " +
            "WHERE r.userId = :userId")
    List<ReviewDTO> findReviewsWithRestaurantInfoUsingUserId(@Param("userId") int userId, Pageable pageable);

}
