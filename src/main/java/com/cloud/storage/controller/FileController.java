package com.cloud.storage.controller;

import com.cloud.storage.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cloud.storage.entity.FileMetadata;
import com.cloud.storage.repository.FileMetadataRepository;
import java.util.List;

@RestController

@RequestMapping("/files")
public class FileController {

    @Autowired
    private S3Service s3Service;
    @Autowired
    private FileMetadataRepository repository;


    @GetMapping
    public List<FileMetadata> getAllFiles() {
        return repository.findAll();
    }

    @GetMapping("/download/{id}")
    public String downloadFile(@PathVariable Long id) {

        return s3Service.getFileUrl(id);
    }
    @DeleteMapping("/{id}")
    public String deleteFile(@PathVariable Long id) {
        return s3Service.deleteFile(id);
    }



    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file) {

        try {
            return s3Service.uploadFile(file);

        } catch (Exception e) {
            return "Upload Failed : " + e.getMessage();
        }
    }
}