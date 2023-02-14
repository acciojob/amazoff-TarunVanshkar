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
    Map<String, Order> orderDB;                         // <Id, Order>
    Map<String, DeliveryPartner> deliveryPartnerDB;     //  <Id, DeliveryPartner>
    Map<String, List<String>> pairDB;                   // <PartnerId, List<OrderID>
    Map<String, String> assignedDB;                     // <OrderId, PartnerId>

    // Constructor
    public OrderRepository()
    {
        this.orderDB = new HashMap<>();
        this.deliveryPartnerDB = new HashMap<>();
        this.pairDB = new HashMap<>();
        this.assignedDB = new HashMap<>();
    }

    public void addOrder(Order order)
    {
        orderDB.put(order.getId(), order);
    }

    public void addPartner(String partnerId)
    {
        // Since we have not got the partner object so, first we will create a partner object
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
        deliveryPartnerDB.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId)
    {
        //This is basically assigning that order to that partnerId
        //add in the pair

        // Update or create pairDB
        List<String> orderList=pairDB.getOrDefault(partnerId, new ArrayList<>());
        orderList.add(orderId);
        pairDB.put(partnerId, orderList);

        //Update assignedDB
        assignedDB.put(orderId, partnerId);

        // Increase/Update the count of NumberOfOrder
        DeliveryPartner deliveryPartner=deliveryPartnerDB.get(partnerId);
        deliveryPartner.setNumberOfOrders(orderList.size());
    }

    public Order getOrderById(String orderId)
    {
        //order should be returned with an orderId.
        if(orderDB.containsKey(orderId))
        {
            return orderDB.get(orderId);
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId)
    {
        //deliveryPartner should contain the value given by partnerId
        if(deliveryPartnerDB.containsKey(partnerId))
        {
            return deliveryPartnerDB.get(partnerId);
        }
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId)
    {
        //orderCount should denote the orders given by a partner-id
        return pairDB.getOrDefault(partnerId, new ArrayList<>()).size();   // pairDB has list of orders mapped by partnerId
    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
        //orders should contain a list of orders by PartnerId
        return pairDB.getOrDefault(partnerId, new ArrayList<>());  // returning list has orderId list
    }

    public List<String> getAllOrders()
    {
        List<String> allOrdersList=new ArrayList<>();
        for(String currentOrder:orderDB.keySet())
        {
            allOrdersList.add(currentOrder);
        }
        //Get all orders
        return allOrdersList;
    }

    public int getCountOfUnassignedOrders()
    {
        //Count of orders that have not been assigned to any DeliveryPartner
        return orderDB.size() - assignedDB.size();
    }


    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId)
    {
        //countOfOrders that are left after a particular time of a DeliveryPartner
        int count = 0;

        // Convert String time to int
        int HH = Integer.valueOf(time.substring(0,2));
        int MM = Integer.valueOf(time.substring(3));
        int deliveryTime=HH*60+MM;

        //Fetch assigned order list
        List<String> orderList = pairDB.getOrDefault(partnerId, new ArrayList<>());
        for(String currOrder:orderList)
        {
            Order order=orderDB.get(currOrder);
            if(order.getDeliveryTime() > deliveryTime)
            {
                count++;
            }
        }
        return count;   // Count of undelivered orders
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        //Return the time when that partnerId will deliver his last delivery order.
        String time="";

        // Fetch order list by partnerId
        List<String> orderList = pairDB.getOrDefault(partnerId, new ArrayList<>());
        int deliverTime=0;

        // get max of deliveryTime
        for(String currOrder: orderList)
        {
            Order order = orderDB.get(currOrder);
            if(order.getDeliveryTime() > deliverTime)
            {
                deliverTime=order.getDeliveryTime();     //// hence we will get max time
            }
        }
        time=convertTime(deliverTime);
        return time;
    }

    private String convertTime(int minutes)
    {
        int hrs=minutes/60;
        int mins=minutes%60;

        String time="";
        if(hrs<=9)
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

        // Update deliverPartnerDB
        deliveryPartnerDB.remove(partnerId);

        // Fetch orderList from pairDB
        List<String> orderList=pairDB.getOrDefault(partnerId, new ArrayList<>());

        // Update assignedDB  <orderId, partnerId>
        for(String currOrder: orderList)
        {
            assignedDB.remove(currOrder);
        }

        // Update pairDB
        pairDB.remove(partnerId);
    }

    public void deleteOrderById(String orderId)
    {
        //Delete an order and also
        // remove it from the assigned order of that partnerId

        // Update orderDB
        orderDB.remove(orderId);

        // get the assigned partnerId with this partnerId from assignedDB
        String partnerId=assignedDB.get(orderId);

        // Update assignedDB
        assignedDB.remove(orderId);

        // Remove this orderID from pairDB
        List<String> orderList = pairDB.get(partnerId);
        for(String currOrder: orderList)
        {
            if(currOrder.equals(orderId))
            {
                orderList.remove(currOrder);
            }
        }

        // Update the pairDB with deleted orderId
        pairDB.put(partnerId, orderList);
    }




}
