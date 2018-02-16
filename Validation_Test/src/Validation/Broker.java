package Validation;

//import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Broker {
	String name = "";
	Date previousOrder = new Date();
	int orderCount = 0;
	public static final int maxOrders = 3;
	Set<Integer> usedOrderIds = new HashSet<Integer>();
	
	Broker(String name){
		//sets the name of this broker object to a string name
		this.name = name;
	}
	
	String getName(){
		//returns the name of this broker
		return this.name;
	}
	
	Date getLastOrder() {
		//returns the date time stamp of the last valid order for this broker
		return this.previousOrder;
	}
	
	Boolean checkOrderId(Integer orderId) {
		//returns false if the order id for this broker has been used already
		return this.usedOrderIds.add(orderId);
	}
	
}
