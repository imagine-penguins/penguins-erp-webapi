package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "department_positions")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DepartmentPosition {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "department_id")
    InstituteDepartment instituteDepartment;

    @Column(name = "position_name")
    String positionName;

    @OneToOne(mappedBy = "departmentPosition")
    UserDepartment userDepartment;

}
