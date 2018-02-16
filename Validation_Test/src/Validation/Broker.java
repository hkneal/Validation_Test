package Validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Broker {
	String name = "";
	int orderCount = 0;
	ArrayList<LocalDateTime> previousOrderTimes = new ArrayList<LocalDateTime>(3);
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
	
	Boolean checkPreviousOrders(LocalDateTime orderTimestamp) {
		//returns the date time stamp of the last valid order for this broker
		boolean added = false;
		if(this.orderCount < 3) {
			this.previousOrderTimes.add(orderTimestamp);
			this.orderCount++;
			added = true;
		}
		else {
			for(int i=0; i<3; i++) {
				//Assuming we didn't span more than 1 hour, else we'll check to be sure the minute passed is within the same hour/day
				if( orderTimestamp.getMinute() - this.previousOrderTimes.get(i).getMinute() > 1 ) {
					this.previousOrderTimes.set(i, orderTimestamp);
					added = true;
					break;
					}	
				}
			}
		return added;
		
	}
	
	Boolean checkOrderId(Integer orderId) {
		//returns false if the order id for this broker has been used already
		return this.usedOrderIds.add(orderId);
	}
	
}
