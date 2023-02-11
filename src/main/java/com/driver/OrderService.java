package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class OrderService
{
    //@Autowired
    OrderRepository orderRepository=new OrderRepository();

    public String addOrder(Order order)
    {
        return orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId)
    {
        orderRepository.addPartner(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId)
    {
        //This is basically assigning that order to that partnerId
        orderRepository.addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId)
    {
        //order should be returned with an orderId.
        return orderRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId)
    {
        //deliveryPartner should contain the value given by partnerId
        return orderRepository.getPartnerById(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId)
    {
        //orderCount should denote the orders given by a partner-id
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
        //orders should contain a list of orders by PartnerId
        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders()
    {
        //Get all orders
        return orderRepository.getAllOrders();
    }

    public int getCountOfUnassignedOrders()
    {
        //Count of orders that have not been assigned to any DeliveryPartner
        return orderRepository.getCountOfUnassignedOrders();
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        //Return the time when that partnerId will deliver his last delivery order.
        return orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
    }

    public void deletePartnerById(String partnerId)
    {

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.

        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId)
    {
        //Delete an order and also
        // remove it from the assigned order of that partnerId
        orderRepository.deleteOrderById(orderId);
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId)
    {
        //countOfOrders that are left after a particular time of a DeliveryPartner
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
    }
}
