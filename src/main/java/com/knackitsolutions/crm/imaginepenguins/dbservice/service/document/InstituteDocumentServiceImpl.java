package com.knackitsolutions.crm.imaginepenguins.dbservice.service.document;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class InstituteDocumentServiceImpl implements InstituteDocumentStoreService {
    @Override
    public String storeFile(MultipartFile file, Institute institute, InstituteDocumentType instituteDocumentType) {
        return null;
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        return null;
    }

    @Override
    public String getDocumentName(Integer instituteId, InstituteDocumentType documentType) {
        return null;
    }
}
