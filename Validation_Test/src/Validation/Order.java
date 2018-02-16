package Validation;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Order {
	LocalDateTime orderTimeStamp = null;
	String broker = "";
	String symbol = "";
	String type = "";
	int quantity;
	String side = "";
	int sequenceID;
	float price;

	Order(LocalDateTime orderTimeStamp, String broker, int sequenceID, String type, String symbol, int quantity, float price, String side){
		this.orderTimeStamp = orderTimeStamp;
		this.broker = broker;
		this.symbol = symbol;
		this.type = type;
		this.side = side;
		this.sequenceID = sequenceID;
		this.quantity = quantity;
		this.price = price;
	}
	
	String getSymbol(){
		return this.symbol;
	}
}
