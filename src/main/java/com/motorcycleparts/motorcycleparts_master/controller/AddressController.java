package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.model.Address;
import com.motorcycleparts.motorcycleparts_master.model.Customer;
import com.motorcycleparts.motorcycleparts_master.model.provinces.AddressRequest;
import com.motorcycleparts.motorcycleparts_master.model.provinces.District;
import com.motorcycleparts.motorcycleparts_master.model.provinces.Province;
import com.motorcycleparts.motorcycleparts_master.model.provinces.Ward;
import com.motorcycleparts.motorcycleparts_master.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class AddressController {

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    @GetMapping ("/get-all-provinces")
    public ResponseEntity<List<Province>> getAllProvinces(){
        return ResponseEntity.ok(provinceRepository.findAll());
    }

    @GetMapping("/get-districts-by-provinces-code/{code}")
    public  ResponseEntity<List<District>> getDistrictsByProvincesCode(@PathVariable String code) {
        return ResponseEntity.ok(districtRepository.findDistrictByProvinceCode(code));
    }

    @GetMapping("/get-wards-by-district-code/{code}")
    public ResponseEntity<List<Ward>> getWardsByDistrictCode(@PathVariable String code) {
        return ResponseEntity.ok(wardRepository.findWardByDistrictCode(code));
    }

    @GetMapping("/get-addresses-by-customer-id/{customerId}")
    public ResponseEntity<List<Address>> getAddressesByCustomerId(@PathVariable Long customerId) {
        System.out.println("customer id: "+customerId);
//        Optional<Customer> customerOptional = customerRepository.findById(customerId);
//        if (customerOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        Customer customer = customerOptional.get();
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        return ResponseEntity.ok(customer.getAddresses());
    }

    //get address default by customer id
    @GetMapping("/get-address-default-by-customer-id/{customerId}")
    public ResponseEntity<?> getAddressDefaultByCustomerId(@PathVariable Long customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Customer customer = customerOptional.get();
        Optional<Address> addressOptional = customer.getAddresses().stream().filter(Address::isDefault).findFirst();
        if (addressOptional.isEmpty()) {
                return ResponseEntity.ok("Default address not found");
        }
        return ResponseEntity.ok(addressOptional.get());
    }


    @PostMapping("/add-address/{isDefault}")
    @Transactional
    public ResponseEntity<?> addAddress(@PathVariable("isDefault") boolean isDefault, @RequestBody AddressRequest addressRequest) {
        System.out.println("address request: "+isDefault);
        Optional<Customer> customerOptional = customerRepository.findById(addressRequest.getCustomerId());
        if (customerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        Customer customer = customerOptional.get();
        List<Address> customerAddresses = customer.getAddresses();

        System.out.println("is default before: "+isDefault);

        // Nếu addressRequest.isDefault() là true, đặt tất cả các địa chỉ khác của khách hàng này thành false
        if (isDefault) {
            for (Address addr : customerAddresses) {
                addr.setDefault(false);
                addressRepository.save(addr);
            }
        }

        // Tìm tỉnh/thành phố, quận/huyện và phường/xã
        Optional<Province> provinceOptional = Optional.ofNullable(provinceRepository.findProvinceByCode(addressRequest.getProvinceCode()));
        Optional<District> districtOptional = Optional.ofNullable(districtRepository.findDistrictByCode(addressRequest.getDistrictCode()));
        Optional<Ward> wardOptional = Optional.ofNullable(wardRepository.findWardByCode(addressRequest.getWardCode()));

        if (provinceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Province not found");
        }
        if (districtOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("District not found");
        }
        if (wardOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ward not found");
        }


//        System.out.println("is default after: "+addressRequest.isDefault());

        // Tạo địa chỉ mới
        Address address = new Address();
        address.setCustomer(customer);
        address.setProvince(provinceOptional.get().getFullName());
        address.setDistrict(districtOptional.get().getFullName());
        address.setWard(wardOptional.get().getFullName());
        address.setFullName(addressRequest.getFullName());
        address.setPhoneNumBer(addressRequest.getPhoneNumBer());
        address.setAddress(addressRequest.getAddressDetail());
        address.setDefault(isDefault);

        Address savedAddress = addressRepository.save(address);

        customerAddresses.add(address);
        customerRepository.save(customer);

        // Lưu địa chỉ vào cơ sở dữ liệu

        return ResponseEntity.ok(savedAddress); // Trả về địa chỉ đã lưu thành công
    }


    @PutMapping("/update-address/{id}")
    @Transactional
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody AddressRequest addressRequest) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        if (addressOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found");
        }

        Address address = addressOptional.get();

        // Tìm tỉnh/thành phố, quận/huyện và phường/xã
        Optional<Province> provinceOptional = Optional.ofNullable(provinceRepository.findProvinceByCode(addressRequest.getProvinceCode()));
        Optional<District> districtOptional = Optional.ofNullable(districtRepository.findDistrictByCode(addressRequest.getDistrictCode()));
        Optional<Ward> wardOptional = Optional.ofNullable(wardRepository.findWardByCode(addressRequest.getWardCode()));

        if (provinceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Province not found");
        }
        if (districtOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("District not found");
        }
        if (wardOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ward not found");
        }

        // Cập nhật thông tin địa chỉ
        address.setProvince(provinceOptional.get().getFullName());
        address.setDistrict(districtOptional.get().getFullName());
        address.setWard(wardOptional.get().getFullName());
        address.setFullName(addressRequest.getFullName());
        address.setPhoneNumBer(addressRequest.getPhoneNumBer());
//        address.setDefault(addressRequest.isDefault());
        address.setAddress(addressRequest.getAddressDetail());
//        System.out.println("address detail: "+addressRequest.getAddressDetail());

        // Lưu địa chỉ vào cơ sở dữ liệu
        Address updatedAddress = addressRepository.save(address);

        return ResponseEntity.ok(updatedAddress); // Trả về địa chỉ đã được cập nhật thành công
    }

    @DeleteMapping("/delete-address/{cid}/{adressid}")
    @Transactional
    public ResponseEntity<?> deleteAddress(@PathVariable Long cid, @PathVariable Long adressid) {
        Optional<Customer> customerOptional = customerRepository.findById(cid);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
        Optional<Address> addressOptional = addressRepository.findById(adressid);
        if (addressOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found");
        }

        Address address = addressOptional.get();
        Customer customer = customerOptional.get();

        // Remove the address from the customer's list of addresses
        customer.getAddresses().remove(address);
        customerRepository.save(customer);

        // Now delete the address
        addressRepository.delete(address);

        return ResponseEntity.ok("Address deleted successfully");
    }

    // Request set default address
    @PutMapping("/set-default-address/{cid}/{addressId}")
    @Transactional
    public ResponseEntity<?> setDefaultAddress(@PathVariable Long cid, @PathVariable Long addressId) {
        Optional<Customer> customerOptional = customerRepository.findById(cid);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        if (addressOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found");
        }

        Customer customer = customerOptional.get();
        Address address = addressOptional.get();

        // Set all other addresses of the customer to false
        for (Address addr : customer.getAddresses()) {
            addr.setDefault(false);
            addressRepository.save(addr);
        }

        // Set the selected address to true
        address.setDefault(true);
        addressRepository.save(address);

        return ResponseEntity.ok(address);
    }




}
