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

    @Column(name = "privilege_code", unique = true)
    String privilegeCode;

    @Column(name = "privilege_desc")
    String privilegeDesc;

    @OneToMany(mappedBy = "privilege")
    Set<InstituteDepartmentPrivilege> instituteDepartmentPrivileges = new HashSet<>();

    @OneToMany(mappedBy = "privilege")
    List<UserPrivilege> userPrivileges = new ArrayList<>();

    @OneToMany(mappedBy = "privilege", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    Set<Dashboard> dashboards = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "parent_privilege_id")
    Privilege privilege;

    @OneToMany(mappedBy = "privilege")
    Set<Privilege> privileges = new HashSet<>();

    protected Privilege() {
    }

    public Privilege(String privilegeCode, String privilegeName) {
        this.privilegeName = privilegeCode;
        this.privilegeDesc = privilegeName;
    }

    public Privilege(Integer id, String privilegeCode, String privilegeName) {
        this.id = id;
        this.privilegeName = privilegeCode;
        this.privilegeDesc = privilegeName;
    }

    public Privilege(Integer id, String privilegeName, String privilegeCode, String privilegeDesc) {
        this.id = id;
        this.privilegeName = privilegeName;
        this.privilegeCode = privilegeCode;
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

    public Set<InstituteDepartmentPrivilege> getInstituteDepartmentPrivileges() {
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

    public String getPrivilegeCode() {
        return privilegeCode;
    }

    public void setPrivilegeCode(String privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    public Set<Dashboard> getDashboards() {
        return dashboards;
    }

    public void setDashboards(Set<Dashboard> dashboards) {
        this.dashboards.addAll(dashboards);
    }

    public void setDashboards(Dashboard dashboard) {
        this.dashboards.add(dashboard);
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges.addAll(privileges);
    }

    public void setPrivileges(Privilege privilege) {
        privileges.add(privilege);
    }
}
