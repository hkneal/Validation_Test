package Validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Validation {
	/*
	 * 1. Only orders that have values for the fields of ‘broker’, ‘symbol’, ‘type’,
	 * ‘quantity’, ‘sequence id’, ‘side’, and ‘price’ should be accepted. 2. Only
	 * orders for symbols actually traded on the exchange should be accepted 3. Each
	 * broker may only submit three orders per minute: any additional orders in
	 * should be rejected 4. Within a single broker’s trades id's must be unique. If
	 * id's repeat for the same broker, only the first message with a given id should
	 * be accepted.
	 */
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm:ss");
	static Set<String> stockSet = new HashSet<String>();
	public static ArrayList<Broker> brokerList = new ArrayList<Broker>();
	public static ArrayList<Order> orderList = new ArrayList<Order>();	
	public static ArrayList<Order> validOrders = new ArrayList<Order>();
	public static ArrayList<Order> invalidOrders = new ArrayList<Order>();
	public static String invalidOrderStr = "";

	public static void main(String[] args) throws IOException {
		//Generate a set of valid stock symbols & a set of valid Broker objects
		generateStockSets();
		
		//Test to make sure proper Broker ArrayList has been generated.
		//for(Broker broker : brokerList) {
		//	System.out.println(broker.getName());
		//}
		
		//Test if order id is unique
		//System.out.println(brokerList.get(0).checkOrderId(1));
		//System.out.println(brokerList.get(0).checkOrderId(1));
		
		//Test to validate the ticker symbol against list of proper symbols
		//System.out.println(HelperClass.validateSymbol("YLLW"));

		createValidList();
		
		generateOutput();
		
		generateOutputJSON();
		
		//Allow the objects to be marked for garbage collection!
		cleanObjects();
		
	}
	
	public static void generateStockSets() {
		//Creates a SET of valid trading symbols and valid brokers
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
	
	public static void createValidList() throws IOException {
		//Reads the order transaction file and separates invalid orders
		int totalCount = 0, sequenceId = 0, quantity = 0; //Don't like setting seqId and quantity to 0
		LocalDateTime orderTimeStamp = null;
		String broker = null, type = null, symbol = null, side = null;
		float price = 0; //Need to fix this as well, don't like setting to 0
		
		FileInputStream fileStream = null;
		Scanner sc = null;
		try {
			//Separate the input file data by comma
			fileStream = new FileInputStream("Files/trades.csv");
			sc = new Scanner(fileStream, "UTF-8");
			
			//Skip the column titles
			sc.nextLine(); 
			
			while (sc.hasNextLine()) {
				totalCount++;
				String line = sc.nextLine();
			    String[] elements = line.split(",");
			    
			    //Validate order elements
			    if(elements.length == 8) {
			    	//Sufficient number for order parameters is 8
			    		try {
			    			orderTimeStamp = LocalDateTime.parse(elements[0], formatter);
			    			broker = elements[1];
			    			sequenceId = Integer.parseInt(elements[2]);
			    			type = elements[3];
			    			symbol = elements[4];
			    			quantity = Integer.parseInt(elements[5]);
			    			price = Float.parseFloat(elements[6]);
			    			side = elements[7];
			    		} catch (Exception e) {
			    			e.printStackTrace();
			    		}
			    		Order newOrder = new Order(orderTimeStamp, broker, sequenceId, type, symbol, quantity, price, side);
					    //Execute Order Validation
					    	if(!validateSymbol(newOrder.getSYMBOL())) {
					    		invalidOrders.add(newOrder);
					    }
					    	else if(!newOrder.validateOrder(brokerList)) {
					    			invalidOrders.add(newOrder);					    		
					    		}
					    	else {
					    		validOrders.add(newOrder);
					    	}
			    }
			    else {
			    		//Invalid order due to insufficient number of parameters, push this to invalid order list
			    	try {
		    			orderTimeStamp = LocalDateTime.parse(elements[0], formatter);
		    			broker = elements[1];
		    			sequenceId = Integer.parseInt(elements[2]);
		    			
		    			//Assumption is that only the side of order is missing or other parameter past sequenceID

		    		} catch (Exception e) {
		    			e.printStackTrace();
		    		}
			    		Order newOrder = new Order(orderTimeStamp, broker, sequenceId, elements);
			    		invalidOrders.add(newOrder);
			    }
			}
			//Scanner suppresses exceptions
			if(sc.ioException() != null) {
				throw sc.ioException();
			}
			
			} finally {
			    if (fileStream != null) {
			        fileStream.close();
			    }
			    if (sc != null) {
			        sc.close();
			    }
			} 

		System.out.println("Total Order Count: " + totalCount  + " Valid Order Count: " + validOrders.size() + " Invalid Order Count: " + invalidOrders.size());
		System.out.println("Sum of valid and invalid: " + (invalidOrders.size() + validOrders.size()));
	}
	
	public static void generateOutput() {
		//Outputs the valid and invalid transaction order files
		File validfile = new File("Files/valid_orders.txt");
		File invalidfile = new File("Files/invalid_orders.txt");
	    FileWriter writer = null;
	    try {
	        writer = new FileWriter(validfile);
	        writer.write("Broker : SequenceID \n");
	        for(Order order : validOrders) {
	        		writer.write(order.getBROKER() + " " + order.getSEQUENCE_ID() + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace(); 
	    } finally {
	        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
	    }
	    System.out.printf("File is located at %s%n", validfile.getAbsolutePath());
	    
	    //invalid orders
	    try {
	        writer = new FileWriter(invalidfile);
	        writer.write("Broker : SequenceID \n");
	        for(Order order : invalidOrders) {
	        		writer.write(order.getBROKER() + " " + order.getSEQUENCE_ID() + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace(); 
	    } finally {
	        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
	    }
	    System.out.printf("File is located at %s%n", invalidfile.getAbsolutePath());
	}
	
	public static void generateOutputJSON() {
		//Outputs the valid and invalid transaction order files
		File validfile = new File("Files/valid_orders.JSON");
		File invalidfile = new File("Files/invalid_orders.JSON");
	    FileWriter writer = null;
	    try {
	        writer = new FileWriter(validfile);
	        writer.write("{\n");
	        writer.write("\"orders\": [\n");
	        for(Order order : validOrders) {
	        		writer.write("{\"time-stamp\": " + "\"" + order.getTimeStamp() + "\", \"broker-name\": " + "\"" + order.getBROKER() + "\", \"sequence-id\": " + "\"" + order.getSEQUENCE_ID() + "\", \"order-type\": \"" + order.getTYPE() + "\", \"symbol\": " + "\"" + order.getSYMBOL() + "\", \"quantity\": \"" + order.getQUANTITY() + "\", \"price\": " + "\"" + order.getPRICE() + "\", \"side\": \"" + order.getSIDE() + "\""+ "},\n");
	        }
	        writer.write("]\n");
	        writer.write("}");
	    } catch (IOException e) {
	        e.printStackTrace(); 
	    } finally {
	        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
	    }
	    System.out.printf("File is located at %s%n", validfile.getAbsolutePath());
	    
	    //invalid orders
	    try {
	        writer = new FileWriter(invalidfile);
	        writer.write("{\n");
	        writer.write("\"orders\": [\n");
	        for(Order order : invalidOrders) {
	        		if(order.getFullOrder()) {
	        			writer.write("{\"time-stamp\": " + "\"" + order.getTimeStamp() + "\", \"broker-name\": " + "\"" + order.getBROKER() + "\", \"sequence-id\": " + "\"" + order.getSEQUENCE_ID() + "\", \"order-type\": \"" + order.getTYPE() + "\", \"symbol\": " + "\"" + order.getSYMBOL() + "\", \"quantity\": \"" + order.getQUANTITY() + "\", \"price\": " + "\"" + order.getPRICE() + "\", \"side\": \"" + order.getSIDE() + "\""+ "},\n");
	        		}
	        		else {
	        			String[] orderEntries = order.getOrderEntries();
	        			String[] orderParameters = {"type", "symbol", "quantity", "price", "side"};
	        			int orderEntriesLength = orderEntries.length;
	        			writer.write("{\"time-stamp\": " + "\"" + order.getTimeStamp() + "\", \"broker-name\": " + "\"" + order.getBROKER() + "\", \"sequence-id\": " + "\"" + order.getSEQUENCE_ID() + "\"");
	        			for(int i= 3; i< orderEntriesLength; i++) {
	        				writer.write(", \"" + orderParameters[i-3] + "\": \"" + orderEntries[i] + "\"");
	        			}
	        			writer.write("},\n");
	        		}
		       }
	        writer.write("]\n");
	        writer.write("}");
	        //writer.write("\n");
	    } catch (IOException e) {
	        e.printStackTrace(); 
	    } finally {
	        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
	    }
	    System.out.printf("File is located at %s%n", invalidfile.getAbsolutePath());
	}
	
	public static void cleanObjects() {
		//Allow the objects to be marked for garbage collection!
	    validOrders = new ArrayList<Order>();
		invalidOrders = new ArrayList<Order>();
	}
	
}

