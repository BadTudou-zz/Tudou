package com.badtudou.model;

/**
 * Created by badtudou on 26/04/2017.
 */

public class Contacts {
    public long id;
    public String dispalyname;
    public String phone;
    public String email;

    public Contacts(){
    }

    public void set(long id, String dispalyname, String phone, String email){
        this.id = id;
        this.dispalyname = dispalyname;
        this.phone = phone;
        this.email = email;
    }
}
