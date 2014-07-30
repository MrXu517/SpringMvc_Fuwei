package com.fuwei.constant;

public class OrderStatusUtil {
	public static OrderStatus get(int status){
    	OrderStatus[] statuses = OrderStatus.values();
    	for(OrderStatus OrderStatus : statuses){
    		if(OrderStatus.ordinal() == status){
    			return OrderStatus;
    		}
    	}
    	return null;
    }
    public static OrderStatus getNext(int status){
    	OrderStatus[] statuses = OrderStatus.values();
    	for(int i = 0 ; i<statuses.length ; ++i){
    		OrderStatus OrderStatus = statuses[i];
    		if(OrderStatus.ordinal() == status){
    			if(i+1 < statuses.length){
    				return statuses[i+1];
    			}else{
    				return null;
    			}
    		}
    	}
    	return null;
    }
}
