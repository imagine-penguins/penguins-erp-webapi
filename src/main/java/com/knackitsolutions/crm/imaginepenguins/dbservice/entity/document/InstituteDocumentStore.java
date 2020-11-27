package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;

@Entity
@Table(name = "institute_documents")
@ConfigurationProperties(prefix = "file.institute")
@Data
public class InstituteDocumentStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    private Integer documentId;

    @ManyToOne
    private Institute institute;

    private InstituteDocumentType documentType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "document_format")
    private String documentFormat;

    @Column(name = "upload_dir")
    private String uploadDir;

}
