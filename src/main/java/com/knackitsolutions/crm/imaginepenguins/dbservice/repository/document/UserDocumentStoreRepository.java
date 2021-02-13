package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.document;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.UserDocumentStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocumentStoreRepository extends JpaRepository<UserDocumentStore, Long> {
    UserDocumentStore findByUserIdAndDocumentType(Long userId, UserDocumentType documentType);

    @Query("SELECT d.storeURL from UserDocumentStore d where d.user.id = :userId and d.documentType = :userDocumentType")
    String getUploadDocumentPath(Long userId, UserDocumentType userDocumentType);

}
