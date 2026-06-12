package com.cloud.storage.service;


import com.cloud.storage.entity.FileMetadata;
import com.cloud.storage.repository.FileMetadataRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import java.io.File;
import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final FileMetadataRepository repository;

    private final String bucketName =
            "vinayak-file-storage-project";

    public S3Service(
            S3Client s3Client,
            FileMetadataRepository repository) {

        this.s3Client = s3Client;
        this.repository = repository;
    }

    public String uploadFile(MultipartFile file)
            throws IOException {

        File tempFile =
                File.createTempFile("upload-", file.getOriginalFilename());

        file.transferTo(tempFile);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(file.getOriginalFilename())
                        .build(),
                tempFile.toPath()
        );

        FileMetadata metadata = new FileMetadata();

        metadata.setFileName(file.getOriginalFilename());
        metadata.setS3Key(file.getOriginalFilename());

        metadata.setFileUrl(
                "https://" + bucketName +
                        ".s3.ap-south-1.amazonaws.com/" +
                        file.getOriginalFilename()
        );

        repository.save(metadata);

        tempFile.delete();

        return "File Uploaded Successfully";
    }
    public String deleteFile(Long id) {

        FileMetadata metadata = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("File not found"));

        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(metadata.getS3Key())
                        .build()
        );

        repository.delete(metadata);

        return "File Deleted Successfully";
    }
    public String getFileUrl(Long id) {

        FileMetadata metadata =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("File not found"));

        return metadata.getFileUrl();
    }
}
