package com.example.addresturantsfragment;



public class Restaurant {
   private String name;
   private String description;
   private String address;
   private String phone;

   public Restaurant() {
   }

   public Restaurant(String name, String description, String address, String phone) {
      this.name = name;
      this.description = description;
      this.address = address;
      this.phone = phone;
   }

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
      return "Restaurant{" +
              "name='" + name + '\'' +
              ", description='" + description + '\'' +
              ", address='" + address + '\'' +
              ", phone='" + phone + '\'' +
              '}';
   }
}
