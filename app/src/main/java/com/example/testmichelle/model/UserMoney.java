package com.example.testmichelle.model;

import com.example.testmichelle.activities.SignUpActivity;

public class UserMoney {
    public Integer currentbalance;
    public  Integer freecash;

    public UserMoney(){} //Default used for Firebase/API


    public UserMoney(Integer currentbalance, Integer freecash ){
        this.currentbalance = currentbalance;
        this.freecash = freecash;
    }

    /*-------------------CREATING GET FUNCTIONS FOR ALL THE VARIABLES------------------- */
    public Integer getFreecash() {
        return freecash;
    }
    public Integer getCurrentbalance() {
        return currentbalance;
    }

    /*-------------------CREATING SET FUNCTIONS FOR ALL THE VARIABLES------------------- */
    public void setFreecash(Integer freecash) {
        this.freecash = freecash;
    }
    public void setCurrentbalance(Integer currentbalance) {
        this.currentbalance = currentbalance;
    }

}
