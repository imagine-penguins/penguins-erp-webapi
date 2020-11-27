package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@NoArgsConstructor
@AllArgsConstructor
public class InstituteDepartment {

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter@Getter
    private Long id;

    @Column(name = "department_name")
    @Setter@Getter
    private String departmentName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institute_id")
    @Setter@Getter
    private Institute institute;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "head")
    @Getter@Setter
    private User user;

    @OneToMany(mappedBy = "instituteDepartment")
    @Getter
    private Set<InstituteDepartmentPrivilege> privileges = new HashSet<>();

    @OneToMany(mappedBy = "instituteDepartment")
    @Getter
    private Set<UserDepartment> userDepartments = new HashSet<>();
/*
    @OneToMany(mappedBy = "instituteDepartment")
    @Getter
    private Set<EmployeeAttendance> employeeAttendances = new HashSet<>();*/

    @OneToMany(mappedBy = "instituteDepartment")
    @Getter
    private Set<DepartmentPosition> departmentPositions = new HashSet<>();

    public InstituteDepartment(String departmentName){
        this.departmentName = departmentName;
    }

    public InstituteDepartment(Long id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }

    public InstituteDepartment(String departmentName, Institute institute) {
        this();
        this.departmentName = departmentName;
        this.institute = institute;
    }

    public InstituteDepartment(String departmentName, Institute institute, Set<InstituteDepartmentPrivilege> privileges) {
        this(departmentName, institute);
        this.privileges = privileges;
    }

    /*public void setEmployeeAttendances(Set<EmployeeAttendance> employeeAttendances) {
        employeeAttendances.forEach(this::setEmployeeAttendances);
    }

    public void setEmployeeAttendances(EmployeeAttendance employeeAttendance) {
        this.employeeAttendances.add(employeeAttendance);
        employeeAttendance.setInstituteDepartment(this);
    }*/

    public void setPrivileges(Set<InstituteDepartmentPrivilege> privileges) {
        privileges.forEach(this::setPrivileges);
    }

    public void addUserDepartment(Set<UserDepartment> userDepartments) {
        userDepartments.forEach(this::addUserDepartment);
    }

    public void addUserDepartment(UserDepartment userDepartment) {
        this.userDepartments.add(userDepartment);
        userDepartment.setInstituteDepartment(this);
    }

    public void setPrivileges(InstituteDepartmentPrivilege privilege) {
        this.privileges.add(privilege);
        privilege.setInstituteDepartment(this);
    }

    public void setDepartmentPositions(Set<DepartmentPosition> departmentPositions) {
        departmentPositions.forEach(this::setDepartmentPositions);
    }

    public void setDepartmentPositions(DepartmentPosition departmentPosition) {
        this.departmentPositions.add(departmentPosition);
        departmentPosition.setInstituteDepartment(this);
    }
}
