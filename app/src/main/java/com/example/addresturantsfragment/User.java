package com.example.addresturantsfragment;

public class User {
    private String name;
    private String lastname;
    private String hobbies;
    private String phone;
    private String livingarea;



    public User(){

    }

    public User(String name, String lastname, String username, String hobbies, String phone, String livingarea) {
        this.name = name;
        this.lastname = lastname;

        this.hobbies = hobbies;
        this.phone = phone;
        this.livingarea = livingarea;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLivingarea() {
        return livingarea;
    }

    public void setLivingarea(String livingarea) {
        this.livingarea = livingarea;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", hobbies='" + hobbies + '\'' +
                ", phone='" + phone + '\'' +
                ", livingarea='" + livingarea + '\'' +
                '}';
    }
}
