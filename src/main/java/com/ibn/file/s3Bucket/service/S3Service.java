package com.ibn.file.s3Bucket.service;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class S3Service {

    private AmazonS3 s3Client;

    public S3Service(@Value("${AWS_ACCESS_KEY_ID}") String accessKey,
                     @Value("${AWS_SECRET_ACCESS_KEY}") String secretKey,
                     @Value("${AWS_REGION}") String region) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        String fileName = UUID.randomUUID().toString()+ "_" + file.getOriginalFilename();
        s3Client.putObject(bucketName, fileName, file.getInputStream(), null);
        return fileName;
    }
    
    public List<String> getAllFiles(String bucketName) throws IOException {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName);
        ListObjectsV2Result listObjectsV2Result  = s3Client.listObjectsV2(listObjectsV2Request);
        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

        List<String> fileNames = objectSummaries.stream()
                .map(S3ObjectSummary::getKey)
                .toList();
        
        return fileNames;
    }

}
