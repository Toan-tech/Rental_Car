package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class HomeController {
    private static final String UPLOAD_FILE = "D:\\FPT Soft Learning\\IT_Java_FullStack\\JWD\\Assignment\\Mock Project\\Toan\\src\\main\\resources\\static\\documents";
    private static final String UPLOAD_IMAGES = "D:\\FPT Soft Learning\\IT_Java_FullStack\\JWD\\Assignment\\Mock Project\\Toan\\src\\main\\resources\\static\\images";

    @PostMapping("/mycar/add")
    public String addCar(@RequestParam("file") MultipartFile file) {

        return "";
    }

    public String upFiles(MultipartFile file) {
        try {
            // Lưu tệp vào thư mục chỉ định
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); //unique name
            String uploadFilePath = UPLOAD_FILE + File.separator + uniqueFileName;
            file.transferTo(new File(uploadFilePath));
            return "/documents/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String upImages(MultipartFile file) {
        try {
            // Lưu tệp vào thư mục chỉ định
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); //unique name
            String uploadFilePath = UPLOAD_IMAGES + File.separator + uniqueFileName;
            file.transferTo(new File(uploadFilePath));
            return "/images/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}

