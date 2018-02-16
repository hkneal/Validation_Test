package Validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
 
class HelperClass {
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm:ss");
	static Set<String> stockSet = new HashSet<String>();
	public static ArrayList<Broker> brokerList = new ArrayList<Broker>();
	public static ArrayList<Order> orderList = new ArrayList<Order>();	
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
		ArrayList<String> ordersArray = new ArrayList<>();
		File file = new File("Files/trades.csv");
		int invalidCount = 0;
		try {
			//Separate the input file data by comma
			Scanner sc = new Scanner(file);
			
			//Skip the column titles
			sc.nextLine(); 
			
			while (sc.hasNextLine()) {
			String line = sc.nextLine();
		    String[] elements = line.split(",");
		    if(elements.length == 8) {
		    		orderList.add(new Order(LocalDateTime.parse(elements[0], formatter), elements[1], Integer.parseInt(elements[2]), elements[3], elements[4], Integer.parseInt(elements[5]), Float.parseFloat(elements[6]), elements[7]));
		    		} 
		    else {
		    		//Add order to invalid List
		    		invalidCount++;
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
					if(broker.name == order.broker) {
						System.out.println("Found Broker: " + broker.name);
					break;	
					}
				}
			} else {
				System.out.println("Invalid Symbol: " + order.symbol);
				invalidCount++;
				//add order to invalid list
			}
			System.out.print(order.orderTimeStamp + " ");
			System.out.print(order.broker + " ");
			System.out.print(order.sequenceID + " ");
			System.out.print(order.type + " ");
			System.out.print(order.getSymbol() + " ");
			System.out.print(order.quantity + " ");
			System.out.print(order.price + " ");
			System.out.print(order.side + " \n");
		}
		System.out.print(invalidCount);
	}
	
	
	
	//  KKKKKKKKKKKKKKKKK        HHHHHHHHHHHHHHHHHHH      Not used YET!!
     
    // to check whether integer is greater than 0 or not
    // usually used when we have to input a whole number
    // like taking input of number of test cases etc
    public static boolean isValidInteger(int test)
    {
        return (test >= 0);
    }
     
    // to check whether integer is greater than the lower
    //  bound and lower than the highest bound.
    public static boolean isValidInteger(int test, int low, 
                                               int high) {
        return (test >= low && test <= high);
    }
     
    // used when we want the user to input the number
    // exactly greater than the lower bound
    public static int getInRange(int low) {
        Scanner sc = new Scanner(System.in);
        int test;
        do
        {
            test = sc.nextInt();
        } while (test < low);
         
        return test;
    }
     
    // used when we want the user to input the number
    // exactly greater than lower bound and lower than
    // the highest bound
    public static int getInRange(int low, int high) {
        Scanner sc = new Scanner(System.in);
        int test;
        do {
            test = sc.nextInt();
        } while (test < low || test > high);
        return test;
    }
     
    // to check whether an array contains any negative value
    public static boolean validatePositiveArray(int[] array, 
                                                  int n) {
        for (int i = 0; i < n; i++)         
            if (array[i] < 0) 
                return false;        
        return true;
    }
     
    // to check whether an array contains any positive value
    public static boolean validateNegativeArray(int[] array, 
                                                  int n) {
        for (int i = 0; i < n; i++)
            if (array[i] > 0) 
                return false;
        return true;
    }
     
    // check whether every element in the array is greater
    // than the lower bound
    public static boolean checkRangeArray(int[] array, 
                                     int n, int low) {
        for (int i = 0; i < n; i++)        
            if (array[i] < low)
                return false;
        return true;
    }
     
    // check whether every element in the array is greater
    // than the lower bound and lower than the highest bound
    public static boolean checkRangeArray(int[] array, int n,
                                         int low, int high) {
        for (int i = 0; i < n; i++)        
            if (array[i] < low || array[i] > high)             
                return false;
        return true;
    }
     
    // check whether two given sets as "arrays" are equal or not
    public static boolean isEqualSets(int[] array1, int n,
                                    int[] array2, int m) {
        if (n != m)         
            return false;
        HashMap<Integer, Integer> Map = 
                            new HashMap<Integer, Integer>();
        for (int i = 0; i < n; i++) 
           Map.put(new Integer(array1[i]), new Integer(1));
        for (int i = 0; i < m; i++) 
           Map.put(new Integer(array2[i]), new Integer(0));
 
        for (int i = 0; i < n; i++) 
           if (Map.get(array1[i]) == 1) 
             return false;
         return true;      
    }
     
    // calculating factorial of a number
    public static String factorial(int n) {
        String fact = new String("");
        int res[] = new int[500];
     
        res[0] = 1;
        int res_size = 1;
     
        for (int x = 2; x <= n; x++)
        res_size = multiply(x, res, res_size);
     
        for (int i = res_size - 1; i >= 0; i--)
        fact += Integer.toString(res[i]);
     
        return fact;
    }
     
    // Multiply x  with res[0..res_size-1]
    public static int multiply(int x, int res[], int res_size) {
        int carry = 0;    
        for (int i = 0; i < res_size; i++) {
           int prod = res[i] * x + carry;
           res[i] = prod % 10;    
           carry = prod / 10;
        }
        while (carry != 0) {
           res[res_size] = carry % 10;
           carry = carry / 10;
           res_size++;
        }
        return res_size;
    }
     
    // Checks whether the given number is prime or not
    public static boolean isPrime(int n) {
        if (n == 2) 
           return true;
         
        int squareRoot = (int)Math.sqrt(n);    
        for (int i = 1; i <= squareRoot; i++) 
          if (n % i == 0 && i != 1) 
             return false;
        return true;
    }
     
    // Returns nthPrimeNumber
    public static int nthPrimeNumber(int n) {
        int k = 0;
        for (int i = 2;; i++) {
          if (isPrime(i)) 
              k++;        
          if (k == n) 
              return i;
        }
    }
     
    // check whether the given string is palindrome or not
    public static boolean isPalindrome(String test) {
        int length = test.length();    
        for (int i = 0; i <= (test.length()) / 2; i++) 
          if (test.charAt(i) != test.charAt(length - i - 1)) 
             return false;
        return true;
    }
     
    // check whether two strings are anagram or not
    public static boolean isAnagram(String s1, String s2) {
 
        // Removing all white spaces from s1 and s2
        String copyOfs1 = s1.replaceAll("\\s", "");
        String copyOfs2 = s2.replaceAll("\\s", "");
     
        if (copyOfs1.length() != copyOfs2.length()) 
            return false;
 
        // Changing the case of characters of both copyOfs1 and
        // copyOfs2 and converting them to char array
        char[] s1Array = copyOfs1.toLowerCase().toCharArray();
        char[] s2Array = copyOfs2.toLowerCase().toCharArray();
 
        // Sorting both s1Array and s2Array
        Arrays.sort(s1Array);
        Arrays.sort(s2Array);
     
        // Checking whether s1Array and s2Array are equal    
        return (Arrays.equals(s1Array, s2Array));
     }    
} /*** end of helperClass **/
     