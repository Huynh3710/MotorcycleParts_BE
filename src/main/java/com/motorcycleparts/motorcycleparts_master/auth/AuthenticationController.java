package com.motorcycleparts.motorcycleparts_master.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcycleparts.motorcycleparts_master.model.Customer;
import com.motorcycleparts.motorcycleparts_master.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController

//Chưa có tk gọi api/v1/authentication
//@RequestMapping("api/v1/authentication")
@RequestMapping("/get-api")
//@CrossOrigin ("http://localhost:3000")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final CustomerRepository customerRepository;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
//            @RequestBody RegisterRequest registerRequest
            @RequestParam("user") String userJson,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) throws IOException {
        System.out.println("avatar: " + avatar);
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest registerRequest = objectMapper.readValue(userJson, RegisterRequest.class);
        if (avatar != null) {
            String avatarPath = saveImage(avatar);
            registerRequest.setAvatar(avatarPath);
        }
        return  ResponseEntity.ok(authenticationService.register(registerRequest));
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


    @GetMapping("/get-image-by-customer/{customerId}")
    public ResponseEntity<byte[]> getImageByCustomerId(@PathVariable Long customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            String imagePath = customer.getAvatar(); // Giả sử đường dẫn hình ảnh được lưu trong entity Customer

            try {
                Path path = Paths.get(imagePath);
                byte[] imageBytes = Files.readAllBytes(path);

                // Thiết lập kiểu nội dung phù hợp dựa trên loại hình ảnh (ví dụ, JPEG, PNG, v.v.)
                MediaType contentType = MediaType.IMAGE_JPEG; // Điều chỉnh dựa trên loại hình ảnh của bạn

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(contentType);

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(imageBytes.length)
                        .body(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    //login
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ){
        return  ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));

    }
    @GetMapping("/get-customer-name-by-id/{id}")
    public ResponseEntity<String> getCustomerNameById(
            @PathVariable(value = "id") Long id
    ){
        Optional<Customer> cus = customerRepository.findById(id);
        Customer customer = cus.get();
        return ResponseEntity.ok(customer.getName());
    }


    @PostMapping("/refresh-token")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
