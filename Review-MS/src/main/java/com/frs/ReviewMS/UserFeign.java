package com.frs.ReviewMS;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("USER-MS")
public interface UserFeign {
    @GetMapping("user-ms/1")
    public ResponseEntity<List<String>> user();
}
