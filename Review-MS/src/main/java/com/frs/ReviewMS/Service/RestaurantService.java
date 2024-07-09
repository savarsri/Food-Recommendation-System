package com.frs.ReviewMS.Service;

import com.frs.ReviewMS.Model.Restaurant;
import com.frs.ReviewMS.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant findById(int id){
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return restaurantOptional.orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }
}
