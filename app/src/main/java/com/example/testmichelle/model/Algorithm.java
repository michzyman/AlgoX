package com.example.testmichelle.model;

import android.hardware.lights.LightState;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {
    public boolean status;
    public String stockname;
    public Integer initialamount;
    public ArrayList<String> buyingrule;
    public ArrayList<String> sellingrule;
    public String start_date, end_date;
    public String algoname;

    public Algorithm(){
        //Empty constructor for firebase
    }
    public Algorithm(boolean status, String stockname, Integer initialamount,
                     ArrayList<String> buyingrule, ArrayList<String> sellingrule, String start_date, String end_date, String algoname) {
        this.status = status;
        this.stockname = stockname;
        this.initialamount = initialamount;
        this.buyingrule = buyingrule;
        this.sellingrule = sellingrule;
        this.end_date = end_date;
        this.start_date = start_date;
        this.algoname = algoname;
    }

    /*-------------------CREATING GET FUNCTIONS FOR ALL THE VARIABLES------------------- */
    public boolean getStatus(){
        return status;
    }
    public String getStockname() {
        return stockname;
    }
    public ArrayList<String> getBuyingrule() {
        return buyingrule;
    }
    public ArrayList<String> getSellingrule() {
        return sellingrule;
    }
    public String getStart_date(){
        return start_date;
    }
    public String getEnd_date() {
        return end_date;
    }
    public void getAlgoname(String name){
        this.algoname = name;
    }
    public void getInitialamount(Integer initialamount){
        this.initialamount = initialamount;
    }



    /*-------------------CREATING SET FUNCTIONS FOR ALL THE VARIABLES------------------- */
    public void setStatus(boolean status) {
        this.status = status;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
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
    public void setAlgoname(String name){
        this.algoname = name;
    }

}