package com.example.testmichelle.model;

public class UserProfile {
    //String variables for storing the information

    public String email, password, name;

    public UserProfile(){} //Default used for Firebase/API


    public UserProfile(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
