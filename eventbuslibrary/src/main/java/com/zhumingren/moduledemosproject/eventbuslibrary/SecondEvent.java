package com.zhumingren.moduledemosproject.eventbuslibrary;

/**
 * Author    ZhuMingren
 * Date      2018/9/16
 * Time      上午1:08
 * DESC      com.zhumingren.moduledemosproject.eventbuslibrary
 */
public class SecondEvent {
    
    public int code = 0;
    
    private String message;
    
    public SecondEvent(String message){
        this.message=message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}