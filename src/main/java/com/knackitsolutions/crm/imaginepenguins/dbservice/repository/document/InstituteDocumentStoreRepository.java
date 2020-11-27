package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.document;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.InstituteDocumentStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteDocumentStoreRepository extends JpaRepository<InstituteDocumentStore, Integer> {

    InstituteDocumentStore findByInstituteIdAndDocumentType(Integer instituteId, InstituteDocumentType documentType);

    @Query("SELECT d.fileName from InstituteDocumentStore d where d.institute.id = :instituteId and d.documentType = :documentType")
    String getUploadDocumentPath(Integer instituteId, InstituteDocumentType documentType);
}
