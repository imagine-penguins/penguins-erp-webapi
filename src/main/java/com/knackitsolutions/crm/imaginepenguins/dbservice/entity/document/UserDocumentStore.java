package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;

@Entity
@Table(name = "user_documents")
@ConfigurationProperties(prefix = "file.user")
@Data
public class UserDocumentStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    Long documentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "document_type")
    private UserDocumentType documentType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "document_format")
    private String documentFormat;

    @Column(name = "upload_dir")
    private String uploadDir;

    @Column(name = "store_url")
    private String storeURL;
}
