package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity(name = "user")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id@Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", updatable = false)
    private String username;

    @Column(name = "user_type", updatable = false)
    private UserType userType;

    @Column(name = "password")
    private String password;

    private Boolean isAdmin;

    private Boolean isSuperAdmin;

    private Boolean isActive;

    private Boolean isVerified;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user")
    private Set<UserDepartment> userDepartments = new HashSet<>();

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Employee employee;
//
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Parent parent;
//
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Student student;

    @OneToMany(mappedBy = "user", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    private List<UserPrivilege> userPrivileges;

    public User(){

    }

    public User(Long id, String username, UserType userType, Boolean isAdmin, Boolean isSuperAdmin) {
        this.id = id;
        this.username = username;
        this.userType = userType;
        this.isAdmin = isAdmin;
        this.isSuperAdmin = isSuperAdmin;
    }

    public User(Long id, String username, String password, UserType userType,
                Boolean isAdmin, Boolean isSuperAdmin, UserProfile userProfile) {
        this(id, username, userType, isAdmin, isSuperAdmin);
        this.password = password;
        this.userProfile = userProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userType, isAdmin, isSuperAdmin, isActive, isVerified, userProfile);
    }

//    public Employee getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(Employee employee) {
//        this.employee = employee;
//    }
//
//    public Parent getParent() {
//        return parent;
//    }
//
//    public void setParent(Parent parent) {
//        this.parent = parent;
//    }
//
//    public Student getStudent() {
//        return student;
//    }
//
//    public void setStudent(Student student) {
//        this.student = student;
//    }

    public List<UserPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    public void setUserPrivileges(List<UserPrivilege> userPrivileges) {
        this.userPrivileges = userPrivileges;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userType=" + userType +
                ", isAdmin=" + isAdmin +
                ", isSuperAdmin=" + isSuperAdmin +
                ", isActive=" + isActive +
                ", isVerified=" + isVerified +
//                ", userProfile=" + userProfile +
//                ", employee=" + employee +
//                ", parent=" + parent +
//                ", student=" + student +
//                ", userPrivileges=" + userPrivileges +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserDepartment> getUserDepartments() {
        return userDepartments;
    }

    public void setUserDepartments(Set<UserDepartment> userDepartments) {
        this.userDepartments = userDepartments;
    }

    public void setUserDepartment(UserDepartment userDepartment){
        userDepartments.add(userDepartment);
    }
}
