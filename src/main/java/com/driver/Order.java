package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime)
    {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        int HH = Integer.valueOf(deliveryTime.substring(0,2));
        int MM = Integer.valueOf(deliveryTime.substring(3));    // Convert String to int
        this.id=id;
        this.deliveryTime=HH*60+MM;
    }


    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;}
}
