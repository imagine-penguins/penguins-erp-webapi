package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

public class StudentInfo {

    private Long id;
    private String name;  //FirstName + LastName
    private String rollNumber;

    public StudentInfo() {
    }

    public StudentInfo(Long id, String firstName, String lastName, String rollNumber) {
        this.id = id;
        this.name = firstName + " " + lastName;
        this.rollNumber = rollNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
}
