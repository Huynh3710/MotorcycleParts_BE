package com.motorcycleparts.motorcycleparts_master.service;


import com.motorcycleparts.motorcycleparts_master.model.Discount;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import com.motorcycleparts.motorcycleparts_master.repository.DiscountRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor

public class DiscountService {
    private final DiscountRepository discountRepository;
    private final SparePartsRepository sparePartsRepository;

//    @Scheduled(fixedRate = 60000)
//    public void checkDiscounts() {
//        System.out.println("Checking discounts");
//        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
//        List<Discount> discounts = discountRepository.findAll();
//
//        for (Discount discount : discounts) {
//            if (!discount.isActive() && !discount.getStartDate().isAfter(now)) {
//                discount.setActive(true);
//                discount.setDiscount(discount.getTempDiscount());
//                discountRepository.save(discount);
//            }
//            if (discount.getEndDate().isBefore(now)) {
//                discount.setActive(false);
//                discount.setDiscount(0);
//                discountRepository.save(discount);
//            }
//        }
//    }
@Scheduled(cron = "0 * * * * *")
public void checkDiscounts() {
    System.out.println("Checking discounts");
    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    List<Discount> discounts = discountRepository.findAll();

    for (Discount discount : discounts) {
        if (!discount.isActive() && !discount.getStartDate().isAfter(now)) {
            discount.setActive(true);
            discount.setDiscount(discount.getTempDiscount());
            discountRepository.save(discount);
        }
        if (discount.getEndDate().isBefore(now) || discount.getEndDate().isEqual(now)) {
            discount.setActive(false);
            discount.setDiscount(0);
            discountRepository.save(discount);
        }
    }
}




}
