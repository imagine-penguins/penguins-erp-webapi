package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "institute_department_privileges")
public class InstituteDepartmentPrivilege {
    @Id
    @Column(name = "institute_department_privilege_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "institute_department_id")
    InstituteDepartment instituteDepartment;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    Privilege privilege;

    public InstituteDepartmentPrivilege() {
    }

    public InstituteDepartmentPrivilege(InstituteDepartment instituteDepartment, Privilege privilege) {
        this.instituteDepartment = instituteDepartment;
        this.privilege = privilege;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InstituteDepartment getInstituteDepartment() {
        return instituteDepartment;
    }

    public void setInstituteDepartment(InstituteDepartment instituteDepartment) {
        this.instituteDepartment = instituteDepartment;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }
}
