package com.motorcycleparts.motorcycleparts_master.controller;

import com.motorcycleparts.motorcycleparts_master.Dto.CustomerDto;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.model.provinces.Province;
import com.motorcycleparts.motorcycleparts_master.model.user.User;
import com.motorcycleparts.motorcycleparts_master.repository.CustomerRepository;
import com.motorcycleparts.motorcycleparts_master.repository.OrderRepository;
import com.motorcycleparts.motorcycleparts_master.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final ProvinceRepository provinceRepository;
    private final OrderRepository orderRepository;
    @GetMapping("/get-customers-by-id/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerRepository.findById(customerId).orElseThrow());
    }

    //update infor customer
    @PutMapping("/update-customer/{customerId}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long customerId,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "sex", required = false) String sex
    ) {
        try {
            Customer customer = customerRepository.findById(customerId).orElseThrow();
            if (name != null) {
                customer.setName(name);
            }
            if (phoneNumber != null) {
                customer.setPhoneNumber(phoneNumber);
            }
            if (sex != null) {
                customer.setSex(sex);
            }
            if (avatar != null && !avatar.isEmpty()) {
                String imagePath = saveImage(avatar);
                customer.setAvatar(imagePath);
            }
            return ResponseEntity.ok(customerRepository.save(customer));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    private String saveImage(MultipartFile image) throws IOException {
        String directory = "D:\\LuanVanTotNghiep\\Motorcycle_Parts_V0\\Picture\\Avatar\\";
        String originalFilename = image.getOriginalFilename();
        String imagePath = directory + originalFilename;
        Path path = Paths.get(imagePath);
        int count = 1;
        while (Files.exists(path)) {
            String fileNameWithoutExtension = FilenameUtils.removeExtension(originalFilename);
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String newFileName = fileNameWithoutExtension + "_" + count + "." + fileExtension;
            imagePath = directory + newFileName;
            path = Paths.get(imagePath);
            count++;
        }
        Files.write(path, image.getBytes());
        return imagePath;
    }

    //get shipping rate by province code for customer
    @GetMapping("/get-shipping-rate-by-province-code/{cid}")
    public ResponseEntity<?> getShippingRateByProvinceCode(@PathVariable("cid") String cid) {
        Customer customer = customerRepository.findById(Long.parseLong(cid)).orElse(null);
        if (customer == null) {
            return ResponseEntity.badRequest().body("Customer not found");
        }
        Optional<Address> addressOptional = customer.getAddresses().stream().filter(Address::isDefault).findFirst();
        if (addressOptional.isEmpty()) {
            return ResponseEntity.ok("Không tìm thấy địa chỉ mặc định");
        }
//        System.out.println("addressOptional: " + addressOptional.get());
        Address address = addressOptional.get();
        String provinceFullName = address.getProvince();
        Province province = provinceRepository.findProvinceByFullName(provinceFullName);
        System.out.println("province: " + province.getFullName());
//        Double shippingRate = province.getShippingRate();
        Double shippingRate = 20.0000;
        return ResponseEntity.ok(shippingRate);
    }



    @GetMapping("/get-customers-with-lock-status-and-registration-date")
    public ResponseEntity<List<Customer>> getCustomersWithLockStatusAndRegistrationDate() {
        List<Customer> customers = customerRepository.findByUser_IsLockedNotNullAndUser_CreationDateNotNull();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customer/{customerId}/lock-status")
    public ResponseEntity<Boolean> getLockStatus(@PathVariable Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        User user = customer.getUser();
        return ResponseEntity.ok(user.getIsLocked());
    }

    @GetMapping("/customer/{customerId}/orders/summary")
    public ResponseEntity<CustomerOrderSummary> getCustomerOrderSummary(@PathVariable Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        List<Order> orders = orderRepository.findByCustomer(customer);

        double totalAmount = orders.stream()
                .mapToDouble(Order::getAmountPrice)
                .sum();

        CustomerOrderSummary summary = new CustomerOrderSummary();
        summary.setTotalOrders(orders.size());
        summary.setTotalAmount(totalAmount);

        return ResponseEntity.ok(summary);
    }

    // seacrh customer by name with is exist status

    @GetMapping("/customers/search")
    public ResponseEntity<List<Customer>> searchCustomers(@RequestParam String name) {
        List<Customer> customers = customerRepository.findByNameContaining(name).stream()
                .filter(customer -> customer.getUser().getIsLocked() != null)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }
}
