package com.example.life_safe_with_real_location;

public class IdHulder {


    public IdHulder() {

    }
    public IdHulder(String u_id, String who) {
        this.u_id = u_id;
        this.who = who;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public static String u_id;


    public static String getWho() {
        return who;
    }

    public static void setWho(String who) {
        IdHulder.who = who;
    }

    public static String who;
}
