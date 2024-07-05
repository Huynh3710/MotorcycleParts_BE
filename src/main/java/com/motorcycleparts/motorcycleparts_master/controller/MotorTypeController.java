package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.model.BrandMotor;
import com.motorcycleparts.motorcycleparts_master.model.BrandParts;
import com.motorcycleparts.motorcycleparts_master.model.MotorType;
import com.motorcycleparts.motorcycleparts_master.repository.BrandMotorRepository;
import com.motorcycleparts.motorcycleparts_master.repository.MotorTypeRepository;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class MotorTypeController {
    private final MotorTypeRepository motorTypeRepository;
    private final BrandMotorRepository brandMotorRepository;
    @GetMapping("/get-all-motor-type")
    public ResponseEntity<List<MotorType>> getAllMotorType() {
        return ResponseEntity.ok(motorTypeRepository.findAll());
    }

    @GetMapping("/get-motor-type-by-brand-motor-id/{id}")
    public ResponseEntity<?> getMotorTypeById(@PathVariable("id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("ID cannot be null");
        }
        List<MotorType> motorType = motorTypeRepository.findByBrandMotor_Id(id);
        if (motorType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(motorType);
    }

    @PostMapping("/add-motor-type")
    public ResponseEntity<?> addMotorType(@RequestParam("name") String name, @RequestParam("image") MultipartFile image) {
        try {
            // Kiểm tra xem brandParts đã tồn tại hay chưa
            if (motorTypeRepository.existsByName(name)) {
                return ResponseEntity.badRequest().body("loại xe đã tồn tại.");
            }

            // Lưu ảnh vào thư mục và nhận đường dẫn của ảnh
            String imagePath = saveImage(image);

            // Tạo đối tượng BrandParts và lưu vào cơ sở dữ liệu
            MotorType motorType = MotorType.builder()
                    .name(name)
                    .image(imagePath)
                    .build();

            MotorType saveMotorType = motorTypeRepository.save(motorType);

            // Trả về đối tượng BrandParts đã được lưu vào cơ sở dữ liệu
            return ResponseEntity.ok(saveMotorType);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update-brand-parts/{motorTypeId}")
    public ResponseEntity<?> updateMotorType(@PathVariable Long motorTypeId,
                                              @RequestParam("name") String name,
                                              @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            // Kiểm tra xem brandParts có tồn tại không
            Optional<MotorType> motorTypeOptional = motorTypeRepository.findById(motorTypeId);
            if (motorTypeOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            MotorType motorType = motorTypeOptional.get();

            // Kiểm tra và cập nhật tên
            if (!name.isEmpty()) {
                motorType.setName(name);
            }

            // Kiểm tra và cập nhật ảnh
            if (image != null) {
                String imagePath = saveImage(image);
                motorType.setImage(imagePath);
            }

            // Lưu thay đổi vào cơ sở dữ liệu
            MotorType updatedMotorType = motorTypeRepository.save(motorType);

            return ResponseEntity.ok(updatedMotorType);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Phương thức lưu ảnh vào thư mục trên máy và trả về đường dẫn của ảnh
    private String saveImage(MultipartFile image) throws IOException {
        // Thư mục lưu trữ ảnh
        String directory = "D:\\LuanVanTotNghiep\\Motorcycle_Parts_V0\\Picture\\MotorType\\";

        // Tên gốc của file ảnh
        String originalFilename = image.getOriginalFilename();

        // Tạo đường dẫn đầy đủ của file ảnh
        String imagePath = directory + originalFilename;
        Path path = Paths.get(imagePath);

        // Kiểm tra xem file ảnh đã tồn tại chưa, nếu tồn tại, thêm số vào tên file để tránh trùng lặp
        int count = 1;
        while (Files.exists(path)) {
            String fileNameWithoutExtension = FilenameUtils.removeExtension(originalFilename);
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String newFileName = fileNameWithoutExtension + "_" + count + "." + fileExtension;
            imagePath = directory + newFileName;
            path = Paths.get(imagePath);
            count++;
        }

        // Lưu ảnh vào thư mục
        Files.write(path, image.getBytes());

        // Trả về đường dẫn của ảnh
        return imagePath;
    }

    @GetMapping("/get-image-by-motor-type/{motorTypeId}")
    public ResponseEntity<byte[]> getImageByIdMotorType(@PathVariable Long motorTypeId) {
        Optional<MotorType> motorTypeOptional = motorTypeRepository.findById(motorTypeId);
        if (motorTypeOptional.isPresent()) {
            MotorType motorType = motorTypeOptional.get();
            String imagePath = motorType.getImage(); // Giả sử đường dẫn hình ảnh được lưu trong entity Category

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

    // search motor type by name
//    @GetMapping("/search-motor-type-by-name/{keyword}")
//    public ResponseEntity<List<MotorType>> searchMotorTypeByName(@PathVariable("keyword") String keyword) {
//       List<MotorType> motorTypes;
//        System.out.println("keyword: " + keyword);
//        if (keyword == null || keyword.isEmpty()) {
//            motorTypes = motorTypeRepository.findAll();
//        } else {
//            motorTypes = motorTypeRepository.findMotorTypeByNameContaining(keyword);
//        }
//        return ResponseEntity.ok(motorTypes);
//    }
    @GetMapping("/search-motor-type-by-name")
    public ResponseEntity<List<MotorType>> searchMotorTypeByName(@RequestParam(value = "keyword", required = false) String keyword) {
        List<MotorType> motorTypes;
        System.out.println("keyword: " + keyword);
        if (keyword == null || keyword.isEmpty()) {
            motorTypes = motorTypeRepository.findAll();
        } else {
            motorTypes = motorTypeRepository.findMotorTypeByNameContaining(keyword);
        }
        return ResponseEntity.ok(motorTypes);
    }


}
