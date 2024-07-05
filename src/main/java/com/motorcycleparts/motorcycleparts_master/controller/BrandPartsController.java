package com.motorcycleparts.motorcycleparts_master.controller;

import com.motorcycleparts.motorcycleparts_master.Dto.BrandPartsDto;
import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.model.BrandParts;
import com.motorcycleparts.motorcycleparts_master.repository.BrandPartsRepository;
import com.motorcycleparts.motorcycleparts_master.service.BrandPartsService;
import com.motorcycleparts.motorcycleparts_master.service.SparePartsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/get-api/brand-parts")
//@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class BrandPartsController {
    private final SparePartsService sparePartsService;
    private final BrandPartsService brandPartsService;
    private final BrandPartsRepository brandPartsRepository;
    @GetMapping("/get-by-brand-id={id}")
    public ResponseEntity<List<SparePartsDto>> getByBrand(@PathVariable Long id){
        return ResponseEntity.ok(sparePartsService.getSparePartsByBrandId(id));
    }

    @GetMapping("/get-all-brand-parts")
    public ResponseEntity<List<BrandParts>> getAllBrandParts (){
        return ResponseEntity.ok(brandPartsService.getAllBrandParts());
    }

    @PostMapping("/add-brands-part")
    public ResponseEntity<?> addBrandsPart(@RequestParam("name") String name, @RequestParam("image") MultipartFile image) {
        try {
            // Kiểm tra xem brandParts đã tồn tại hay chưa
            if (brandPartsRepository.existsByName(name)) {
                return ResponseEntity.badRequest().body("BrandParts đã tồn tại.");
            }

            // Lưu ảnh vào thư mục và nhận đường dẫn của ảnh
            String imagePath = saveImage(image);

            // Tạo đối tượng BrandParts và lưu vào cơ sở dữ liệu
            BrandParts brandParts = BrandParts.builder()
                    .name(name)
                    .image(imagePath)
                    .build();

            BrandParts savedBrandParts = brandPartsRepository.save(brandParts);

            // Trả về đối tượng BrandParts đã được lưu vào cơ sở dữ liệu
            return ResponseEntity.ok(savedBrandParts);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/update-brand-parts/{brandPartsId}")
    public ResponseEntity<?> updateBrandParts(@PathVariable Long brandPartsId,
                                              @RequestParam("name") String name,
                                              @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            // Kiểm tra xem brandParts có tồn tại không
            Optional<BrandParts> brandPartsOptional = brandPartsRepository.findById(brandPartsId);
            if (brandPartsOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            BrandParts brandParts = brandPartsOptional.get();

            // Kiểm tra và cập nhật tên
            if (!name.isEmpty()) {
                brandParts.setName(name);
            }

            // Kiểm tra và cập nhật ảnh
            if (image != null) {
                String imagePath = saveImage(image);
                brandParts.setImage(imagePath);
            }

            // Lưu thay đổi vào cơ sở dữ liệu
            BrandParts updatedBrandParts = brandPartsRepository.save(brandParts);

            return ResponseEntity.ok(updatedBrandParts);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    // Phương thức lưu ảnh vào thư mục trên máy và trả về đường dẫn của ảnh
    private String saveImage(MultipartFile image) throws IOException {
        // Thư mục lưu trữ ảnh
        String directory = "D:\\LuanVanTotNghiep\\Motorcycle_Parts_V0\\Picture\\BrandParts\\";

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



    @GetMapping("/get-image-by-category/{categoryId}")
    public ResponseEntity<byte[]> getImageByIdCategory(@PathVariable Long categoryId) {
        Optional<BrandParts> categoryOptional = brandPartsRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            BrandParts category = categoryOptional.get();
            String imagePath = category.getImage(); // Giả sử đường dẫn hình ảnh được lưu trong entity Category

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

    //search brand parts by name
//    @GetMapping("/search-brand-parts-by-name/{name}")
//    public ResponseEntity<List<BrandParts>> searchBrandPartsByName(@PathVariable("name") String name) {
//        List<BrandParts> brandParts;
//        if (name == null || name.isEmpty()) {
//            brandParts = brandPartsRepository.findAll();
//        } else {
//            brandParts = brandPartsRepository.findBrandPartsByNameContaining(name);
//        }
//        return ResponseEntity.ok(brandParts);
//    }
    @GetMapping("/search-brand-parts-by-name")
    public ResponseEntity<List<BrandParts>> searchBrandPartsByName(@RequestParam(value = "name", required = false) String name) {
        List<BrandParts> brandParts;

        if (name == null || name.isEmpty()) {
            brandParts = brandPartsRepository.findAll();
        } else {
            brandParts = brandPartsRepository.findBrandPartsByNameContaining(name);
        }
        return ResponseEntity.ok(brandParts);
    }


}
