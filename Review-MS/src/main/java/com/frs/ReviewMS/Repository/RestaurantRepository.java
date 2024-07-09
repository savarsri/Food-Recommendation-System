package com.frs.ReviewMS.Repository;

import com.frs.ReviewMS.Model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {
}
