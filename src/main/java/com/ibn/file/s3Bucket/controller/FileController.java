package com.ibn.file.s3Bucket.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibn.file.s3Bucket.service.S3Service;


@RestController
@RequestMapping("/files")
@ControllerAdvice
public class FileController {
    
    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    	String bucketName = "ibn-bucket";
        try {
            String fileName = s3Service.uploadFile(file, bucketName);
            return new ResponseEntity<>("File uploaded successfully: " + fileName, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error occurred during file upload", HttpStatus.INTERNAL_SERVER_ERROR);
        }
     }
    
    @GetMapping("/files")
    public ResponseEntity<String> getAllFiles() throws IOException {
    	String bucketName = "ibn-bucket";
        try {
        	//return
           // String fileName = s3Service.uploadFile(file, bucketName);
        	List<String> files = s3Service.getAllFiles(bucketName);
            return new ResponseEntity<>(files.size() + " files are in bucket: " + files, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
     }
}

