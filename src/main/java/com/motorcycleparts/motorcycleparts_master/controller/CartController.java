package com.motorcycleparts.motorcycleparts_master.controller;



import com.motorcycleparts.motorcycleparts_master.Dto.OrderDetailDto;
import com.motorcycleparts.motorcycleparts_master.exception.NotFoundException;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperOrderDetailImpl;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.repository.CartRepository;
import com.motorcycleparts.motorcycleparts_master.repository.OrderDetailRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/user-cart")
//@RequestMapping("/get-api")
@RequiredArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final OrderDetailRepository orderDetailsRepository;
    private final SparePartsRepository sparePartsRepository;
    private final MapperOrderDetailImpl mapperOrderDetail;

    @GetMapping("/cart={cart}")
    public ResponseEntity<List<OrderDetailDto>> getAllCart(@PathVariable("cart") Long cartId) {
        List<OrderDetail> orderDetails = orderDetailsRepository.findOrderDetailByCartId(cartId);
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailDto orderDetailDto = mapperOrderDetail.mapTo(orderDetail);
            orderDetailDtos.add(orderDetailDto);
        }
        return ResponseEntity.ok(orderDetailDtos);
    }


    @PostMapping("/add-to-cart")
    public ResponseEntity<OrderDetailDto> addToCart(@RequestBody CartItem cartItem) {
        System.out.println("cartItem = " + cartItem);
        Cart cart = cartRepository.findById(cartItem.getCartId())
                .orElseThrow(() -> new NotFoundException("cart not found, " + "id=" + cartItem.getCartId()));
//        System.out.println("cart = "+cart);

        SpareParts spareParts = sparePartsRepository.findById(cartItem.getSparePartId())
                .orElseThrow(() -> new NotFoundException("spare part not found, " + "id=" + cartItem.getSparePartId()));

        List<OrderDetail> orderDetails = orderDetailsRepository.findOrderDetailByCartId(cart.getId());
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getSpareParts().getId().equals(cartItem.getSparePartId())) {
                orderDetail.setQuantity(orderDetail.getQuantity() + cartItem.getQuantity());
                orderDetail.setPrice(orderDetail.getPrice());
                orderDetailsRepository.save(orderDetail);
                return ResponseEntity.ok(mapperOrderDetail.mapTo(orderDetail));
            }
        }
        OrderDetail orderDetail = OrderDetail.builder()
                .cart(cart)
                .spareParts(spareParts)
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
        orderDetailsRepository.save(orderDetail);
        return ResponseEntity.ok(mapperOrderDetail.mapTo(orderDetail));
    }

    @DeleteMapping("/delete-cart/{cartId}&{orderDetailId}")
    public ResponseEntity<List<OrderDetailDto>> deleteCart(@PathVariable("cartId") Long cartId, @PathVariable("orderDetailId") Long orderDetailId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("cart not found, " + "id=" + cartId));
        OrderDetail orderDetail = orderDetailsRepository.findById(orderDetailId)
                .orElseThrow(() -> new NotFoundException("order detail not found, " + "id=" + orderDetailId));

        List<OrderDetail> orderDetails = orderDetailsRepository.findOrderDetailByCartId(cart.getId());
        for (OrderDetail o : orderDetails) {
            if (o.getId().equals(orderDetail.getId())) {
                orderDetailsRepository.delete(o);
            }
        }
        cartRepository.save(cart);
        cart.setOrderDetails(orderDetails);
        List<OrderDetailDto> orderDetailDtos = orderDetailsRepository.findOrderDetailByCartId(cartId).stream().map(mapperOrderDetail::mapTo).toList();
        return ResponseEntity.ok(orderDetailDtos);
    }

    @PutMapping("/update-quanlity")
    public ResponseEntity<List<OrderDetailDto>> updateQuantity(@RequestParam("cartId") Long cartId, @RequestParam("orderDetailId") Long orderDetailId, @RequestParam("calculateMethod") String calculateMethod) {

        List<OrderDetail> orderDetails = orderDetailsRepository.findOrderDetailByCartId(cartId);
        System.out.println("orderDetails = " + orderDetails.size());
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getId().equals(orderDetailId)) {
                if (calculateMethod.equals("plus")) {
                    orderDetail.setQuantity(orderDetail.getQuantity() + 1);
                    orderDetail.setPrice((orderDetail.getPrice()));

                } else if(calculateMethod.equals("minus")) {
                    if (orderDetail.getQuantity() == 1) {
                        orderDetailsRepository.delete(orderDetail);
                        orderDetails.remove(orderDetail);
                        return ResponseEntity.ok(orderDetails.stream().map(mapperOrderDetail::mapTo).toList());
                    }
                    else {
                        orderDetail.setQuantity(orderDetail.getQuantity() - 1);
                        orderDetail.setPrice((orderDetail.getPrice()));
                    }
                }
                else {
                    orderDetail.setQuantity(orderDetail.getQuantity());
                    orderDetail.setPrice((orderDetail.getPrice()));
                }
                orderDetailsRepository.save(orderDetail);
                break;
            }
        }
        System.out.println("orderDetails = " + orderDetails.size());
        return ResponseEntity.ok(orderDetails.stream().map(mapperOrderDetail::mapTo).toList());
    }
}




