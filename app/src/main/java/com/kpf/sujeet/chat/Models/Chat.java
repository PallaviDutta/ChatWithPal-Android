package com.kpf.sujeet.chat.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user pc on 1/13/2017.
 */

public class Chat {
    public String photo_url = "";
    public String name ="";
    public String author = "";
    public String message = "";
    public String timeStamp = "";
    public boolean isMine=false;

    public Chat(){

    }
    public Chat(String message, String author, String timeStamp, boolean isMine){


        this.message = message;
        this.author = author;
        this.timeStamp = timeStamp;
        this.isMine = isMine;

    }

    @Exclude
    public Map<String,Object>toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("message",message);
        result.put("author",author);
        result.put("timeStamp",timeStamp);
        result.put("isMine",isMine);

        return result;

    }

}
