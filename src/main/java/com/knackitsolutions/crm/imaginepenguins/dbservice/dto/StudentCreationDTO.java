package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

import javax.persistence.Id;
import java.util.List;

public class StudentCreationDTO extends UserCreationDTO{

    Long classId;

    Long parentId;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }


    public StudentCreationDTO(String username, String password, Long classId, Long parentId) {
        super(username, password);
        this.classId = classId;
        this.parentId = parentId;
    }

    public StudentCreationDTO(String username, String password, Boolean isAdmin, Boolean isSuperAdmin, Long classId, Long parentId) {
        super(username, password, isAdmin, isSuperAdmin);
        this.classId = classId;
        this.parentId = parentId;
    }

    public StudentCreationDTO(String username, String password, Boolean isAdmin, Boolean isSuperAdmin, List<Integer> privileges, UserProfileDTO profile, Long classId, Long parentId) {
        super(username, password, isAdmin, isSuperAdmin, privileges, profile);
        this.classId = classId;
        this.parentId = parentId;
    }

    public StudentCreationDTO(Long classId, Long parentId) {
        this.classId = classId;
        this.parentId = parentId;
    }
}
