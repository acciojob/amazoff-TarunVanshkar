package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime)
    {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        this.deliveryTime=timeConvertToMinutes(deliveryTime);
    }

    private int timeConvertToMinutes(String deliveryTime)
    {
        String []time=deliveryTime.split(":");
        int hours=Integer.valueOf(time[0]);
        int minutes=Integer.valueOf(time[1]);
        int hoursInMinutes=hours*60;
        return hoursInMinutes+minutes;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
