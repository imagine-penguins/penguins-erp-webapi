package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_departments")
public class UserDepartment {

    @Id
    @Column(name = "user_department_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "institute_department_id")
    private InstituteDepartment instituteDepartment;

    public UserDepartment(){}

    public UserDepartment(User user, InstituteDepartment instituteDepartment) {
        this.user = user;
        this.instituteDepartment = instituteDepartment;
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

    public InstituteDepartment getInstituteDepartment() {
        return instituteDepartment;
    }

    public void setInstituteDepartment(InstituteDepartment instituteDepartment) {
        this.instituteDepartment = instituteDepartment;
    }

    @Override
    public String toString() {
        return "UserDepartment{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", instituteDepartment=" + instituteDepartment.getId() +
                '}';
    }
}