package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperSparePartsImpl;
import com.motorcycleparts.motorcycleparts_master.model.MotorType;
import com.motorcycleparts.motorcycleparts_master.model.Parts_MotorType;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import com.motorcycleparts.motorcycleparts_master.model.SparePartsType;
import com.motorcycleparts.motorcycleparts_master.repository.Parts_MotorTypeRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsTypeRepository;
import com.motorcycleparts.motorcycleparts_master.service.SparePartsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/get-api/spare-parts")
//@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor

public class SparePartsController {

    @Autowired
    private final MapperSparePartsImpl mapperSpareParts;
    @Autowired
    private final SparePartsService sparePartsService;
    @Autowired
    private final SparePartsRepository sparePartsRepository;
    @Autowired
    private final Parts_MotorTypeRepository parts_motorTypeRepository;

    private final SparePartsTypeRepository sparePartsTypeRepository;


    @PutMapping("/add-product")
    public ResponseEntity<SpareParts> addProduct (@RequestBody SpareParts spareParts){
        SpareParts newSpareParts = SpareParts.builder()
                                                .name(spareParts.getName())
                                                .unitPrice(spareParts.getUnitPrice())
                                                .build();
        return ResponseEntity.ok(newSpareParts);
    }

    @GetMapping("/get-all-parts")
    public ResponseEntity<List<SparePartsDto>> getAllP(){
//        return new ResponseEntity<>(sparePartsService.getAllParts(), HttpStatus.OK);
        return ResponseEntity.ok(sparePartsService.getAllParts());
    }

    @GetMapping("/get-spare-parts-by-id={id}")
    public ResponseEntity<SparePartsDto> getPId(@PathVariable Long id){
        System.out.println("id la: "+id);
        return ResponseEntity.ok(sparePartsService.getSparePartsById(id));
    }


    @GetMapping("/get-api/spare-parts/get-all-parts-pages")
    public ResponseEntity<Page<SparePartsDto>> getAllPartsPages(Pageable page){
        return ResponseEntity.ok(sparePartsService.getAllPartsPages(page));
    }

    @GetMapping("/get-spare-parts-by-moto-type-id={id}")
    public ResponseEntity<List<SparePartsDto>> getBrandId(@PathVariable Long id){
        List<SpareParts> spareParts = parts_motorTypeRepository.findAllSparePartsByMotorTypeId(id);
        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(java.util.stream.Collectors.toList()));
    }

    @GetMapping("/get-motor-types-by-spare-parts-id={id}")
    public ResponseEntity<List<MotorType>> getMotorTypesBySparePartsId(@PathVariable Long id){
        List<MotorType> motorTypes = parts_motorTypeRepository.findAllMotorTypesBySparePartsId(id);
        return ResponseEntity.ok(motorTypes);
    }

//    getSpareParts By SparePartTypeId
//    @GetMapping("/get-spare-parts-by-spare-parts-type-id={id}")
//    public ResponseEntity<List<SparePartsDto>> getSparePartsBySparePartsTypeId(@PathVariable Long id){
//        List<SpareParts> spareParts = sparePartsRepository.findAllBySparePartsTypeId(id);
//        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(java.util.stream.Collectors.toList()));
//    }


    @GetMapping("/get-spare-parts-by-spare-parts-type-id={id}/exclude={excludeId}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsBySparePartsTypeId(@PathVariable Long id, @PathVariable Long excludeId){
        List<SpareParts> spareParts = sparePartsRepository.findAllBySparePartsTypeId(id);
        List<SpareParts> filteredSpareParts = spareParts.stream()
                .filter(sparePart -> !sparePart.getId().equals(excludeId))
                .toList();
        return ResponseEntity.ok(filteredSpareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }


    @GetMapping("/get-all-spare-parts-type")
    public ResponseEntity<List<SparePartsType>> getAllSparePartsType(){
        return ResponseEntity.ok(sparePartsTypeRepository.findAll());
    }

    @GetMapping("/get-spare-parts-type-by-id={sparePartsTypeId}")
    public ResponseEntity<SparePartsType> getSparePartsTypeById(@PathVariable Long sparePartsTypeId) {
        // Kiểm tra xem sparePartsType có tồn tại không
        Optional<SparePartsType> sparePartsTypeOptional = sparePartsTypeRepository.findById(sparePartsTypeId);
        if (sparePartsTypeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sparePartsTypeOptional.get());
    }


    @PostMapping("/add-spare-parts-type")
    public ResponseEntity<?> addSparePartsType(@RequestParam("name") String name, @RequestParam("image") MultipartFile image) {
        try {
            // Kiểm tra xem sparePartsType đã tồn tại hay chưa
            if (sparePartsTypeRepository.existsByName(name)) {
                return ResponseEntity.badRequest().body("SparePartsType đã tồn tại.");
            }

            // Lưu ảnh vào thư mục và nhận đường dẫn của ảnh
            String imagePath = saveImage(image);

            // Tạo đối tượng SparePartsType và lưu vào cơ sở dữ liệu
            SparePartsType sparePartsType = SparePartsType.builder()
                    .name(name)
                    .image(imagePath)
                    .build();

            SparePartsType savedSparePartsType = sparePartsTypeRepository.save(sparePartsType);
            // Trả về đối tượng SparePartsType đã được lưu vào cơ sở dữ liệu
            return ResponseEntity.ok(savedSparePartsType);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update-spare-parts-type/{sparePartsTypeId}")
    public ResponseEntity<?> updateSparePartsType(@PathVariable Long sparePartsTypeId,
                                          @RequestParam("name") String name,
                                          @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            // Kiểm tra xem sparePartsType có tồn tại không
            Optional<SparePartsType> sparePartsTypeOptional = sparePartsTypeRepository.findById(sparePartsTypeId);
            if (sparePartsTypeOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            SparePartsType sparePartsType = sparePartsTypeOptional.get();

            // Kiểm tra và cập nhật tên
            if (!name.isEmpty()) {
                sparePartsType.setName(name);
            }

            // Kiểm tra và cập nhật ảnh
            if (image != null) {
                String imagePath = saveImage(image);
                sparePartsType.setImage(imagePath);
            }

            // Lưu thông tin cập nhật vào cơ sở dữ liệu
            SparePartsType updatedSparePartsType = sparePartsTypeRepository.save(sparePartsType);

            // Trả về thông tin của sparePartsType sau khi cập nhật
            return ResponseEntity.ok(updatedSparePartsType);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


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
    @GetMapping("/get-image-by-spare-parts-type-id/{sparePartsTypeId}")
    public ResponseEntity<byte[]> getImageBySparePartsTypeId(@PathVariable Long sparePartsTypeId) {
        // Kiểm tra xem sparePartsType có tồn tại không
        Optional<SparePartsType> sparePartsTypeOptional = sparePartsTypeRepository.findById(sparePartsTypeId);
        if (sparePartsTypeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SparePartsType sparePartsType = sparePartsTypeOptional.get();
        String imagePath = sparePartsType.getImage();

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
    }


    @GetMapping("/get-spare-parts-by-search={search}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsBySearch(@PathVariable String search){
        List<SpareParts> spareParts = sparePartsRepository.findSparePartsByNameContaining(search);
        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }

    @GetMapping("/get-spare-parts-by-filter")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByFilter(
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "typeId", required = false) Long typeId,
            @RequestParam(value = "motorTypeId", required = false) Long motorTypeId,
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice) {

        List<SpareParts> spareParts = sparePartsRepository.findAll();

        if (brandId != null && !brandId.equals("")) {
            spareParts = spareParts.stream()
                    .filter(sparePart -> sparePart.getBrandParts().getId().equals(brandId))
                    .collect(Collectors.toList());
        }

        if (typeId != null && !typeId.equals("")) {
            spareParts = spareParts.stream()
                    .filter(sparePart -> sparePart.getSparePartsType().getId().equals(typeId))
                    .collect(Collectors.toList());
        }

        if (motorTypeId != null && !motorTypeId.equals("")) {
            spareParts = spareParts.stream()
                    .filter(sparePart -> parts_motorTypeRepository.findAllMotorTypesBySparePartsId(sparePart.getId())
                            .stream()
                            .anyMatch(motor -> motor.getId().equals(motorTypeId)))
                    .collect(Collectors.toList());
        }

        if (minPrice != null) {
            spareParts = spareParts.stream()
                    .filter(sparePart -> sparePart.getUnitPrice() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (maxPrice != null) {
            spareParts = spareParts.stream()
                    .filter(sparePart -> sparePart.getUnitPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }


    //get spare parts by brand id
    @GetMapping("/get-spare-parts-by-brand-id={id}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByBrandId(@PathVariable Long id){
        List<SpareParts> spareParts = sparePartsRepository.findSparePartsByBrandPartsId(id);
        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }

    //get spare parts by type id
    @GetMapping("/get-spare-parts-by-type-id={id}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByTypeId(@PathVariable Long id){
        List<SpareParts> spareParts = sparePartsRepository.findSparePartsBySparePartsTypeId(id);
        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }

    //get spare parts by motor type id
    @GetMapping("/get-spare-parts-by-motor-type-id={id}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByMotorTypeId(@PathVariable Long id){
        List<SpareParts> spareParts = parts_motorTypeRepository.findAllSparePartsByMotorTypeId(id);
        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }

    @GetMapping("/get-spare-parts-by-name")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByName(@RequestParam(value = "name", required = false) String name){
        List<SpareParts> spareParts;
        System.out.println("name: "+name);
        if (name == null || name.isEmpty()) {
            spareParts = sparePartsRepository.findAll();
        } else {
            spareParts = sparePartsRepository.findSparePartsByNameContaining(name);
        }
        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }


//...

    @GetMapping("/get-spare-parts-by-ids")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByIds(@RequestParam(value = "ids", required = false) List<Long> ids){
        List<SpareParts> spareParts;
        System.out.println("ids: "+ids);
        if (ids == null || ids.isEmpty()) {
            spareParts = sparePartsRepository.findAll();
            Collections.shuffle(spareParts);  // Xáo trộn vị trí các phần tử
        } else {
            spareParts = sparePartsRepository.findAllById(ids);
        }
        return ResponseEntity.ok(spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }





}
