package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.web.JsonPath;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@Entity(name = "userprofile")
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "guardian_relation")
    private String guardianRelation;

    @Column(name = "guardian_phone_number")
    private String guardianPhoneNo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    @Column(name = "date_of_joining", nullable = false, updatable = false)
    private Timestamp dateOfJoining;

    @Transient
    private String doj;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "addressLine1", column = @Column(name = "p_house_number")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "p_street")),
            @AttributeOverride(name = "state", column = @Column(name = "p_state")),
            @AttributeOverride(name = "country", column = @Column(name = "p_country")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "p_zipcode"))
    })
    private Address personalAddress;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "addressLine1", column = @Column(name = "c_house_number")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "c_street")),
            @AttributeOverride(name = "state", column = @Column(name = "c_state")),
            @AttributeOverride(name = "country", column = @Column(name = "c_country")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "c_zipcode"))
    })
    private Address communicationAddress;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "phone",
                    column = @Column(name = "phone_number", nullable = false, unique = true)),
            @AttributeOverride(name = "email",
                    column = @Column(name = "email_address", nullable = false, unique = true)),
            @AttributeOverride(name = "alternatePhone",
                    column = @Column(name = "alternate_phone_number")),
            @AttributeOverride(name = "alternateEmail",
                    column = @Column(name = "alternate_email_address"))
    })
    private Contact contact;

    @Column(name = "blood_group")
    private String bloodGroup;
    @Column(name = "gender")
    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianRelation() {
        return guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }

    public String getGuardianPhoneNo() {
        return guardianPhoneNo;
    }

    public void setGuardianPhoneNo(String guardianPhoneNo) {
        this.guardianPhoneNo = guardianPhoneNo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public UserProfile(){
        this.dateOfJoining = new Timestamp(
                System.currentTimeMillis()
        );
    }

    public UserProfile(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserProfile(String firstName, String lastName, User user) {
        this(firstName, lastName);
        this.user = user;
    }
    public UserProfile(String firstName, String lastName, Address personalAddress, Contact contact) {
        this(firstName, lastName);
        this.personalAddress = personalAddress;
        this.contact = contact;
    }

    public String getDoj() {
        if (doj == null){
            doj = DateTimeFormatter.ISO_DATE_TIME.format(
                    dateOfJoining.toLocalDateTime()
            );

        }
        return  doj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
//        this.id = user.getId();
    }

    public Address getPersonalAddress() {
        return personalAddress;
    }

    public void setPersonalAddress(Address personalAddress) {
        this.personalAddress = personalAddress;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile)) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(contact, that.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, user, personalAddress, contact);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "Id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + personalAddress +
                ", contact=" + contact +
                '}';
    }

    public Timestamp getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Timestamp dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Address getCommunicationAddress() {
        return communicationAddress;
    }

    public void setCommunicationAddress(Address communicationAddress) {
        this.communicationAddress = communicationAddress;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
