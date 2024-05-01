package com.motorcycleparts.motorcycleparts_master.service;


import com.motorcycleparts.motorcycleparts_master.model.Discount;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import com.motorcycleparts.motorcycleparts_master.repository.DiscountRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class DiscountService {
    private final DiscountRepository discountRepository;
    private final SparePartsRepository sparePartsRepository;

//    @Scheduled(fixedRate = 60000)
//    public void checkDiscounts() {
//        System.out.println("Checking discounts");
//        List<Discount> discounts = discountRepository.findAll();
//        LocalDateTime now = LocalDateTime.now();
//        for (Discount discount : discounts) {
//            if (discount.getEndDate().isBefore(now)) {
//                List<SpareParts> sparePartsList = discount.getSpareParts();
//                if (sparePartsList != null) {
//                    for (SpareParts part : sparePartsList) {
//                        part.setDiscount(null);
//                        sparePartsRepository.save(part);
//                    }
//                }
//                discount.setSpareParts(null);
//                discountRepository.save(discount);
//                discountRepository.delete(discount);
//            }
//        }
//    }
}
