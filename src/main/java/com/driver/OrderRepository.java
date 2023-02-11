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

    HashMap<String, List<Order>> orderPartnerPair=new HashMap<>();   // partnerId as key and list of orderIds
    int ordersAssigned=0;

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
        ordersAssigned++;
        //This is basically assigning that order to that partnerId

        //add in the pair
//        List<Order> currentOrder = orderPartnerPair.getOrDefault(partnerId,new ArrayList<Order>());
//        currentOrder.add(ordersMap.get(orderId));
//        orderPartnerPair.put(partnerId,currentOrder);

        List<Order> currentOrder=new ArrayList<>();
        if(orderPartnerPair.containsKey(partnerId))
        {
            currentOrder=orderPartnerPair.get(partnerId);
            currentOrder.add(ordersMap.get(orderId));
            orderPartnerPair.put(partnerId, currentOrder);
        }
        else
        {
            //List<Order> currentOrder=new ArrayList<>();
            currentOrder.add(ordersMap.get(orderId));
            orderPartnerPair.put(partnerId, currentOrder);
        }

        DeliveryPartner partner = partnerMap.get(partnerId);
        partner.setNumberOfOrders(currentOrder.size());
//        int orders = partner.getNumberOfOrders()+1;
//        partner.setNumberOfOrders(orders);   // Increased order count of partner
    }

    public Order getOrderById(String orderId)
    {
        //order should be returned with an orderId.

        return ordersMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId)
    {
        //deliveryPartner should contain the value given by partnerId

        return partnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId)
    {
        //orderCount should denote the orders given by a partner-id

        //return partnerMap.get(partnerId).getNumberOfOrders();
        int count=0;
        if(orderPartnerPair.containsKey(partnerId))
        {
            count=orderPartnerPair.get(partnerId).size();
        }
        return count;
    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
        List<String> orders = new ArrayList<>();
        for(Order currOrder:orderPartnerPair.get(partnerId))
        {
            orders.add(currOrder.getId());
        }

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
        int count = ordersMap.size()-ordersAssigned;
        //Count of orders that have not been assigned to any DeliveryPartner

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        //Return the time when that partnerId will deliver his last delivery order.

        int lastTime=0;
        for(Order order:orderPartnerPair.get(partnerId))
        {
            if(lastTime<order.getDeliveryTime())
            {
                lastTime=order.getDeliveryTime();   // hene we will get max time
            }
        }

        String time=convertTime(lastTime);
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

        partnerMap.remove(partnerId);
        List<Order> orderList=orderPartnerPair.get(partnerId);
        for(Order order:orderList)
        {
            ordersMap.remove(order);
            ordersAssigned--;
        }
        orderPartnerPair.remove(partnerId);

    }

    public void deleteOrderById(String orderId)
    {
        //Delete an order and also
        // remove it from the assigned order of that partnerId

        ordersMap.remove(orderId);

        for(String currPartnerId: orderPartnerPair.keySet())
        {
            List<Order> currOrders=orderPartnerPair.get(currPartnerId);
            if(currOrders.contains(orderId))
            {
                currOrders.remove(orderId);
                ordersAssigned--;
            }
        }

    }


    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId)
    {

        int count = 0;

        //countOfOrders that are left after a particular time of a DeliveryPartner

        int HH = Integer.valueOf(time.substring(0,2));
        int MM = Integer.valueOf(time.substring(3));
        int deliveryTime=HH*60+MM;

        for(Order order:orderPartnerPair.get(partnerId))
        {
            if(deliveryTime < order.getDeliveryTime())     // here order.getDeliveryTime() is a required time to order get delivered
            {
                count++;
            }
        }
        return count;
    }

}
