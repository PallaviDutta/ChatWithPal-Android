package com.kpf.sujeet.chat.Models;

/**
 * Created by user pc on 12/29/2016.
 */

public class User {

    public String name="";
    public String email="";
    public String mobno="";
    public String country="";
    public String uid="";
    public String photoUrl = "";

    public User(){

    }
    public User(String name, String email, String mobno, String uid,String photoUrl){
        this.name=name;
        this.email=email;
        this.mobno=mobno;
        this.uid=uid;
        this.photoUrl=photoUrl;

    }


}
