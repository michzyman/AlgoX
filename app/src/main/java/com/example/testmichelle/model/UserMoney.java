package com.example.testmichelle.model;

import com.example.testmichelle.activities.SignUpActivity;

public class UserMoney {
    private Integer currentbalance;

    public UserMoney(){} //Default used for Firebase/API


    public UserMoney(Integer currentbalance){
        this.currentbalance = currentbalance;
    }

    public Integer getCurrentBalance() {
        return currentbalance;
    }

    public void setCurrentBalance(Integer currentbalance) {
        this.currentbalance = currentbalance;
    }


}
