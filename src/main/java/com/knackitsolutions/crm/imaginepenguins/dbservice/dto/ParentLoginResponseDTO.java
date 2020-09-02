package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;

import java.util.List;

public class ParentLoginResponseDTO extends UserLoginResponseDTO {
    private String QR;

    private List<Student> students;

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public ParentLoginResponseDTO() {
    }

    public ParentLoginResponseDTO(String QR, List<Student> students) {
        this.QR = QR;
        this.students = students;
    }

//    public ParentLoginResponseDTO(Long id, String username, Boolean isAdmin, Boolean isSuperAdmin, UserType userType, UserProfileDTO profile, List<Integer> privileges, String QR, List<Student> students) {
//        super(id, username, isAdmin, isSuperAdmin, userType, profile, privileges);
//        this.QR = QR;
//        this.students = students;
//    }
}
