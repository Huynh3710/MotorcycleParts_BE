package com.motorcycleparts.motorcycleparts_master.controller;

import com.motorcycleparts.motorcycleparts_master.model.provinces.Province;
import com.motorcycleparts.motorcycleparts_master.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class ShippingController {
    // add shipping rate
    private final ProvinceRepository provinceRepository;
    @PostMapping("/add-shipping-rate/{provinceCode}/{shippingRate}")
    public ResponseEntity<?> addShippingRate(@PathVariable("provinceCode") String provinceCode,
                                             @PathVariable("shippingRate") Double shippingRate) {
        Province province = provinceRepository.findProvinceByCode(provinceCode);
        if (province == null) {
            return ResponseEntity.badRequest().body("Province not found");
        }
        province.setShippingRate(shippingRate);
        provinceRepository.save(province);
        return ResponseEntity.ok("Create discount success");
    }
    // update shipping rate
//    @PutMapping("/update-shipping-rate/{provinceCode}/{shippingRate}")
//    public ResponseEntity<?> updateShippingRate(@PathVariable("provinceCode") String provinceCode,
//                                                @PathVariable("shippingRate") Double shippingRate) {
//        Province province = provinceRepository.findProvinceByCode(provinceCode);
//        if (province == null) {
//            return ResponseEntity.badRequest().body("Province not found");
//        }
//        province.setShippingRate(shippingRate);
//        return ResponseEntity.ok("Update discount success");
//    }
    // delete shipping rate
    @DeleteMapping("/delete-shipping-rate/{provinceCode}")
    public ResponseEntity<?> deleteShippingRate(@PathVariable("provinceCode") String provinceCode) {
        Province province = provinceRepository.findProvinceByCode(provinceCode);
        if (province == null) {
            return ResponseEntity.badRequest().body("Province not found");
        }
        province.setShippingRate(null);
        provinceRepository.save(province);
        return ResponseEntity.ok("Delete discount success");
    }
    // get all shipping rate
    @GetMapping("/get-all-shipping-rate")
    public ResponseEntity<?> getAllShippingRate() {
        return ResponseEntity.ok(provinceRepository.findByShippingRateIsNotNull());
    }
    // get shipping rate by id
    @GetMapping("/get-shipping-rate-by-id/{provinceCode}")
    public ResponseEntity<?> getShippingRateById(@PathVariable("provinceCode") String provinceCode) {
        Province province = provinceRepository.findProvinceByFullName(provinceCode);
        System.out.println("getFullProvince"+province.getFullName());
        if (province == null) {
            return ResponseEntity.badRequest().body("Province not found");
        }
        return ResponseEntity.ok(province);
    }

    //search shipping rate by province name
//    @GetMapping("/search-shipping-rate-by-province-name/{provinceName}")
//    public ResponseEntity<?> searchShippingRateByProvinceName(@PathVariable("provinceName") String provinceName) {
//        List<Province> province;
//        if (provinceName == null || provinceName.isEmpty() || provinceName.equals("")) {
//            province = provinceRepository.findAll();
//        } else {
//            province = provinceRepository.findProvinceByFullNameContaining(provinceName);
//        }
//        return ResponseEntity.ok(province);
//    }
    @GetMapping("/search-shipping-rate-by-province-name")
    public ResponseEntity<?> searchShippingRateByProvinceName(@RequestParam(value = "provinceName", required = false) String provinceName) {
        List<Province> province;
        if (provinceName == null || provinceName.isEmpty()) {
            province = provinceRepository.findAll();
        } else {
            province = provinceRepository.findProvinceByFullNameContaining(provinceName);
        }
        return ResponseEntity.ok(province);
    }

}
