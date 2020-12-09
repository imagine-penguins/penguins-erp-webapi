package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;

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
    PrivilegeCode privilegeCode;

    @Column(name = "privilege_desc")
    String privilegeDesc;

    @Column(name = "logo")
    String logo;

    @OneToMany(mappedBy = "privilege")
            @JsonBackReference
    Set<InstituteDepartmentPrivilege> instituteDepartmentPrivileges = new HashSet<>();

//    @OneToMany(mappedBy = "privilege")
//    List<UserPrivilege> userPrivileges = new ArrayList<>();

    @OneToMany(mappedBy = "privilege", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
            @JsonBackReference
    Set<Dashboard> dashboards = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "parent_privilege_id")
            @JsonManagedReference
    Privilege parentPrivilege;

    @OneToMany(mappedBy = "parentPrivilege", fetch = FetchType.EAGER)
            @JsonBackReference
    Set<Privilege> privileges = new HashSet<>();

    public Privilege() {
    }

    public Privilege(PrivilegeCode privilegeCode, String privilegeName, String privilegeDesc) {
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
                Objects.equals(privilegeCode, privilege.privilegeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, privilegeCode);
    }

    @Override
    public String toString() {
        return "Privilege{" +
                "id=" + id +
                ", privilegeName='" + privilegeName + '\'' +
                ", privilegeCode=" + privilegeCode +
                ", privilegeDesc='" + privilegeDesc + '\'' +
                ", logo='" + logo + '\'' +
                ", parentPrivilege=" + Optional.ofNullable(parentPrivilege).map(parentPrivilege -> parentPrivilege.getId()).orElse(null) +
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
        instituteDepartmentPrivileges.forEach(this::addInstituteDepartmentPrivilege);
    }

    public void addInstituteDepartmentPrivilege(InstituteDepartmentPrivilege instituteDepartmentPrivilege){
        this.instituteDepartmentPrivileges.add(instituteDepartmentPrivilege);
        instituteDepartmentPrivilege.setPrivilege(this);
    }

/*    public List<UserPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    public void setUserPrivileges(List<UserPrivilege> userPrivileges) {
        this.userPrivileges = userPrivileges;
    }*/

    public PrivilegeCode getPrivilegeCode() {
        return privilegeCode;
    }

    public void setPrivilegeCode(PrivilegeCode privilegeCode) {
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

    public Privilege getParentPrivilege() {
        return parentPrivilege;
    }

    public void setParentPrivilege(Privilege parentPrivilege) {
        this.parentPrivilege = parentPrivilege;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        privileges.forEach(this::setParentPrivilege);
    }

    public void setPrivileges(Privilege privilege) {
        privileges.add(privilege);
        privilege.setParentPrivilege(this);

    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



}
