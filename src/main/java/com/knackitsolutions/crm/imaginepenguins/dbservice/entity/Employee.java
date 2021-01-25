package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employees")
@PrimaryKeyJoinColumn(name = "employee_id")
public class Employee extends User{

    private static final Logger log = LoggerFactory.getLogger(Employee.class);

    @Column(name = "employee_type")
    private EmployeeType employeeType;

    @Column(name = "designation")
    private String designation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private Set<Employee> subordinates = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeAttendance> employeeAttendances = new HashSet<>();

    @Column(name = "employee_org_id")
    private String employeeOrgId;

    public Employee() {
    }

    public Employee(Long id, String username, UserType userType, Boolean isAdmin, Boolean isSuperAdmin
            , EmployeeType employeeType, String designation) {
        super(id, username, userType, isAdmin, isSuperAdmin);
        this.employeeType = employeeType;
        this.designation = designation;
    }

    public Employee(EmployeeType employeeType, String designation, Institute institute, Employee manager, Set<Employee> subordinates) {
        this.employeeType = employeeType;
        this.designation = designation;
        this.institute = institute;
        this.manager = manager;
        this.subordinates = subordinates;
    }

    public Employee(Long id, String username, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, EmployeeType employeeType, String designation, Institute institute, Employee manager, Set<Employee> subordinates) {
        super(id, username, userType, isAdmin, isSuperAdmin);
        this.employeeType = employeeType;
        this.designation = designation;
        this.institute = institute;
        this.manager = manager;
        this.subordinates = subordinates;
    }

    public Employee(Long id, String username, String password, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, UserProfile userProfile, EmployeeType employeeType, String designation) {
        super(id, username, password, userType, isAdmin, isSuperAdmin, userProfile);
        this.employeeType = employeeType;
        this.designation = designation;
    }

    public Employee(Long id, String username, String password, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, UserProfile userProfile, EmployeeType employeeType, String designation, Institute institute, Employee manager, Set<Employee> subordinates) {
        super(id, username, password, userType, isAdmin, isSuperAdmin, userProfile);
        this.employeeType = employeeType;
        this.designation = designation;
        this.institute = institute;
        this.manager = manager;
        this.subordinates = subordinates;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
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

    public void setSubordinates(Employee subordinate) {
        subordinates.add(subordinate);
    }

    public void setSubordinates(Set<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return getEmployeeType() == employee.getEmployeeType() &&
                Objects.equals(getDesignation(), employee.getDesignation()) &&
                Objects.equals(getInstitute(), employee.getInstitute()) &&
                Objects.equals(getManager(), employee.getManager()) &&
                Objects.equals(getSubordinates(), employee.getSubordinates());
    }

    public Set<EmployeeAttendance> getEmployeeAttendances() {
        return employeeAttendances;
    }

    public void setEmployeeAttendances(Set<EmployeeAttendance> employeeAttendances) {
        employeeAttendances.forEach(this::setEmployeeAttendances);
    }

    public void setEmployeeAttendances(EmployeeAttendance employeeAttendance) {
        this.employeeAttendances.add(employeeAttendance);
        employeeAttendance.setEmployee(this);

    }

    public String getEmployeeOrgId() {
        return employeeOrgId;
    }

    public void setEmployeeOrgId(String employeeOrgId) {
        this.employeeOrgId = employeeOrgId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmployeeType(), getDesignation(), getInstitute());
    }

    @Override
    public String toString() {
        return super.toString() +
                "Employee{" +
                "employeeType=" + employeeType +
                ", designation='" + designation + '\'' +
                ", institute=" + institute +
                ", manager=" + (manager != null ? manager.getUsername() : "null") +
                '}';
    }
}
