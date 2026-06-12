package com.pizza.pos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "POS_TBL_User_Profile") 
public class Customer {
    @Id
    private String userId; 
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String street;
    private String location;
    private String city;
    private String state;
    private String pincode;
    private String mobileNo;
    private String emailID;
    private String password;
    private String role;

    // --- EXISTING GETTERS/SETTERS ---
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmailID() { return emailID; }
    public void setEmailID(String emailID) { this.emailID = emailID; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // --- ADD THESE MISSING GETTERS/SETTERS TO FIX THE ERROR ---
    
    public String getStreet() { 
        return street; 
    }
    public void setStreet(String street) { 
        this.street = street; 
    }

    public String getCity() { 
        return city; 
    }
    public void setCity(String city) { 
        this.city = city; 
    }

    public String getMobileNo() { 
        return mobileNo; 
    }
    public void setMobileNo(String mobileNo) { 
        this.mobileNo = mobileNo; 
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
}