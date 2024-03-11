package com.motorcycleparts.motorcycleparts_master.controller;

import com.motorcycleparts.motorcycleparts_master.model.user.User;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/v1")
public class TestController {
    @PostMapping("/v2")
    public ResponseEntity<String> Test(@RequestBody User user){
        System.out.println(user.getEmail());
        return ResponseEntity.ok("Test sucess!!");
    }
}
