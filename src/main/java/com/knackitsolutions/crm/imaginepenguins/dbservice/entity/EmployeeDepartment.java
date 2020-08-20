package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import org.hibernate.annotations.LazyToOne;

import javax.persistence.*;

@Entity
@Table(name = "employee_departments")
public class EmployeeDepartment {

    @Id
    @Column(name = "employee_department_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "institute_department_id")
    private InstituteDepartment instituteDepartment;

    public EmployeeDepartment(Employee employee, InstituteDepartment instituteDepartment) {
        this.employee = employee;
        this.instituteDepartment = instituteDepartment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public InstituteDepartment getInstituteDepartment() {
        return instituteDepartment;
    }

    public void setInstituteDepartment(InstituteDepartment instituteDepartment) {
        this.instituteDepartment = instituteDepartment;
    }
}