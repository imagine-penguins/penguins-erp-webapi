package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

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
    User user;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    Privilege privilege;

    public UserPrivilege() {
    }

    public UserPrivilege(User user, Privilege privilege) {
        this();
        this.user = user;
        this.privilege = privilege;
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

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "UserPrivilege{" +
                "id=" + id +
                ", user=" + user +
                ", privilege=" + privilege +
                '}';
    }
}
