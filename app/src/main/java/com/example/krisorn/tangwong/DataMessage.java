package com.example.krisorn.tangwong;

import java.util.HashMap;
import java.util.Map;
public class DataMessage {
    public  String to;
    public Map<String,String> data;

    public DataMessage()
    {

    }

    public DataMessage(String to,Map<String,String> data)
    {
        this.to = to;
        this.data = data;
    }

    public String getTo()
    {
        return  to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public Map<String,String> getData()
    {
        return data;
    }


}
