package com.knackitsolutions.crm.imaginepenguins.dbservice.service.document;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface InstituteDocumentStoreService {
    String storeFile(MultipartFile file, Institute institute, InstituteDocumentType instituteDocumentType);

    Resource loadFileAsResource(String fileName) throws Exception;

    String getDocumentName(Integer instituteId, InstituteDocumentType documentType);
}
