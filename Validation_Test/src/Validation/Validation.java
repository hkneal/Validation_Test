package Validation;

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

	public static void main(String[] args) {
		//Generate a set of valid stock symbols & a set of valid Broker objects
		HelperClass.generateStockSets();
		
		//Test to make sure proper Broker ArrayList has been generated.
		//for(Broker broker : HelperClass.brokerList) {
		//	System.out.println(broker.getName());
		//}
		
		//Test if order id is unique
		//System.out.println(HelperClass.brokerList.get(0).checkOrderId(1));
		//System.out.println(HelperClass.brokerList.get(0).checkOrderId(1));
		
		//Test to validate the ticker symbol against list of proper symbols
		//System.out.println(HelperClass.validateSymbol("YLLW"));

		HelperClass.createValidList();
		
		HelperClass.generateOutput();
		
		
	}
}
