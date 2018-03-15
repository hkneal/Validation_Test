package Validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
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
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm:ss");
	private static Set<String> stockSet = new HashSet<String>();
	private static ArrayList<Broker> brokerList = new ArrayList<Broker>();
	private static ArrayList<Order> validOrders = new ArrayList<Order>();
	private static ArrayList<Order> invalidOrders = new ArrayList<Order>();

	public static void main(String[] args) throws IOException {
		//Measure Elapse Time 
		long startTimeStamp = System.currentTimeMillis();
		
		//Generate a set of valid stock symbols & a set of valid Broker objects
		generateStockSets();
		
		//Test to make sure proper Broker ArrayList has been generated.
		//for(Broker broker : brokerList) {
		//	System.out.println(broker.getName());
		//}
		
		//Test if order id is unique
		//System.out.println(brokerList.get(0).checkOrderId(1));
		
		//Test to validate the ticker symbol against list of proper symbols
		//System.out.println(HelperClass.validateSymbol("YLLW"));

		createValidList();
		
		generateOutput();
		
		generateOutputJSON();
		
		//Allow the objects to be marked for garbage collection!
		cleanObjects();
		
		//Ending of main logic time stamp 
		long endTimeStamp = System.currentTimeMillis();
		
		//Simple elapsed time measurement
		System.out.print("Total Elapsed Time in Milliseconds: ");
		System.out.print(endTimeStamp - startTimeStamp);
	}
	
	public static void generateStockSets() {
		//Creates a SET of valid trading symbols and valid list of brokers 
		//File file = new File("Files/symbols.txt");
		
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("Files/symbols.txt")));
			
			String line;
			while ((line = br.readLine()) != null) {
				stockSet.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("Files/firms.txt")));
			
			String line;
			while ((line = br.readLine()) != null) {
				brokerList.add(new Broker(line));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean validateSymbol(String symbol){
		//Checks if ticker symbol is a valid trading symbol
		    return stockSet.contains(symbol);
		}
	
	public static void createValidList() throws IOException {
		//Reads the order transaction file and separates invalid orders
		int totalCount = 0, sequenceId = 0, quantity = 0; 
		LocalDateTime orderTimeStamp = null;
		String broker = null, type = null, symbol = null, side = null;
		float price = 0; 

		try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("Files/trades.csv")));
				//Skip the column titles
				br.readLine(); 
				
				String line;
				
				while ((line = br.readLine()) != null) {
					totalCount++;
					
					//Order parameters are separated by comma
					String[] elements = line.split(",");

				    //Validate order elements
				    if(elements.length == 8) {
				    	
				    	//Sufficient number of order parameters is 8
		    			orderTimeStamp = LocalDateTime.parse(elements[0], formatter);
		    			broker = elements[1];
		    			sequenceId = Integer.parseInt(elements[2]);
		    			type = elements[3];
		    			symbol = elements[4];
		    			quantity = Integer.parseInt(elements[5]);
		    			price = Float.parseFloat(elements[6]);
		    			side = elements[7];
				    		
			    		Order newOrder = new Order(orderTimeStamp, broker, sequenceId, type, symbol, quantity, price, side);
					    //Execute Order Validation
					    	if(!validateSymbol(newOrder.getSYMBOL())) {
					    		//Write to invalid text file instead of array list
					    		invalidOrders.add(newOrder);
					    }
					    	else if(!newOrder.validateOrder(brokerList)) {
					    			invalidOrders.add(newOrder);					    		
					    		}
					    	else {
					    		//Write to valid text file instead of array list
					    		validOrders.add(newOrder);
					    	}
				    }  
				    else {  //less than 8 parameters
				    	
				    		//Invalid order due to insufficient number of parameters, push this to invalid order list
			    			orderTimeStamp = LocalDateTime.parse(elements[0], formatter);
			    			broker = elements[1];
			    			sequenceId = Integer.parseInt(elements[2]);
			    			
			    			//Assumption is that only the side of order is missing or other parameter past sequenceID
				    		Order newOrder = new Order(orderTimeStamp, broker, sequenceId, elements);
				    		invalidOrders.add(newOrder);
				    }
				}  //end of while
				br.close();
				
			} catch (IOException e) {
				e.printStackTrace();
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
	        		writer.write("{\"time-stamp\": " + "\"" + order.getTimeStamp().format(formatter) + "\", \"broker-name\": " + "\"" + order.getBROKER() + "\", \"sequence-id\": " + "\"" + order.getSEQUENCE_ID() + "\", \"order-type\": \"" + order.getTYPE() + "\", \"symbol\": " + "\"" + order.getSYMBOL() + "\", \"quantity\": \"" + order.getQUANTITY() + "\", \"price\": " + "\"" + order.getPRICE() + "\", \"side\": \"" + order.getSIDE() + "\""+ "},\n");
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
	        			writer.write("{\"time-stamp\": " + "\"" + order.getTimeStamp().format(formatter) + "\", \"broker-name\": " + "\"" + order.getBROKER() + "\", \"sequence-id\": " + "\"" + order.getSEQUENCE_ID() + "\", \"order-type\": \"" + order.getTYPE() + "\", \"symbol\": " + "\"" + order.getSYMBOL() + "\", \"quantity\": \"" + order.getQUANTITY() + "\", \"price\": " + "\"" + order.getPRICE() + "\", \"side\": \"" + order.getSIDE() + "\""+ "},\n");
	        		}
	        		else {
	        			String[] orderEntries = order.getOrderEntries();
	        			String[] orderParameters = {"type", "symbol", "quantity", "price", "side"};
	        			int orderEntriesLength = orderEntries.length;
	        			writer.write("{\"time-stamp\": " + "\"" + order.getTimeStamp().format(formatter) + "\", \"broker-name\": " + "\"" + order.getBROKER() + "\", \"sequence-id\": " + "\"" + order.getSEQUENCE_ID() + "\"");
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

