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
import java.util.Arrays;
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
    @PostMapping("/create-discount")
    public ResponseEntity<String> createDiscount(@RequestBody DiscountRequest request) {
        String code = request.getCode();
        String name = request.getName();
        String description = request.getDescription();
        int percentDiscount = request.getDiscount();
        LocalDateTime startDate = request.getStartDate();
        LocalDateTime endDate = request.getEndDate();
        List<Long> sparePartsIds = request.getSparePartsIds();

        Discount discount = Discount.builder()
                .code(code)
                .name(name)
                .description(description)
                .discount(percentDiscount)
                .startDate(startDate)
                .endDate(endDate)
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
    public ResponseEntity<String> updateDiscount(@PathVariable Long id, @RequestBody DiscountRequest request) {
        Discount discount = discountRepository.findById(id).orElse(null);
        if (discount != null) {
            discount.setCode(request.getCode());
            discount.setName(request.getName());
            discount.setDescription(request.getDescription());
            discount.setDiscount(request.getDiscount());
            discount.setStartDate(request.getStartDate());
            discount.setEndDate(request.getEndDate());
            discountRepository.save(discount);
            return ResponseEntity.ok("Discount updated successfully");
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
}