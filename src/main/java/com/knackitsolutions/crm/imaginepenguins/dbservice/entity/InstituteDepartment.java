package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "departments")
public class InstituteDepartment {

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @OneToMany(mappedBy = "instituteDepartment")
    private Set<InstituteDepartmentPrivilege> privileges = new HashSet<>();

    @OneToMany(mappedBy = "instituteDepartment")
    private Set<UserDepartment> userDepartments = new HashSet<>();

    public InstituteDepartment() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Set<InstituteDepartmentPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<InstituteDepartmentPrivilege> privileges) {
        privileges.forEach(this::setPrivileges);
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Set<UserDepartment> getUserDepartments() {
        return userDepartments;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstituteDepartment)) return false;
        InstituteDepartment that = (InstituteDepartment) o;
        return getId().equals(that.getId()) &&
                getDepartmentName().equals(that.getDepartmentName()) &&
                getInstitute().equals(that.getInstitute()) &&
                Objects.equals(getPrivileges(), that.getPrivileges()) &&
                Objects.equals(getUserDepartments(), that.getUserDepartments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDepartmentName(), getInstitute(), getPrivileges(), getUserDepartments());
    }

    @Override
    public String toString() {
        return "InstituteDepartment{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
//                ", institute=" + institute +
                '}';
    }
}
