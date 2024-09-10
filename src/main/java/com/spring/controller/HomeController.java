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
    public String addCar(@RequestParam("file0") MultipartFile file0,
                         @RequestParam("file1") MultipartFile file1,
                         @RequestParam("file2") MultipartFile file2,
                         @RequestParam("file3") MultipartFile file3,
                         @RequestParam("file4") MultipartFile file4,
                         @RequestParam("file5") MultipartFile file5,
                         @RequestParam("file6") MultipartFile file6,
                         @RequestParam("color") String color,
                         Model model
                         ) {
        MultipartFile[] files = {file0, file1, file2, file3, file4, file5, file6};
        for (int i = 0; i < files.length; i++) {
            if (i <= 2){
                upFiles(files[i]);
            } else {
                upImages(files[i]);
            }
        }
        System.out.println(color);
        return "redirect:/addcar";
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

