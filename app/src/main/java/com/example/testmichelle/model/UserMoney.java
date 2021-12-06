package com.example.testmichelle.model;

import com.example.testmichelle.activities.SignUpActivity;

public class UserMoney {
<<<<<<< HEAD
    public Integer currentbalance, freecash;
=======
    public Integer currentbalance;
    public  Integer freecash;
>>>>>>> e5c05b44235c375536d78aa5815e2cdd5a5a330c


    public UserMoney(){} //Default used for Firebase/API


    public UserMoney(Integer currentbalance, Integer freecash ){
        this.currentbalance = currentbalance;
        this.freecash = freecash;
<<<<<<< HEAD
=======
    }

    public Integer getFreecash() {
        return freecash;
>>>>>>> e5c05b44235c375536d78aa5815e2cdd5a5a330c
    }

    public Integer getCurrentbalance() {
        return currentbalance;
    }

<<<<<<< HEAD
    public Integer getFreecash() {
        return freecash;
    }

=======
>>>>>>> e5c05b44235c375536d78aa5815e2cdd5a5a330c
    public void setFreecash(Integer freecash) {
        this.freecash = freecash;
    }

<<<<<<< HEAD
    public void setCurrentBalance(Integer currentbalance) {
=======
    public void setCurrentbalance(Integer currentbalance) {
>>>>>>> e5c05b44235c375536d78aa5815e2cdd5a5a330c
        this.currentbalance = currentbalance;
    }


}
