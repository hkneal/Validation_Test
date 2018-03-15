package Validation;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Order { 
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm:ss");
	private final LocalDateTime ORDER_TIME_STAMP;
	private final String BROKER, SYMBOL, TYPE, SIDE;
	private final int QUANTITY, SEQUENCE_ID;
	private final float PRICE;
	private final String[] orderEntries;
	private final boolean FULL_ORDER; //Used to determine if all parameters were supplied

	//constructor
	Order(LocalDateTime ORDER_TIME_STAMP, String BROKER, int SEQUENCE_ID, String TYPE, String SYMBOL, int QUANTITY, float PRICE, String SIDE){
		this.ORDER_TIME_STAMP = ORDER_TIME_STAMP;
		this.BROKER = BROKER;
		this.SYMBOL = SYMBOL;
		this.TYPE = TYPE;
		this.SIDE = SIDE;
		this.SEQUENCE_ID = SEQUENCE_ID;
		this.QUANTITY = QUANTITY;
		this.PRICE = PRICE;
		this.FULL_ORDER = true;
		this.orderEntries = null;
	}
	
	Order(LocalDateTime ORDER_TIME_STAMP, String BROKER, int SEQUENCE_ID, String[] orderEntries){
		this.orderEntries = orderEntries;
		this.ORDER_TIME_STAMP = ORDER_TIME_STAMP;
		this.BROKER = BROKER;
		this.SYMBOL = null;
		this.TYPE = null;
		this.SIDE = null;
		this.SEQUENCE_ID = SEQUENCE_ID;
		this.QUANTITY = 0;
		this.PRICE = 0;
		this.FULL_ORDER = false;
	}
	
	//getters and setters
	public String[] getOrderEntries() {
		return this.orderEntries;
	}
	
	public LocalDateTime getTimeStamp(){
		return this.ORDER_TIME_STAMP;
	}
	
	public String getSYMBOL(){
		return this.SYMBOL;
	}
	
	public String getBROKER(){
		return this.BROKER;
	}
	
	public String getTYPE(){
		return this.TYPE;
	}
	
	public String getSIDE(){
		return this.SIDE;
	}
	
	public int getQUANTITY(){
		return this.QUANTITY;
	}
	
	public int getSEQUENCE_ID(){
		return this.SEQUENCE_ID;
	}
	
	public float getPRICE(){
		return this.PRICE;
	}
	
	public boolean getFullOrder(){
		return this.FULL_ORDER;
	}
	
	public boolean validateOrder(ArrayList<Broker> brokerList) {
		boolean orderValid = false;
		
		//Pull valid broker by name and validate sequence Id's used and last 3 time stamps for needed broker
//		Broker orderBroker = brokerList.stream()
//				.filter((b) -> this.BROKER.equals(b.getName()))
//				.findAny()
//				.orElse(null);
		
		//Validate a proper sequence id is being used,
		//check number of orders on broker that are within the last minute And
//		try {
//			if(orderBroker.checkOrderId(this.SEQUENCE_ID) && orderBroker.checkPreviousOrders(this.ORDER_TIME_STAMP)) {
//					orderValid = true;
//					}
//		} catch (NullPointerException e) {
//			//Invalid Broker Name, sequence_Id, or orderTimeStamp on orderBroker, return with false valid order validation
//		}
		
// Traditional method seems to run about 10% quicker in this implementation
		loop: for(Broker broker: brokerList) {
			if(broker.getName().equals(this.BROKER)) {
				Broker orderBroker = broker;
				try {
					if(orderBroker.checkOrderId(this.SEQUENCE_ID) && orderBroker.checkPreviousOrders(this.ORDER_TIME_STAMP)) {
							orderValid = true;
							return true;
						}
				} catch (NullPointerException e) {
					//Invalid Broker Name, sequence_Id, or orderTimeStamp on orderBroker, return with false valid order validation
				}
				break loop;
			}
		}
		
		return orderValid;
	}
}
