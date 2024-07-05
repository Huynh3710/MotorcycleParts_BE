package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperSparePartsImpl;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.repository.*;
import com.motorcycleparts.motorcycleparts_master.service.BrandPartsService;
import com.motorcycleparts.motorcycleparts_master.service.SparePartsService;
import com.motorcycleparts.motorcycleparts_master.service.SparePartsTypeService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
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
@RequestMapping("/get-api/spares-parts-type")
//@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class SparePartsTypeController {
    private final SparePartsService sparePartsService;
    private final SparePartsTypeService sparePartsTypeService;
    private final SparePartsTypeRepository sparePartsTypeRepository;
    private final BrandPartsRepository brandPartsRepository;
    private final SparePartsRepository sparePartsRepository;
    private final BrandMotorRepository brandMotorRepository;
    private final MapperSparePartsImpl mapperSpareParts;
    private final MotorTypeRepository motorTypeRepository;
    private final Parts_MotorTypeRepository parts_motorTypeRepository;
    @GetMapping("/get-all-spare-parts-type")
    public ResponseEntity<List<SparePartsType>> getAllSparePartsType(){
        return ResponseEntity.ok(sparePartsTypeService.getAllSparePartsType());
    }

    @GetMapping("/get-spare-parts-type-by-id={id}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByTypeId(@PathVariable Long id){
        return ResponseEntity.ok(sparePartsService.getSparePartsByTypeId(id));
    }

//    @GetMapping("/get-spare-parts-by-type-and-status/{typeId}/{status}")
//    public ResponseEntity<List<SparePartsDto>> getSparePartsByTypeAndStatus(@PathVariable Long typeId, @PathVariable String status){
//        return ResponseEntity.ok(sparePartsRepository.findSparePartsBySparePartsType_IdAndStatus(typeId, status)
//                .stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
//    }

    @GetMapping("/get-spare-parts-by-type-and-status/{typeId}/{status}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByTypeAndStatus(@PathVariable Long typeId, @PathVariable String status) {
        List<SpareParts> sparePartsList = sparePartsRepository.findSparePartsBySparePartsType_IdAndStatus(typeId, status);
        if (sparePartsList == null || sparePartsList.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // Trả về danh sách rỗng nếu không tìm thấy kết quả
        }
        return ResponseEntity.ok(sparePartsList.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }

    @GetMapping("/get-spare-parts-by-status/{status}")
    public ResponseEntity<List<SparePartsDto>> getSparePartsByBrandId(@PathVariable String status){
        return ResponseEntity.ok(sparePartsRepository.findSparePartsByStatus(status)
                .stream().map(mapperSpareParts::mapTo).collect(Collectors.toList()));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSparePart(@RequestParam("name") String name,
                                             @RequestParam("description") String description,
                                             @RequestParam("unitPrice") String unitPrice,
                                             @RequestParam("weight") String weight,
                                             @RequestParam("size") String size,
                                             @RequestParam(value = "wattage", required = false) String wattage,
                                             @RequestParam(value = "voltage", required = false) String voltage,
                                             @RequestParam("year") String year,
                                             @RequestParam("origin") String origin,
                                             @RequestParam("inventory") String inventory,
                                             @RequestParam("status") String status,
                                             @RequestParam("carNames") String carNamesString,
                                             @RequestParam("sparePartsTypeId") String sparePartsTypeId,
                                             @RequestParam("brandPartsId") String brandPartsId,
                                             @RequestParam("carBrandId") String carBrandId,
                                             @RequestParam("image") MultipartFile image) throws IOException {

        try {

            System.out.println("Status: " + status);
            // Kiểm tra nếu wattage và voltage được gửi từ client, nếu không, đặt giá trị mặc định là null
            Float wattageValue = wattage != null && !wattage.isEmpty() ? Float.parseFloat(wattage) : null;
            Float voltageValue = voltage != null && !voltage.isEmpty() ? Float.parseFloat(voltage) : null;

            // Chuyển đổi các giá trị khác sang kiểu dữ liệu phù hợp
            float unitPriceValue = Float.parseFloat(unitPrice);
            float weightValue = Float.parseFloat(weight);
            int yearValue = Integer.parseInt(year);
            int inventoryValue = Integer.parseInt(inventory);


//            BrandMotor brandMotorOpt = brandMotorRepository.findById(Long.valueOf(carBrandId)).orElseThrow();
            BrandMotor brandMotorOpt = null;
            try {
                Long brandMotorId = Long.parseLong(carBrandId);
                brandMotorOpt = brandMotorRepository.findById(brandMotorId).orElseThrow();
            } catch (NumberFormatException e) {
                BrandMotor brandMotor = new BrandMotor();
                brandMotor.setName(carBrandId);
                brandMotorOpt = brandMotorRepository.save(brandMotor);
            }

            SparePartsType sparePartsTypeOpt = null;
            try {
                Long sparePartsTypeIdLong = Long.parseLong(sparePartsTypeId);
                sparePartsTypeOpt = sparePartsTypeRepository.findById(sparePartsTypeIdLong).orElseThrow();
            } catch (NumberFormatException e) {
                SparePartsType newSparePartsType = new SparePartsType();
                newSparePartsType.setName(sparePartsTypeId);
                sparePartsTypeOpt = sparePartsTypeRepository.save(newSparePartsType);
            }



            // Lấy BrandParts từ repository
            BrandParts brandPartsOpt = brandPartsRepository.findById(Long.valueOf(brandPartsId)).orElseThrow();

            // Lấy BrandMotor từ repository


            // Lưu hình ảnh và các thông tin vào cơ sở dữ liệu
            String imagePath = saveImage(image);
            SpareParts sparePart = SpareParts.builder()
                    .name(name)
                    .description(description)
                    .unitPrice(unitPriceValue)
                    .weight(weightValue)
                    .size(size)
                    .wattage(wattageValue)
                    .voltage(voltageValue)
                    .year(yearValue)
                    .origin(origin)
                    .inventory(inventoryValue)
                    .status(status)
                    .brandMotor(brandMotorOpt.getName())
                    .sparePartsType(sparePartsTypeOpt)
                    .brandParts(brandPartsOpt)
                    .image(imagePath)
                    .build();
            System.out.println("sparePart Status: " + sparePart.getStatus());
            sparePartsRepository.save(sparePart);

            // Lưu thông tin về các loại xe và linh kiện vào cơ sở dữ liệu
            String[] carNames = carNamesString.split("\\s*,\\s*");
            for (String carName : carNames) {
                MotorType motorType = motorTypeRepository.findByName(carName);
                if (motorType == null) {
                    motorType = MotorType.builder().name(carName).brandMotor(brandMotorOpt).build();
                    motorTypeRepository.save(motorType);
                }else{
                    motorType.setBrandMotor(brandMotorOpt);
                }
                Parts_MotorType parts_motorType = Parts_MotorType.builder()
                        .motorType(motorType)
                        .spareParts(sparePart)
                        .build();
                parts_motorTypeRepository.save(parts_motorType);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(mapperSpareParts.mapTo(sparePart));

        } catch (NumberFormatException | IOException e) {
            return ResponseEntity.badRequest().body("Invalid number format or IO exception: " + e.getMessage());
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSparePart(@PathVariable("id") Long id,
                                             @RequestParam("name") String name,
                                             @RequestParam("description") String description,
                                             @RequestParam("unitPrice") String unitPrice,
                                             @RequestParam("weight") String weight,
                                             @RequestParam("size") String size,
                                             @RequestParam(value = "wattage", required = false) String wattage,
                                             @RequestParam(value = "voltage", required = false) String voltage,
                                             @RequestParam("year") String year,
                                             @RequestParam("origin") String origin,
                                             @RequestParam("inventory") String inventory,
                                             @RequestParam("status") String status,
                                             @RequestParam("carNames") String carNamesString,
                                             @RequestParam("sparePartsTypeId") String sparePartsTypeId,
                                             @RequestParam("brandPartsId") String brandPartsId,
                                             @RequestParam("carBrandId") String carBrandId,
                                             @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        try {

            System.out.println("Status: " + status);

            Float wattageValue = wattage != null && !wattage.isEmpty() ? Float.parseFloat(wattage) : null;
            Float voltageValue = voltage != null && !voltage.isEmpty() ? Float.parseFloat(voltage) : null;

            // Kiểm tra xem sản phẩm có tồn tại không
            Optional<SpareParts> existingSparePartOpt = sparePartsRepository.findById(id);
            if (!existingSparePartOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm không tồn tại: " + id);
            }

            SpareParts existingSparePart = existingSparePartOpt.get();

            // Cập nhật thông tin sản phẩm từ request
            existingSparePart.setName(name);
            existingSparePart.setDescription(description);
            existingSparePart.setUnitPrice(Float.parseFloat(unitPrice));
            existingSparePart.setWeight(Float.parseFloat(weight));
            existingSparePart.setSize(size);
            existingSparePart.setWattage(wattageValue);
            existingSparePart.setVoltage(voltageValue);
            existingSparePart.setYear(Integer.parseInt(year));
            existingSparePart.setOrigin(origin);
            existingSparePart.setInventory(Integer.parseInt(inventory));
            existingSparePart.setStatus(status);

            // Lưu ảnh mới nếu có
            if (image != null) {
                String imagePath = saveImage(image);
                existingSparePart.setImage(imagePath);
            }

            // Lưu thông tin cập nhật vào cơ sở dữ liệu
            SparePartsType sparePartsTypeOpt = null;
            try {
                sparePartsTypeOpt = sparePartsTypeRepository.findById(Long.valueOf(sparePartsTypeId)).orElseThrow();
            } catch (NumberFormatException e) {
                SparePartsType newSparePartsType = new SparePartsType();
                newSparePartsType.setName(sparePartsTypeId);
                sparePartsTypeOpt = newSparePartsType;
                sparePartsTypeRepository.save(newSparePartsType);
            }
            BrandParts brandPartsOpt = brandPartsRepository.findById(Long.valueOf(brandPartsId)).orElseThrow();
//            BrandMotor brandMotorOpt = brandMotorRepository.findById(Long.valueOf(carBrandId)).orElseThrow();
            BrandMotor brandMotorOpt = null;
            try {
                brandMotorOpt = brandMotorRepository.findById(Long.valueOf(carBrandId)).orElseThrow();
            } catch (NumberFormatException e) {
                BrandMotor brandMotor = new BrandMotor();
                brandMotor.setName(carBrandId);
                brandMotorOpt = brandMotorRepository.save(brandMotor);
            }
            existingSparePart.setSparePartsType(sparePartsTypeOpt);
            existingSparePart.setBrandParts(brandPartsOpt);
            existingSparePart.setBrandMotor(brandMotorOpt.getName());

            sparePartsRepository.save(existingSparePart);

            // Xử lý các loại xe liên quan

            String[] carNames = carNamesString.split("\\s*,\\s*");
            System.out.println("carNamesString: " + carNamesString);
            List<MotorType> motorTypes = parts_motorTypeRepository.findAllMotorTypesBySparePartsId(id);
            for (MotorType motorType : motorTypes) {
                List<Parts_MotorType> partsMotorTypes = motorType.getPartMotorTypes();
                parts_motorTypeRepository.deleteAll(partsMotorTypes);
            }

            for (String carName : carNames) {
                System.out.println("carName: " + carName);
                MotorType motorType = motorTypeRepository.findByName(carName);
                if (motorType == null) {
                    // Nếu motorType chưa tồn tại, tạo mới và lưu vào cơ sở dữ liệu
                    motorType = MotorType.builder().name(carName).brandMotor(brandMotorOpt).build();
                    motorTypeRepository.save(motorType);
                }

                // Sau khi có motorType, tạo parts_motorType và lưu vào cơ sở dữ liệu
                Parts_MotorType parts_motorType = Parts_MotorType.builder()
                        .motorType(motorType)
                        .spareParts(existingSparePart)
                        .build();
                parts_motorTypeRepository.save(parts_motorType);
            }

            return ResponseEntity.status(HttpStatus.OK).body(mapperSpareParts.mapTo(existingSparePart));
        } catch (NumberFormatException | IOException e) {
            return ResponseEntity.badRequest().body("Invalid number format or IO exception: " + e.getMessage());
        }
    }


    @GetMapping("/get-image/{sparePartId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long sparePartId) {
        Optional<SpareParts> sparePartOptional = sparePartsRepository.findById(sparePartId);
        if (sparePartOptional.isPresent()) {
            SpareParts sparePart = sparePartOptional.get();
            String imagePath = sparePart.getImage(); // Assume image path is stored in SparePart entity
            try {
                Path path = Paths.get(imagePath);
                byte[] imageBytes = Files.readAllBytes(path);

                // Set appropriate content type based on image type (e.g., JPEG, PNG, etc.)
                MediaType contentType = MediaType.IMAGE_JPEG; // Adjust based on your image type

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


    // Hàm để lưu ảnh vào thư mục và trả về đường dẫn
    private String saveImage(MultipartFile image) throws IOException {
        // Lưu ảnh vào thư mục
        String originalFilename = image.getOriginalFilename();
        String imagePath = "D:\\LuanVanTotNghiep\\Motorcycle_Parts_V0\\Picture\\SparePart\\" + originalFilename;
        Path path = Paths.get(imagePath);

        // Kiểm tra xem tập tin ảnh đã tồn tại chưa
        int count = 1;
        while (Files.exists(path)) {
            // Nếu tập tin đã tồn tại, thêm số vào tên của tập tin ảnh
            String fileNameWithoutExtension = FilenameUtils.removeExtension(originalFilename);
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String newFileName = fileNameWithoutExtension + count + "." + fileExtension;
            imagePath = "D:\\LuanVanTotNghiep\\Motorcycle_Parts_V0\\Picture\\" + newFileName;
            path = Paths.get(imagePath);
            count++;
        }

        Files.write(path, image.getBytes());
        return imagePath;
    }

    //search spare parts type by name
//    @GetMapping("/search-spare-parts-type-by-name/{name}")
//    public ResponseEntity<List<SparePartsType>> searchSparePartsTypeByName(@PathVariable("name") String name) {
//        List<SparePartsType> sparePartsTypes;
//        if (name == null || name.isEmpty()) {
//            sparePartsTypes = sparePartsTypeRepository.findAll();
//        } else {
//            sparePartsTypes = sparePartsTypeRepository.findSparePartsTypeByNameContaining(name);
//        }
//        return ResponseEntity.ok(sparePartsTypes);
//    }
    @GetMapping("/search-spare-parts-type-by-name")
    public ResponseEntity<List<SparePartsType>> searchSparePartsTypeByName(@RequestParam(value = "name", required = false) String name) {
        List<SparePartsType> sparePartsTypes;
        System.out.println("NameType: " + name);
        if (name == null || name.isEmpty()) {
            sparePartsTypes = sparePartsTypeRepository.findAll();
        } else {
            sparePartsTypes = sparePartsTypeRepository.findSparePartsTypeByNameContaining(name);
        }
        return ResponseEntity.ok(sparePartsTypes);
    }



}






