package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "privileges")
public class Privilege {
    @Id
    @Column(name = "privilege_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(name = "privilege_name", unique = true)
    String privilegeName;

    @Column(name = "privilege_desc")
    String privilegeDesc;

    @OneToMany(mappedBy = "privilege")
    Set<InstituteDepartmentPrivilege> instituteDepartmentPrivileges = new HashSet<>();

    @OneToMany(mappedBy = "privilege")
    List<UserPrivilege> userPrivileges = new ArrayList<>();

    protected Privilege() {
    }

    public Privilege(String privilegeName, String privilegeDesc) {
        this();
        this.privilegeName = privilegeName;
        this.privilegeDesc = privilegeDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Privilege)) return false;
        Privilege privilege = (Privilege) o;
        return Objects.equals(id, privilege.id) &&
                Objects.equals(privilegeName, privilege.privilegeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, privilegeName);
    }

    @Override
    public String toString() {
        return "Privilege{" +
                "id=" + id +
                ", privilegeName='" + privilegeName + '\'' +
                ", privilegeDesc='" + privilegeDesc + '\'' +
                ", instituteDepartmentPrivileges=" + instituteDepartmentPrivileges +
                ", userPrivileges=" + userPrivileges +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getPrivilegeDesc() {
        return privilegeDesc;
    }

    public void setPrivilegeDesc(String privilegeDesc) {
        this.privilegeDesc = privilegeDesc;
    }

    public Set<InstituteDepartmentPrivilege> getInstituteDepartmentPrivilages() {
        return instituteDepartmentPrivileges;
    }

    public void setInstituteDepartmentPrivileges(Set<InstituteDepartmentPrivilege> instituteDepartmentPrivileges) {
        this.instituteDepartmentPrivileges = instituteDepartmentPrivileges;
    }

    public void addInstituteDepartmentPrivilege(){

    }

    public List<UserPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    public void setUserPrivileges(List<UserPrivilege> userPrivileges) {
        this.userPrivileges = userPrivileges;
    }
}
