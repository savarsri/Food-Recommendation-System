package com.frs.ReviewMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private int reviewId;
    private float rating;
    private String ratingText;
    private Date reviewDate;

    private int restaurantId;
    private String restaurantName;
    private String localityVerbose;
    private double aggregateRating;
}

