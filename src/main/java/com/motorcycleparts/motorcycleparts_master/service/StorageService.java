//package com.motorcycleparts.motorcycleparts_master.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class StorageService {
//    private final ImageRepository imageRepository;
//    private final String FOLDER_PATH="/Users/tongcongminh/watch-img/";
//    private final DialRepository dialRepository;
//
//    public String uploadImage(MultipartFile file) throws IOException {
//        String filePath=FOLDER_PATH+file.getOriginalFilename();
//        File isExist=new File(filePath);
//        if(isExist.exists()){
//            //image.png -> image1.png
//            int lastDot = filePath.lastIndexOf(".");
//            filePath = filePath.substring(0, lastDot) + "1" + filePath.substring(lastDot);
//        }
//        file.transferTo(new File(filePath));
//        int lashSlash=filePath.lastIndexOf("/");
//        return filePath.substring(lashSlash+1);
//    }
//    public Image uploadImageToFileSystem(MultipartFile file) throws IOException {
//        String filePath=FOLDER_PATH+file.getOriginalFilename();
//
//        Image fileData=imageRepository.save(Image.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                //image as file path
//                .image(filePath).build());
//
//        file.transferTo(new File(filePath));
//
//        if (fileData != null) {
//            return fileData;
//        }
//        return null;
//    }
//}
//
