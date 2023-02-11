package com.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository
{
    HashMap<String, Order> ordersMap=new HashMap<>();    // To store order object with its order id as key
    HashMap<String, DeliveryPartner> partnerMap=new HashMap<>();

    HashMap<String, List<String>> orderPartnerPair=new HashMap<>();   // partnerId as key and list of orderIds

    public String addOrder(Order order)
    {
        ordersMap.put(order.getId(), order);
        return "New order added successfully";
    }

    public void addPartner(String partnerId)
    {
        // Since we have not got the partner object so, first we will create a partner object
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
        partnerMap.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId)
    {
        //This is basically assigning that order to that partnerId
        if(orderPartnerPair.containsKey(partnerId))
        {
            orderPartnerPair.get(partnerId).add(orderId);
        }
        else
        {
            List<String> orderList=new ArrayList<>();
            orderList.add(orderId);
            orderPartnerPair.put(partnerId, orderList);
        }
    }

    public Order getOrderById(String orderId)
    {
        //order should be returned with an orderId.

        return ordersMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId)
    {

        DeliveryPartner deliveryPartner = partnerMap.get(partnerId);

        //deliveryPartner should contain the value given by partnerId

        return deliveryPartner;
    }

    public int getOrderCountByPartnerId(String partnerId)
    {

        Integer orderCount = orderPartnerPair.get(partnerId).size();

        //orderCount should denote the orders given by a partner-id

        return orderCount;
    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
        List<String> orders = orderPartnerPair.get(partnerId);

        //orders should contain a list of orders by PartnerId

        return orders;
    }

    public List<String> getAllOrders()
    {
        List<String> allOrdersList=new ArrayList<>();
        for(String currentOrder:ordersMap.keySet())
        {
            allOrdersList.add(currentOrder);
        }
        //Get all orders
        return allOrdersList;
    }

    public int getCountOfUnassignedOrders()
    {
        int count = 0;
        //Count of orders that have not been assigned to any DeliveryPartner

        for(String currentPartner:orderPartnerPair.keySet())
        {
            List<String> currOrderList=orderPartnerPair.get(currentPartner);

            for(String currOrders: currOrderList)
            {
               if(!ordersMap.containsKey(currOrders))
               {
                   count++;
               }
            }
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        //Return the time when that partnerId will deliver his last delivery order.

        String time="";
        List<String> deliveryList=orderPartnerPair.get(partnerId);
        String lastDeliveryId=deliveryList.get(deliveryList.size()-1);

        int minutes=ordersMap.get(lastDeliveryId).getDeliveryTime();
        time=convertTime(minutes);
        return time;
    }

    private String convertTime(int minutes)
    {
        int hrs=minutes/60;
        int mins=minutes%60;

        String time="";
        if(hrs<9)
        {
            time+="0"+String.valueOf(hrs)+":";
        }
        else
        {
            time+=String.valueOf(hrs)+":";
        }

        if(mins<9)
        {
            time+="0"+String.valueOf(mins);
        }
        else
        {
            time+=String.valueOf(mins);
        }
        return time;
    }

    public void deletePartnerById(String partnerId)
    {
        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.

        partnerMap.remove(partnerId);
        orderPartnerPair.remove(partnerId);
    }

    public void deleteOrderById(String orderId)
    {
        //Delete an order and also
        // remove it from the assigned order of that partnerId

        ordersMap.remove(orderId);

        for(String currPartnerId: orderPartnerPair.keySet())
        {
            List<String> currOrders=orderPartnerPair.get(currPartnerId);
            if(currOrders.contains(orderId))
            {
                currOrders.remove(orderId);
            }
        }
    }


    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId)
    {

        int count = 0;

        //countOfOrders that are left after a particular time of a DeliveryPartner

        int currTime=timeConvertToMinutes(time);

        List<String> orderList=orderPartnerPair.get(partnerId);
        for(String order:orderList)
        {
            if(ordersMap.get(order).getDeliveryTime()>currTime)
            {
                count++;
            }
        }
        return count;
    }

    private int timeConvertToMinutes(String deliveryTime)
    {
        String []time=deliveryTime.split(":");
        int hours=Integer.valueOf(time[0]);
        int minutes=Integer.valueOf(time[1]);
        int hoursInMinutes=hours*60;
        return hoursInMinutes+minutes;
    }
}
