package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee{

    private static final Logger log = LoggerFactory.getLogger(Employee.class);

    @Id
    @Column(name = "employee_id", updatable = false)
    private Long id;

    @Column(name = "employee_type")
    private EmployeeType employeeType;

    @OneToOne(mappedBy = "employee")
    private Teacher teacher;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    @JoinColumn(name = "employee_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private Set<Employee> subordinates;

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeDepartment> employeeDepartments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        log.info("User Id: {}", user.getId());
        this.id = user.getId();
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Set<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    public Set<EmployeeDepartment> getEmployeeDepartments() {
        return employeeDepartments;
    }

    public void setEmployeeDepartments(Set<EmployeeDepartment> employeeDepartments) {
        this.employeeDepartments = employeeDepartments;
    }

    public Employee() {
    }

    public Employee(Long id, User user, EmployeeType employeeType){
        this.id = id;
        this.employeeType = employeeType;
        this.user = user;
    }

    public Employee(Long id, EmployeeType employeeType, Teacher teacher, User user, Employee manager, Set<Employee> subordiantes) {
        this(id, user, employeeType);
        this.teacher = teacher;
        this.manager = manager;
        this.subordinates = subordiantes;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employeeType=" + employeeType +
                ", teacher=" + teacher +
                ", user=" + user +
                ", manager=" + manager +
                ", subordinates=" + subordinates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(getId(), employee.getId()) &&
                getEmployeeType() == employee.getEmployeeType() &&
                Objects.equals(getTeacher(), employee.getTeacher()) &&
                Objects.equals(getUser(), employee.getUser()) &&
                Objects.equals(getManager(), employee.getManager()) &&
                Objects.equals(getSubordinates(), employee.getSubordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmployeeType(), getTeacher(), getUser(), getManager(), getSubordinates());
    }
}
