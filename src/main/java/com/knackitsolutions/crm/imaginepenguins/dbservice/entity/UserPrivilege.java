package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "user_privileges")
public class UserPrivilege {

    @Id
    @Column(name = "user_privilege_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    User user;

    @ManyToOne
    @JoinColumn(name = "department_privilege_id")
    @JsonManagedReference
    private InstituteDepartmentPrivilege departmentPrivilege;

    public UserPrivilege() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserPrivilege{" +
                "id=" + id +
                ", user=" + user +
                ", departmentPrivilege=" + departmentPrivilege +
                '}';
    }

    public InstituteDepartmentPrivilege getDepartmentPrivilege() {
        return departmentPrivilege;
    }

    public void setDepartmentPrivilege(InstituteDepartmentPrivilege departmentPrivilege) {
        this.departmentPrivilege = departmentPrivilege;
    }
}
