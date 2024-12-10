package com.user.identity.service.Impl;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.user.identity.constant.BucketConstants;
import com.user.identity.exception.AppException;
import com.user.identity.exception.ErrorCode;
import com.user.identity.repository.UserRepository;
import com.user.identity.repository.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FirebaseStorageClient {

    private final StorageClient storageClient;

    public FirebaseStorageClient(StorageClient storageClient) {
        this.storageClient = storageClient;

    }


    // Phương thức tải file lên Firebase Storage
    String uploadFileToBucket(String bucketName, String folderName, MultipartFile file) {
        try {
            String normalizedFolderName = folderName.endsWith("/") ? folderName.substring(0, folderName.length() - 1) : folderName;
            String generatedFileName = generateUniqueFileName(file.getOriginalFilename());
            String fullObjectName = normalizedFolderName + "/" + generatedFileName;
            Storage storage = storageClient.bucket().getStorage();
            BlobInfo blobInfo = createBlobInfo(bucketName, fullObjectName, file.getContentType());
            storage.create(blobInfo, file.getBytes());
            makeFilePublic(bucketName, fullObjectName);
            return createPublicUrl(bucketName, fullObjectName);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private String createPublicUrl(String bucketName, String objectName) {
        String encodedObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8)
                .replace("+", "%20");
        return String.format(BucketConstants.URL_FIREBASE_API.getValue(), bucketName, encodedObjectName);
    }

    private void makeFilePublic(String bucketName, String objectName) {
        Storage storage = storageClient.bucket().getStorage();
        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob != null) {
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        } else {
            log.error("File not found: {}", objectName);
            throw new AppException(ErrorCode.REQUEST_NULL);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    private BlobInfo createBlobInfo(String bucketName, String objectName, String contentType) {
        return BlobInfo.newBuilder(BlobId.of(bucketName, objectName))
                .setContentType(contentType)
                .build();
    }
}
