package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "institute_department_privileges")
public class InstituteDepartmentPrivilege {
    @Id
    @Column(name = "institute_department_privilege_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "institute_department_id")
    @JsonBackReference
    InstituteDepartment instituteDepartment;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    @JsonManagedReference
    Privilege privilege;

    @JsonBackReference
    @OneToMany(mappedBy = "departmentPrivilege")
    private List<UserPrivilege> userPrivileges = new ArrayList<>();

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

    public List<UserPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    public void setUserPrivileges(List<UserPrivilege> userPrivileges) {
        userPrivileges.forEach(this::setPrivileges);
    }

    public void setPrivileges(UserPrivilege userPrivilege) {
        this.userPrivileges.add(userPrivilege);
        userPrivilege.setDepartmentPrivilege(this);
    }

    @Override
    public String toString() {
        return "InstituteDepartmentPrivilege{" +
                "id=" + id +
                ", instituteDepartment=" + instituteDepartment +
                ", privilege=" + privilege +
                '}';
    }
}
