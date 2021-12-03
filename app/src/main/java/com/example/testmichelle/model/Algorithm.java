package com.example.testmichelle.model;

import android.hardware.lights.LightState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {
    public String status, stockname;
    public Integer initialamount;
    public ArrayList<String> buyingrule;
    public ArrayList<String> sellingrule;
    public Algorithm(){
        //Empty constructor for firebase
    }
    public Algorithm(String status, String stockname, Integer initialamount,
                     ArrayList<String> buyingrule, ArrayList<String> sellingrule){
        this.status = status;
        this.stockname = stockname;
        this.initialamount = initialamount;
        this.buyingrule = buyingrule;
        this.sellingrule = sellingrule;
    }

    public String getStatus() {
        return status;
    }
    public String getSotckname(){
        return stockname;
    }

    public Integer getInitialamount() {
        return initialamount;
    }

    public ArrayList<String> getBuyingrule() {
        return buyingrule;
    }

    public ArrayList<String> getSellingrule() {
        return sellingrule;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public void setInitialamount(Integer initialamount) {
        this.initialamount = initialamount;
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
