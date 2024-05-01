package com.motorcycleparts.motorcycleparts_master.controller;

import com.motorcycleparts.motorcycleparts_master.Dto.ReviewsDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperReviewsImpl;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.repository.CustomerRepository;
import com.motorcycleparts.motorcycleparts_master.repository.OrderRepository;
import com.motorcycleparts.motorcycleparts_master.repository.ReviewsRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class ReviewsController {

    private final OrderRepository orderRepository;
    private final SparePartsRepository sparePartsRepository;
    private final CustomerRepository customerRepository;
    private final ReviewsRepository reviewsRepository;
    private final MapperReviewsImpl mapperReviews;

    @PostMapping("/create-reviews")
    public ResponseEntity<String> createReview(@RequestBody ReviewRequest request) {
        Long orderId = request.getOrderId();
        Long sparePartsId = request.getSparePartsId();
        Long customerId = request.getCustomerId();
        String content = request.getContent();
        Integer rating = request.getRating();

        // Tìm đánh giá của khách hàng cho sản phẩm
        Optional<Reviews> optionalReview = Optional.ofNullable(reviewsRepository.findBySparePartsIdAndCustomerId(sparePartsId, customerId));
        if (optionalReview.isPresent()) {
            // Nếu đánh giá đã tồn tại, cập nhật nội dung, số sao và thời gian
            Reviews review = optionalReview.get();
            review.setContent(content);
            review.setRating(rating);
            review.setDate(new Date());
            reviewsRepository.save(review);
            return ResponseEntity.ok("Review updated successfully");
        }

        // Tìm đơn hàng
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.badRequest().body("Order not found");
        }
        Order order = optionalOrder.get();
        if (order.getOrderStatus().getStatus() != Status.DELIVERED) {
            return ResponseEntity.badRequest().body("Order is not delivered yet");
        }

        // Kiểm tra xem người dùng đã mua sản phẩm trong đơn hàng hay chưa
        boolean hasBoughtProduct = order.getOrderDetails().stream()
                .anyMatch(orderDetail -> orderDetail.getSpareParts().getId().equals(sparePartsId));
        if (!hasBoughtProduct) {
            return ResponseEntity.badRequest().body("Customer hasn't bought this product");
        }

        // Tạo đánh giá mới
        Reviews review = new Reviews();
        review.setContent(content);
        review.setRating(rating);
        review.setDate(new Date());

        // Liên kết với sản phẩm, khách hàng và đơn hàng
        Optional<SpareParts> optionalSpareParts = sparePartsRepository.findById(sparePartsId);
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalSpareParts.isPresent() && optionalCustomer.isPresent()) {
            SpareParts spareParts = optionalSpareParts.get();
            Customer customer = optionalCustomer.get();
            review.setSpareParts(spareParts);
            review.setCustomer(customer);
            reviewsRepository.save(review);
            return ResponseEntity.ok("Review created successfully");
        }

        return ResponseEntity.badRequest().body("Invalid spare parts or customer");
    }

    @GetMapping("/by-sparePartsId/{sparePartsId}")
    public ResponseEntity<List<ReviewsDto>> getReviewsBySparePartsId(@PathVariable Long sparePartsId) {
        List<Reviews> reviews = reviewsRepository.findAllBySparePartsId(sparePartsId);
        return ResponseEntity.ok(reviews.stream().map(mapperReviews::mapTo).toList());
    }


    @GetMapping("/reviews-statistics/{sparePartsId}")
    public ResponseEntity<ReviewsResponse> getReviewsStatisticsBySparePartsId(@PathVariable Long sparePartsId) {
        List<Reviews> reviews = reviewsRepository.findAllBySparePartsId(sparePartsId);

        int totalReviews = reviews.size();
        int fiveStar = 0, fourStar = 0, threeStar = 0, twoStar = 0, oneStar = 0;
        float totalRating = 0;

        for (Reviews review : reviews) {
            int rating = review.getRating();
            totalRating += rating;

            switch (rating) {
                case 5: fiveStar++; break;
                case 4: fourStar++; break;
                case 3: threeStar++; break;
                case 2: twoStar++; break;
                case 1: oneStar++; break;
            }
        }

        float averageRating = totalReviews > 0 ? totalRating / totalReviews : 0;

        ReviewsResponse response = new ReviewsResponse();
        response.setAverageRating(averageRating);
        response.setTotalReviews(totalReviews);
        response.setFiveStar(fiveStar);
        response.setFourStar(fourStar);
        response.setThreeStar(threeStar);
        response.setTwoStar(twoStar);
        response.setOneStar(oneStar);

        return ResponseEntity.ok(response);
    }


    // Controller để lấy tất cả đánh giá của sparePart thông qua SparePartsId và customerId
    @GetMapping("/by-sparePartsId-and-customerId/{sparePartsId}/{customerId}")
    public ResponseEntity<?> getReviewsBySparePartsIdAndCustomerId(
            @PathVariable Long sparePartsId,
            @PathVariable Long customerId) {
        Reviews review = reviewsRepository.findBySparePartsIdAndCustomerId(sparePartsId, customerId);
        if (review != null) {
            return ResponseEntity.ok(mapperReviews.mapTo(review));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đánh giá cho sparePartsId và customerId đã cho.");
        }
    }

}
