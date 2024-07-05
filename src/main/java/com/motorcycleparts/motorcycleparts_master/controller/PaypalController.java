package com.motorcycleparts.motorcycleparts_master.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcycleparts.motorcycleparts_master.Dto.CheckOutRequest;
import com.motorcycleparts.motorcycleparts_master.Dto.OrderDetailDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperOrderImple;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.repository.OrderDetailRepository;
import com.motorcycleparts.motorcycleparts_master.repository.OrderRepository;
import com.motorcycleparts.motorcycleparts_master.repository.OrderStatusRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import com.motorcycleparts.motorcycleparts_master.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/user-payment")
public class PaypalController {
    private final String BASE = "https://api-m.sandbox.paypal.com";
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final MapperOrderImple mapperOrder;
    private final SparePartsRepository sparePartsRepository;
    private final OrderDetailRepository orderDetailRepository;
    private String getAuth(String client_id, String app_secret) {
        String auth = client_id + ":" + app_secret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    public String generateAccessToken() {
        String auth = this.getAuth("AQa9pPU77zfuBeTlm3RkB3p-eY52qbi9Y5nqyHgNy_mW-gB8eaFW5Tn1JLj0AfiG25PE4sgphJQwYjsD", "EA_jHAG4_ZUslgpa2g7U1UwDXwxaIX2tqTcbrCJiLu9DXxtlxEsEZDgzum9_G4wbM3KHHDwqSuaqzdCd");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<String> response = restTemplate.postForEntity(BASE + "/v1/oauth2/token", request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.log(Level.INFO, "GET TOKEN: SUCCESSFUL!");
            return new JSONObject(response.getBody()).getString("access_token");
        } else {
            LOGGER.log(Level.SEVERE, "GET TOKEN: FAILED!");
            return "Unavailable to get ACCESS TOKEN, STATUS CODE " + response.getStatusCode();
        }
    }


    @PostMapping("/orders/by-now/{sparePartId}/{quantity}/{cid}")
    public OrderDetail createOrderDetail(@PathVariable Long sparePartId,
                                         @PathVariable int quantity,
                                         @PathVariable Long cid) throws IOException {

        SpareParts spareParts = sparePartsRepository.findById(sparePartId)
                .orElseThrow(() -> new IllegalArgumentException("Spare part not found with id: " + sparePartId));

        OrderDetail orderDetail = OrderDetail.builder()
                .spareParts(spareParts)
                .quantity(quantity)
                .price(spareParts.getUnitPrice() * quantity)
                .build();
        orderDetailRepository.save(orderDetail);

        // Create a CheckOutRequest
        CheckOutRequest paymentRequest = new CheckOutRequest();
        paymentRequest.setCustomerId(cid);
        paymentRequest.setCurrency("USD"); // Set the currency here
        paymentRequest.setAmount(spareParts.getUnitPrice() * quantity); // Set the amount here
        paymentRequest.setOrderDetailsId(Arrays.asList(orderDetail.getId())); // Set the order detail id here

        // Call the createOrder function
        createOrder(paymentRequest);

        return orderDetail;
    }


    @PostMapping("/orders/create")
    public Object createOrder(@RequestBody CheckOutRequest paymentRequest) throws IOException {
        String URL_SUCCESS = "http://localhost:3000";
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("intent", "CAPTURE");
        requestBody.put("payment_source", new JSONObject().put("paypal", new JSONObject().put("experience_context", new JSONObject().put("payment_method_preference", "IMMEDIATE_PAYMENT_REQUIRED").put("user_action", "PAY_NOW").put("return_url", URL_SUCCESS).put("cancel_url", URL_SUCCESS))));
        requestBody.put("purchase_units", new JSONArray().put(new JSONObject().put("amount", new JSONObject().put("currency_code", paymentRequest.getCurrency()).put("value", paymentRequest.getAmount()))));

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        System.out.println("requestBody" + requestBody);

        ResponseEntity<Object> response = restTemplate.exchange(BASE + "/v2/checkout/orders", HttpMethod.POST, entity, Object.class);

        System.out.println("response" + Objects.requireNonNull(response.getBody()));

        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CREATED");
            System.out.println("response" + response.getBody().toString());
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(response.getBody());
            String orderId = new JSONObject(jsonString).getString("id");
            System.out.println("orderId la: " + orderId);
            orderService.createOrder(paymentRequest, orderId);
            return response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }
    }


    private String getCaptureIdFromResponse(Object response) {
        ObjectMapper mapper = new ObjectMapper();
        Map responseMap = mapper.convertValue(response, Map.class);
        List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) responseMap.get("purchase_units");
        Map<String, Object> firstPurchaseUnit = purchaseUnits.get(0);
        Map<String, Object> payments = (Map<String, Object>) firstPurchaseUnit.get("payments");
        List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
        Map<String, Object> firstCapture = captures.get(0);
        return (String) firstCapture.get("id");
    }

    @PostMapping("/orders/{orderId}/CAPTURED")
    public Object capturePayment(@PathVariable("orderId") String orderCode){
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders/" + orderCode + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );
        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CREATED");
            String captureId = getCaptureIdFromResponse(response.getBody());
            System.out.println("captureId" + captureId);
            orderService.captureOrder(orderCode, captureId);
            return  response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }
    }
    @PutMapping("/orders/{orderId}/{orderStatusId}/PREPARING")
    public Object prepareOrder(@PathVariable("orderId") String orderCode,@PathVariable("orderStatusId") Long orderStatusId){
        Order order = orderRepository.findByOrderCodeAndOrderStatusId(orderCode, orderStatusId);
        if (order == null) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElseThrow(() -> new IllegalArgumentException("Order status not found with id: " + orderStatusId));
        orderStatus.setStatus(Status.PREPARING);
        orderStatusRepository.save(orderStatus);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return new ResponseEntity<>(mapperOrder.mapTo(order), HttpStatus.OK);
    }
    @PutMapping("/orders/{orderId}/{orderStatusId}/SHIPMENT")
    public Object shipOrder(@PathVariable("orderId") String orderCode, @PathVariable("orderStatusId") Long orderStatusId){
        Order order = orderRepository.findByOrderCodeAndOrderStatusId(orderCode, orderStatusId);
        if (order == null) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElseThrow(() -> new IllegalArgumentException("Order status not found with id: " + orderStatusId));
        orderStatus.setStatus(Status.SHIPMENT);
        orderStatusRepository.save(orderStatus);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return new ResponseEntity<>(mapperOrder.mapTo(order), HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}/{orderStatusId}/DELIVERED")
    public Object deliverOrder(@PathVariable("orderId") String orderCode, @PathVariable("orderStatusId") Long orderStatusId){
        Order order = orderRepository.findByOrderCodeAndOrderStatusId(orderCode, orderStatusId);
        if (order == null) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElseThrow(() -> new IllegalArgumentException("Order status not found with id: " + orderStatusId));
        orderStatus.setStatus(Status.DELIVERED);
        orderStatusRepository.save(orderStatus);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return new ResponseEntity<>(mapperOrder.mapTo(order), HttpStatus.OK);
    }


    @DeleteMapping("/orders/{orderId}/CANCELLED")
    public Object cancelOrder(@PathVariable("orderId") String orderCode){
        System.out.println("orderCode" + orderCode);
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v1/checkout/orders/" + orderCode,
                HttpMethod.DELETE,
                entity,
                Object.class
        );
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            LOGGER.log(Level.INFO, "ORDER CANCELLED");
           Order order = orderService.cancelOrder(orderCode);
//            return  response.getBody();
            System.out.println("orderState" + order.getOrderStatus().getStatus());
            return new ResponseEntity<>(mapperOrder.mapTo(order), HttpStatus.OK);
        } else {
            LOGGER.log(Level.INFO, "FAILED CANCELLING ORDER");
            return "Unavailable to get CANCEL AN ORDER, STATUS CODE " + response.getStatusCode();
        }

    }


    @PostMapping("/orders/{captureId}/REFUNDED")
    public Object refundPayment(@PathVariable("captureId") String captureId){
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(captureId);
        HttpEntity<?> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/payments/captures/" + captureId + "/refund",
                HttpMethod.POST,
                entity,
                Object.class
        );
        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "REFUND CREATED");
            Order order = orderService.refundOrder(captureId);
//            return  response.getBody();
            return new ResponseEntity<>(mapperOrder.mapTo(order), HttpStatus.OK);
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING REFUND");
            return "Unavailable to get CREATE A REFUND, STATUS CODE " + response.getStatusCode();
        }


    }



}
