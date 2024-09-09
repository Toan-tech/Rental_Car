package com.spring.Service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadDocument {
    private static final String UPLOAD_FILE = "D:\\FPT Soft Learning\\IT_Java_FullStack\\JWD\\Assignment\\Mock Project\\Toan\\src\\upload";

    @PostMapping("/mycar/add")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
            try {
                // Lưu tệp vào thư mục chỉ định
                String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); //unique name
                String uploadFilePath = UPLOAD_FILE + File.separator + uniqueFileName;
                file.transferTo(new File(uploadFilePath));
                return "/upload/" + uniqueFileName;
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
}


