package com.example.addresturantsfragment.DataBase;


import com.example.addresturantsfragment.Utilites.Utils;
import com.google.android.gms.maps.model.LatLng;

public class Hotel {
   private String name;
   private String description;
   private String address;
   private String phone;
   private String photo;


   public Hotel() {
   }

   public Hotel(String name, String description, String address, String phone, String photo) {
      this.name = name;
      this.description = description;
      this.address = address;
      this.phone = phone;
      this.photo = photo;

   }



   public double getLat()
   {
      return  Double.parseDouble(address.split(",")[0]);
   }

   public double getLng()
   {
      return  Double.parseDouble(address.split(",")[1]);
   }
   public String getPhoto() {
      return photo;
   }

   public void setPhoto(String photo) {this.photo = photo;}


   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   @Override
   public String toString() {
      return "Hotel{" +
              "name='" + name + '\'' +
              ", description='" + description + '\'' +
              ", address='" + address + '\'' +
              ", phone='" + phone + '\'' +
              ", photo='" + photo + '\'' +


              '}';
   }
}
