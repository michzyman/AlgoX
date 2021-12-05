package com.example.testmichelle.model;

import android.hardware.lights.LightState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {
    public boolean status;
    public String stockname;
    public Integer currentbalance;
    public ArrayList<String> buyingrule;
    public ArrayList<String> sellingrule;
    public Algorithm(){
        //Empty constructor for firebase
    }
    public Algorithm(boolean status, String stockname, Integer initialamount,
                     ArrayList<String> buyingrule, ArrayList<String> sellingrule){
        this.status = status;
        this.stockname = stockname;
        this.currentbalance = initialamount;
        this.buyingrule = buyingrule;
        this.sellingrule = sellingrule;
    }

    public boolean getStatus() {
        return status;
    }
    public String getStockname(){
        return stockname;
    }

    public Integer getCurrentbalance() {
        return currentbalance;
    }

    public ArrayList<String> getBuyingrule() {
        return buyingrule;
    }

    public ArrayList<String> getSellingrule() {
        return sellingrule;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public void setCurrentbalance(Integer initialamount) {
        this.currentbalance = initialamount;
    }

    public void setBuyingrule(ArrayList<String> buyingrule) {
        this.buyingrule = buyingrule;
    }

    public void setSellingrule(ArrayList<String> sellingrule) {
        this.sellingrule = sellingrule;
    }
    /*
    private void algorithmToUse() {
    String status = "Running";
    String stockname = "APPL";
    Integer initialamount = 100;
    String[] list = {"p1","p2","type"};
    ArrayList<String> buyingrule = new ArrayList<String>(Arrays.asList(list));
    ArrayList<String> sellingrule = new ArrayList<String>(Arrays.asList(list));
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    Algorithm algorithm = new Algorithm(status, stockname, initialamount, buyingrule, sellingrule);
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
    databaseReference.child(firebaseUser.getUid()).child("Algorithms").setValue(algorithm);
}

     */
}
