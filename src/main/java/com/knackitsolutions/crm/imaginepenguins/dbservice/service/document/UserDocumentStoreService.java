package com.knackitsolutions.crm.imaginepenguins.dbservice.service.document;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserDocumentStoreService {
    String storeFile(MultipartFile file, User user, UserDocumentType userDocumentType);

    Resource loadFileAsResource(String fileName) throws Exception;

    String getDocumentName(Long userId, UserDocumentType userDocumentType);
}
