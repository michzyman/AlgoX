

/*

package com.example.testmichelle.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class UserAccount {

    public static FirebaseDatabase database;
    public static FirebaseAuth auth;
    public static FirebaseUser user;

    public static UserAccount account;
    public OnTaskCompleted listener;

    //Used for singleton constructor
    private UserAccount(){
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user =  auth.getCurrentUser();
    }
    public static void signOut(){
        database = null;
        user = null;
        account = null;
        auth = null;
    }
    public static boolean userIsLogin(){
        return user != null;
    }

    //Returns singleton instance
    public static UserAccount getInstance() {
        if(account == null){
            account = new UserAccount();
        }
        return account;
    }
    public String getUserName(){
        String str = user.getEmail();
        str = str.substring(0,str.indexOf("@"));
        return str;
    }
}
*/
