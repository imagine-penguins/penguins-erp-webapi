package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.HashSet;
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

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @OneToMany(mappedBy = "instituteDepartment")
    private Set<InstituteDepartmentPrivilege> privileges = new HashSet<>();

    protected InstituteDepartment() {
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

    public void setPrivileges(Set<InstituteDepartmentPrivilege> privilages) {
        this.privileges = privileges;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }
}
