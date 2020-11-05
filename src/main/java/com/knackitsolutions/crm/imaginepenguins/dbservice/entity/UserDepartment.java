package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDepartment {

    @Id
    @Column(name = "user_department_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "institute_department_id")
    private InstituteDepartment instituteDepartment;

    @OneToOne
    @JoinColumn(name = "department_position_id")
    private DepartmentPosition departmentPosition;

    public UserDepartment(User user, InstituteDepartment instituteDepartment) {
        this.user = user;
        this.instituteDepartment = instituteDepartment;
    }

}