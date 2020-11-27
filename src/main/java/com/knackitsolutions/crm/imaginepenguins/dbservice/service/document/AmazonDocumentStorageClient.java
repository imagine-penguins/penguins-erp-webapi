package com.knackitsolutions.crm.imaginepenguins.dbservice.service.document;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.InstituteDocumentStore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.UserDocumentStore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.DataStorageException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.document.InstituteDocumentStoreRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.document.UserDocumentStoreRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "amazon-properties")
@Slf4j
public class AmazonDocumentStorageClient implements InstituteDocumentStoreService, UserDocumentStoreService {

    private final AmazonS3 amazonS3;
    private final InstituteDocumentStoreRepository instituteDocumentStoreRepository;
    private final UserDocumentStoreRepository userDocumentStoreRepository;
    private final InstituteRepository instituteRepository;
    private final UserService userService;

    @Value("${amazon-properties.bucket-name}")
    private String bucketName;

    @Value("${file.institute.upload-dir}")
    private String instituteUploadDir;

    @Value("${file.user.upload-dir}")
    private String userUploadDir;

    @Override
    public String storeFile(MultipartFile multipartFile, Institute institute, InstituteDocumentType instituteDocumentType) {
        String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String fileName = "";
        URL resourceURL = null;
        try {
            final File file = convertMultipartFileToFileObject(multipartFile);

            if (originalFileName.contains("..")) {
                throw new DataStorageException("Sorry! filename contains invalid path sequence");
            }
            String fileExtension = originalFileName.substring(originalFileName.indexOf("."));
            fileName = instituteUploadDir + "/" + institute.getId() + "_" + instituteDocumentType.getDocType() + fileExtension;

            uploadFileTos3bucket(fileName, file);
            resourceURL = amazonS3.getUrl(bucketName, fileName);

            file.delete();
            InstituteDocumentStore store1 = instituteDocumentStoreRepository
                    .findByInstituteIdAndDocumentType(institute.getId(), instituteDocumentType);
            if (store1 != null) {
                store1.setDocumentFormat(multipartFile.getContentType());
                store1.setFileName(fileName);
                instituteDocumentStoreRepository.save(store1);
            } else {
                InstituteDocumentStore newStore = new InstituteDocumentStore();
                institute.setInstituteDocumentStores(newStore);
                newStore.setFileName(fileName);
                newStore.setDocumentFormat(multipartFile.getContentType());
                newStore.setDocumentType(instituteDocumentType);
                instituteDocumentStoreRepository.save(newStore);
            }
        } catch (Exception exception) {
            throw new DataStorageException("Could not store file " + fileName + ". Please try again!.", exception);
        }
        return resourceURL.toExternalForm();
    }

    private File convertMultipartFileToFileObject(MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            log.error("Error converting the multi-part file to file= ", ex.getMessage());
        }
        return file;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        amazonS3.putObject(
                new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    @Override
    public Resource loadFileAsResource(String fileName) throws Exception{

        try {
            Resource resource = new UrlResource(fileName);
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File Not Found: " + fileName);
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            throw new FileNotFoundException("File Not Found: " + fileName);
        }
    }

    @Override
    public String getDocumentName(Integer instituteId, InstituteDocumentType documentType) {
        return instituteDocumentStoreRepository.getUploadDocumentPath(instituteId, documentType);
    }

    @Override
    public String storeFile(MultipartFile multipartFile, User user, UserDocumentType userDocumentType) {
        String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String fileName = "";

        String resourceURL = "";
        try {
            final File file = convertMultipartFileToFileObject(multipartFile);

            if (originalFileName.contains("..")) {
                throw new DataStorageException("Sorry! filename contains invalid path sequence");
            }
            String fileExtension = originalFileName.substring(originalFileName.indexOf("."));
            fileName = userUploadDir + "/" + user.getId() + "_" + userDocumentType.getDocType() + fileExtension;

            uploadFileTos3bucket(fileName, file);
            resourceURL = amazonS3.getUrl(bucketName, fileName).toExternalForm();

            file.delete();
            UserDocumentStore store = user
                    .getUserDocumentStores()
                    .stream()
                    .filter(userDocumentStore -> userDocumentStore.getDocumentType() == userDocumentType)
                    .findFirst()
                    .orElseGet(() -> new UserDocumentStore());
            if (store.getDocumentId() != null) {
                store.setDocumentFormat(multipartFile.getContentType());
                store.setFileName(fileName);
                userDocumentStoreRepository.save(store);
            } else {
                user.setUserDocumentStores(store);
                store.setFileName(fileName);
                store.setDocumentFormat(multipartFile.getContentType());
                store.setDocumentType(userDocumentType);
                userDocumentStoreRepository.save(store);
            }
        } catch (Exception exception) {
            throw new DataStorageException("Could not store file " + fileName + ". Please try again!.", exception);
        }
        return resourceURL;
    }

    @Override
    public String getDocumentName(Long userId, UserDocumentType userDocumentType) {
        return userDocumentStoreRepository.getUploadDocumentPath(userId, userDocumentType);
    }
}
