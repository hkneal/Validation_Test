package Validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
 
class HelperClass {
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm:ss");
	static Set<String> stockSet = new HashSet<String>();
	public static ArrayList<Broker> brokerList = new ArrayList<Broker>();
	public static ArrayList<Order> orderList = new ArrayList<Order>();	
	public static ArrayList<Order> validOrders = new ArrayList<Order>();
	public static ArrayList<Order> invalidOrders = new ArrayList<Order>();
	public static void generateStockSets() {
		
		
		File file = new File("Files/symbols.txt");
		try {

			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				stockSet.add(sc.next());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		 //System.out.println(stockSet);
		
		File brokerFile = new File("Files/firms.txt");
		try {

			Scanner sc = new Scanner(brokerFile);

			while (sc.hasNextLine()) {
				brokerList.add(new Broker(sc.nextLine()));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean validateSymbol(String symbol){
	//Checks if ticker symbol is a valid trading symbol
	    return stockSet.contains(symbol);
	}
	
	public static void createValidList() {
		File file = new File("Files/trades.csv");
		int invalidCount = 0;
		int validCount = 0;
		int totalCount = 0;
		try {
			//Separate the input file data by comma
			Scanner sc = new Scanner(file);
			
			//Skip the column titles
			sc.nextLine(); 
			
			while (sc.hasNextLine()) {
				totalCount++;
				String line = sc.nextLine();
			    String[] elements = line.split(",");
			    if(elements.length == 8) {
			    		orderList.add(new Order(LocalDateTime.parse(elements[0], formatter), elements[1], Integer.parseInt(elements[2]), elements[3], elements[4], Integer.parseInt(elements[5]), Float.parseFloat(elements[6]), elements[7]));
			    		} 
			    else {
			    		//Add order to invalid List
			    		invalidCount++;
			    		//invalidOrders.add(elements);
			    		System.out.println();
			    		System.out.print("Invalid Order: "); 
			    			for(int i=0; i<elements.length; i++) {
			    				System.out.print(elements[i] + " ");
			    			}
			    		System.out.print("\n");
			    		}
		    
		    }
			
//			while (sc.hasNextLine()) {
//				sc.useDelimiter(",|\n");
//				orderList.add(new Order(LocalDateTime.parse(sc.next(), formatter), sc.next(), sc.nextInt(), sc.next(), sc.next(), sc.nextInt(), sc.nextFloat(), sc.next()));
//				//System.out.println(sc.nextLine());	
//			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(Order order: orderList) {
			
			//check if order is valid 
			//Validate the ticker symbol
			if (HelperClass.validateSymbol(order.getSymbol())) {
				for(Broker broker: brokerList) {
					if(broker.name.equals(order.broker)) {
						//System.out.println("Found Broker: " + broker.name);
						//check if sequence id is valid
						if(broker.checkOrderId(order.sequenceID)) {
							//check number of orders on broker that are within the last minute 
							if(broker.checkPreviousOrders(order.orderTimeStamp)) {
								//add time stamp to broker array 
								validOrders.add(order);
								}
								else {
									invalidCount++;
									invalidOrders.add(order);
								}
							}
							else {
								invalidCount++;
								invalidOrders.add(order);
							}
						break;	
					}
				}
			} else {
				//System.out.println("Invalid Symbol: " + order.symbol);
				invalidCount++;
				invalidOrders.add(order);
				//add order to invalid list
			}
		}
		for(Order order : validOrders) {
			validCount ++;
			System.out.print(order.orderTimeStamp + " ");
			System.out.print(order.broker + " ");
			System.out.print(order.sequenceID + " ");
			System.out.print(order.type + " ");
			System.out.print(order.getSymbol() + " ");
			System.out.print(order.quantity + " ");
			System.out.print(order.price + " ");
			System.out.print(order.side + " \n");
		}
		System.out.println("Total Order Count: " + totalCount  + " Valid Order Count: " + validCount + " Invalid Order Count: " + invalidCount);
		System.out.println("Sum of valid and invalid: " + (invalidCount + validCount));
	}
	
} /*** end of helperClass **/
     