package com.example.testmichelle.model;

import com.example.testmichelle.activities.SignUpActivity;

public class UserMoney {
    public Integer currentbalance, freecash;


    public UserMoney(){} //Default used for Firebase/API


    public UserMoney(Integer currentbalance){
        this.currentbalance = currentbalance;
        this.freecash = freecash;
    }

    public Integer getCurrentBalance() {
        return currentbalance;
    }

    public Integer getFreecash() {
        return freecash;
    }

    public void setFreecash(Integer freecash) {
        this.freecash = freecash;
    }

    public void setCurrentBalance(Integer currentbalance) {
        this.currentbalance = currentbalance;
    }


}
