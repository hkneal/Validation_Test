package Validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Broker {
	private String name;
	private int orderCount;
	private ArrayList<LocalDateTime> previousOrderTimes = new ArrayList<LocalDateTime>(3);
	private Set<Integer> usedOrderIds = new HashSet<Integer>();
	
	public Broker(String name){
		//sets the name of this broker object to a string name
		this.name = name;
	}
	
	public String getName(){
		//returns the name of this broker
		return this.name;
	}
	
	public Boolean checkPreviousOrders(LocalDateTime orderTimestamp) {
		//returns boolean if order isn't third within an hour, it checks the date time stamp of the last valid order for this broker
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
	
	public Boolean checkOrderId(Integer orderId) {
		//returns false if the order id for this broker has been used already
		return this.usedOrderIds.add(orderId);
	}
	
}
