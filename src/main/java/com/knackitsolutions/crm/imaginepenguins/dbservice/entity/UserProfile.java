package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date_of_joining", nullable = false, updatable = false)
    private Timestamp dateOfJoining;

    @JsonIgnore
    @Transient
    private String doj;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "addressLine1", column = @Column(name = "house_number")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "street"))
    })
    private Address address;

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
    public UserProfile(String firstName, String lastName, Address address, Contact contact) {
        this(firstName, lastName);
        this.address = address;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
        return Objects.hash(id, firstName, lastName, user, address, contact);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "Id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", user=" + user +
                ", address=" + address +
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
}
