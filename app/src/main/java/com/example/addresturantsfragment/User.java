package com.example.addresturantsfragment;

import android.os.Parcel;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    private String address;
    private String photo;
    private  String password;
    private String email;
    private ArrayList<String> favorites;

    public User() {
    }

    public User(String firstName, String lastName, String username, String phone, String address, String photo, String password,String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.photo = photo;
        this.favorites = new ArrayList<>();
        this.password= password;
        this.email= email;
    }

    public User(Parcel in) {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<String> favourits) {
        this.favorites = favourits;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", Photo='" + photo + '\'' +
                '}';
    }
}
