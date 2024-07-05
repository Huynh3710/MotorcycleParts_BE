package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperSparePartsImpl;
import com.motorcycleparts.motorcycleparts_master.model.Discount;
import com.motorcycleparts.motorcycleparts_master.model.DiscountRequest;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import com.motorcycleparts.motorcycleparts_master.repository.DiscountRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountRepository discountRepository;
    private final SparePartsRepository sparePartsRepository;
    private final MapperSparePartsImpl mapperSpareParts;


    //get all discount
    @GetMapping("/get-all-discounts")
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        return ResponseEntity.ok(discounts);
    }


    //create discount
//    @PostMapping("/create-discount")
//    public ResponseEntity<String> createDiscount(@RequestBody DiscountRequest request) {
//        String code = request.getCode();
//        String name = request.getName();
//        String description = request.getDescription();
//        int percentDiscount = request.getDiscount();
//        LocalDateTime startDate = request.getStartDate();
//        LocalDateTime endDate = request.getEndDate();
//        List<Long> sparePartsIds = request.getSparePartsIds();
//
//        Discount discount = Discount.builder()
//                .code(code)
//                .name(name)
//                .description(description)
//                .discount(percentDiscount)
//                .startDate(startDate)
//                .endDate(endDate)
//                .build();
//
//        discount = discountRepository.save(discount);
//
//        if (sparePartsIds != null) {
//            for (Long id : sparePartsIds) {
//                SpareParts part = sparePartsRepository.findById(id).orElse(null);
//                if (part != null) {
//                    part.setDiscount(discount);
//                    sparePartsRepository.save(part);
//                }
//            }
//        }
//
//        return ResponseEntity.ok("Discount created successfully");
//    }
    @PostMapping("/create-discount")
    public ResponseEntity<String> createDiscount(@RequestBody DiscountRequest request) {
        String code = request.getCode();
        String name = request.getName();
        String description = request.getDescription();
        int percentDiscount = request.getDiscount();
        LocalDateTime startDate = request.getStartDate().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime endDate = request.getEndDate();
        List<Long> sparePartsIds = request.getSparePartsIds();

        // Kiểm tra startDate để xác định giá trị isActive và discount
        boolean isActive = !startDate.isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        int discountValue = isActive ? percentDiscount : 0;

        Discount discount = Discount.builder()
                .code(code)
                .name(name)
                .description(description)
                .discount(discountValue)
                .tempDiscount(percentDiscount)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(!startDate.isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) // Set isActive khi khởi tạo Discount
                .build();
        discount = discountRepository.save(discount);

        if (sparePartsIds != null) {
            for (Long id : sparePartsIds) {
                SpareParts part = sparePartsRepository.findById(id).orElse(null);
                if (part != null) {
                    part.setDiscount(discount);
                    sparePartsRepository.save(part);
                }
            }
        }

        return ResponseEntity.ok("Discount created successfully");
    }



    //update discount
    @PutMapping("/update-discount/{id}")
    public ResponseEntity<?> updateDiscount(@PathVariable Long id, @RequestBody DiscountRequest request) {
        Discount discount = discountRepository.findById(id).orElse(null);
        if (discount != null) {
            List<SpareParts> sparePartsList = sparePartsRepository.findAllByDiscountId(discount.getId());
            for (SpareParts sparePart : sparePartsList) {
                sparePart.setDiscount(null);
                sparePartsRepository.save(sparePart);
            }
            discount.setCode(request.getCode());
            discount.setName(request.getName());
            discount.setDescription(request.getDescription());

            LocalDateTime startDate = request.getStartDate().truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime endDate = request.getEndDate();

            // Kiểm tra startDate để xác định giá trị isActive và discount
            boolean isActive = !startDate.isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            int discountValue = isActive ? request.getDiscount() : 0;

            discount.setDiscount(discountValue);
            discount.setTempDiscount(request.getDiscount());
            discount.setStartDate(startDate);
            discount.setEndDate(endDate);
            discount.setActive(isActive);

            discountRepository.save(discount);

            List<Long> sparePartsIds = request.getSparePartsIds();
            if (sparePartsIds != null) {
                for (Long sparePartsId : sparePartsIds) {
                    SpareParts sparePart = sparePartsRepository.findById(sparePartsId).orElse(null);
                    if (sparePart != null) {
                        sparePart.setDiscount(discount);
                        sparePartsRepository.save(sparePart);
                    }
                }
            }
            return ResponseEntity.ok(discount);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Discount not found");
        }
    }



    //delete discount
    @DeleteMapping("/delete-discount/{id}")
    public ResponseEntity<String> deleteDiscount(@PathVariable Long id) {
        Discount discount = discountRepository.findById(id).orElse(null);
        if (discount != null) {
            // Hủy liên kết giữa Discount và SparePart
            for (SpareParts sparePart : discount.getSpareParts()) {
                sparePart.setDiscount(null);
                sparePartsRepository.save(sparePart);
            }
            // Xóa Discount sau khi đã hủy liên kết
            discountRepository.delete(discount);
            return ResponseEntity.ok("Discount deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Discount not found");
        }
    }


    //get all discount
    //get discount by id
    @GetMapping("/get-discount/{id}")
    public ResponseEntity<Discount> getDiscountById(@PathVariable Long id) {
        Discount discount = discountRepository.findById(id).orElse(null);
        if (discount != null) {
            return ResponseEntity.ok(discount);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/get-spare-parts-by-discount/{id}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByDiscount(@PathVariable Long id) {
        Discount discount = discountRepository.findById(id).orElse(null);
        if (discount != null) {
            List<SpareParts> spareParts = sparePartsRepository.findAllByDiscountId(discount.getId());
            return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //search discount by name or code
//    @GetMapping("/search-discount/{keyword}")
//    public ResponseEntity<List<Discount>> searchDiscount(@PathVariable String keyword) {
//        List<Discount> discounts;
//        if (keyword == null || keyword.isEmpty()) {
//            discounts = discountRepository.findAll();
//        } else {
//            discounts = discountRepository.findDiscountByNameContainingOrCodeContaining(keyword, keyword);
//        }
//        return ResponseEntity.ok(discounts);
//    }
    @GetMapping("/search-discount")
    public ResponseEntity<List<Discount>> searchDiscount(@RequestParam(value = "keyword", required = false) String keyword) {
        List<Discount> discounts;
        if (keyword == null || keyword.isEmpty()) {
            discounts = discountRepository.findAll();
        } else {
            discounts = discountRepository.findDiscountByNameContainingOrCodeContaining(keyword, keyword);
        }
        return ResponseEntity.ok(discounts);
    }

}