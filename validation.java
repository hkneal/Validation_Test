import java.io.*;

class Validation{
/*  1. Only orders that have values for the fields of ‘broker’, ‘symbol’, ‘type’, 
        ‘quantity’, ‘sequence id’, ‘side’, and ‘price’ should be accepted.
    2. Only orders for symbols actually traded on the exchange should be accepted
    3. Each broker may only submit three orders per minute: any additional orders in  should be rejected
    4. Within a single broker’s trades ids must be unique. If ids repeat for the same broker, 
        only the first message with a given id should be accepted.
*/

    void checkFields(){
        //This method splits the list between valid and invalid orders based on required fields

    }

    boolean validateSymbol(){
        return false;
    }

    void checkTimeStamps(){
        
    }

    public static void main(string[] args) {
        
    }
}
