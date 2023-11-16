package com.ss.tools.show;

public class message {
    public String MyName;
    public String DuiName;
    public String content;
    public String currentTime;
    public String messageType;

    public String getMyName() {
        return MyName;
    }


    public String getDuiName() {
        return DuiName;
    }


    public String getContent() {
        return content;
    }

    public String getCurrentTime() {
        return currentTime;
    }
    public String getMessageType() {
        return messageType;
    }

    public message(String MyName, String DuiName,  String messageType,String content, String currentTime){
        this.MyName=MyName;
        this.DuiName=DuiName;
        this.content=content;
        this.currentTime=currentTime;
        this.messageType=messageType;
    }

}
