package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.Attendance;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity(name = "user")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id@Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", updatable = false)
    private String username;

    @Column(name = "user_type", updatable = false)
    private UserType userType;

    @Column(name = "password")
    private String password;

    @Column(name = "is_admin")
    private Boolean admin;

    @Column(name = "is_super_admin")
    private Boolean superAdmin;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "is_verified")
    private Boolean verified;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user")
    private Set<UserDepartment> userDepartments = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    private List<UserPrivilege> userPrivileges;

    @OneToMany(mappedBy = "user")
    private List<Attendance> attendances = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Optional
                .ofNullable(getVerified())
                .orElse(Boolean.TRUE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE;
    }

    public User(){

    }

    public User(Long id, String username, UserType userType, Boolean admin, Boolean superAdmin) {
        this.id = id;
        this.username = username;
        this.userType = userType;
        this.admin = admin;
        this.superAdmin = superAdmin;
    }

    public User(Long id, String username, String password, UserType userType,
                Boolean admin, Boolean superAdmin, UserProfile userProfile) {
        this(id, username, userType, admin, superAdmin);
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
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
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
        return Objects.hash(id, username, userType, admin, superAdmin, active, verified);
    }

    public List<UserPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    public void setUserPrivileges(List<UserPrivilege> userPrivileges) {
        userPrivileges.forEach(this::setUserPrivileges);
    }

    public void setUserPrivileges(UserPrivilege userPrivilege) {
        this.userPrivileges.add(userPrivilege);
        userPrivilege.setUser(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userType=" + userType +
                ", isAdmin=" + admin +
                ", isSuperAdmin=" + superAdmin +
                ", isActive=" + active +
                ", isVerified=" + verified +
                ", userProfile=" + userProfile +
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
        userDepartments.forEach(this::setUserDepartment);
    }

    public void setUserDepartment(UserDepartment userDepartment){
        userDepartments.add(userDepartment);
        userDepartment.setUser(this);
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        attendances.forEach(this::setAttendances);
    }

    public void setAttendances(Attendance attendance) {
        this.attendances.add(attendance);
        attendance.setUser(this);
    }
}
