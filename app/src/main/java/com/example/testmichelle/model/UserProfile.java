package com.example.testmichelle.model;

public class UserProfile {

    //String variables for storing the information
    public String email, password, name, lastname;

    public UserProfile(){} //Default used for Firebase/API


    public UserProfile(String name,String lastname, String email, String password){
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;

    }

    /*-------------------CREATING GET FUNCTIONS FOR ALL THE VARIABLES------------------- */
    public String getName(){
        return name;
    }
    public String getLastname() {
        return lastname;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }

    /*-------------------CREATING SET FUNCTIONS FOR ALL THE VARIABLES------------------- */
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
