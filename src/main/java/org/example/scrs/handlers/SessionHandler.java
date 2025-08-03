package org.example.scrs.handlers;

import org.example.scrs.Enums.USERTYPE;

public class SessionHandler {
    public static int userid;
    public static USERTYPE usertype;
    public static void endSession(){
        userid=0;
        usertype=null;
    }
    public static void startSession(int id,USERTYPE type){
        userid=id;
        usertype=type;
    }

    public static int getUserid() {
        return userid;
    }

    public static USERTYPE getUsertype() {
        return usertype;
    }
}
