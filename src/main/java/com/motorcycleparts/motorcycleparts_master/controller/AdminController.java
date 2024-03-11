package com.motorcycleparts.motorcycleparts_master.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/demo-admin")
public class AdminController {
    @GetMapping
    public String get(){
        return "GET-ADMIN Controler";
    }

    @PostMapping
    public String post(){
        return "POST-ADMIN Controler";
    }

    @PutMapping
    public String put(){
        return "PUT-ADMIN Controler";
    }
    @DeleteMapping
    public String delete(){
        return "DELETE-ADMIN Controler";
    }

}
