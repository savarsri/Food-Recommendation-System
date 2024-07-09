package com.frs.ReviewMS.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private int restaurantId;

    private String restaurantName;

    private int countryCode;

    private String city;

    private String address;

    private String locality;

    private String localityVerbose;

    private double longitude;

    private double latitude;

    private String cuisines;

    private int averageCostForTwo;

    private String currency;

    private String hasTableBooking;

    private String hasOnlineDelivery;

    private String isDeliveringNow;

    private String switchToOrderMenu;

    private int priceRange;

    private double aggregateRating;

    private String ratingColor;

    private String ratingText;

    private int votes;
}
