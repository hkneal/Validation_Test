package Validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Validation {
	/*
	 * 1. Only orders that have values for the fields of ‘broker’, ‘symbol’, ‘type’,
	 * ‘quantity’, ‘sequence id’, ‘side’, and ‘price’ should be accepted. 2. Only
	 * orders for symbols actually traded on the exchange should be accepted 3. Each
	 * broker may only submit three orders per minute: any additional orders in
	 * should be rejected 4. Within a single broker’s trades ids must be unique. If
	 * ids repeat for the same broker, only the first message with a given id should
	 * be accepted.
	 */

	void checkFields() {
		// This method splits the list between valid and invalid orders based on
		// required fields
		// And creates Broker objects or Hashmap while running validateSymbol and
		// checkTimestamps methods

	}

	void checkTimeStamps() {

	}

	public static void main(String[] args) {
		//1st calls a helper class to generate a set of valid stock symbols & a set of valid Broker objects
		HelperClass.generateStockSets();
		
		//Test to make sure proper Broker ArrayList has been generated.
		//for(Broker broker : HelperClass.brokerList) {
		//	System.out.println(broker.getName());
		//}
		
		//Test if order id is unique
		//System.out.println(HelperClass.brokerList.get(0).checkOrderId(1));
		//System.out.println(HelperClass.brokerList.get(0).checkOrderId(1));

		HelperClass.createValidList();
		
		//Used to validate the ticker symbol
		//System.out.println(HelperClass.validateSymbol("YLLW"));
		
		//System.out.println(HelperClass.isPalindrome("AlanalA"));
	}
}
