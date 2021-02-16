import java.util.*; 
import java.io.*;
public class HW5
{	  
		// Defining it as -1 because if there is no previous Delivery point then
		// It is -1 since there is no negative delivery node in graph
	    private static final int NO_PREV_POINT = -1; 
	  
	    // Dijkstra's algorithm using adjacency matrix
	    private static void dijkstrasAlgorithm(int[][] adjacencyMatrix, int startingPoint, 
	    		int endingPoint, int carrierType, int edgeNumber) 
	    { 
	    	// Number of delivery points
	        int dPoints = adjacencyMatrix[0].length;
	        
	        // Int array to hold shortest times
	        // shortestTimes[i] will hold time from starting delivery point to i delivery point
	        int[] shortestTimes = new int[dPoints];
	        
	        // Int array to hold path length for carrier type 3
	        int[] pathLength = new int[dPoints];
	  
	        // Boolean array to hold if that delivery point is in shortest path
	        boolean[] inPath = new boolean[dPoints]; 
	  
	        // Initialize all times as positive infinity at beginning
	        for (int i = 0; i < dPoints;  i++) 
	        { 
	        	shortestTimes[i] = Integer.MAX_VALUE;
	        	inPath[i] = false; 
	        } 
	        // Time of starting point to itself is always 0
	        shortestTimes[startingPoint] = 0;
	        pathLength[startingPoint] = 0;
	  
	        // Int array to store shortest path
	        int[] shortestPath = new int[dPoints]; 
	  
	        // The starting point does not have a previous point
	        shortestPath[startingPoint] = NO_PREV_POINT;
	        
	        
	  
	        // Find shortest path from starting point to ending point
	        for (int i = 1; i < dPoints; i++) 
	        { 
	            // Pick the minimum time point from boolean array  
	            int nearestPoint = 0; 
	            int shortestTime = Integer.MAX_VALUE;
	            for (int j = 0; j < dPoints;  j++) 
	            { 
	            	// If delivery point is not calculated 
	            	// And it's shortest time is lower than previous time
	                if (!(inPath[j]) && shortestTimes[j] < shortestTime)  
	                { 
	                	nearestPoint = j; 
	                	shortestTime = shortestTimes[j];
	                } 
	            }
	            inPath[nearestPoint] = true;
	  
	            // Update time value of the adjacent points
	            for (int j = 0; j < dPoints;  j++)  
	            {  
	                int edgeTime = adjacencyMatrix[nearestPoint][j]; 
	                
	                // If time is positive and path time is lower than shortest time
	                if (edgeTime > 0 && ((shortestTime + edgeTime) < shortestTimes[j]))  
	                { 
	                	shortestPath[j] = nearestPoint;
	                    shortestTimes[j] = shortestTime +  edgeTime;
	                    pathLength[j] = pathLength[shortestPath[j]] + 1;	
	                }
	            } 
	        }
	        // Call Function to print to screen according to carrier type
	        printShortest(shortestTimes, shortestPath, endingPoint, carrierType, adjacencyMatrix, 
	        		pathLength);
	    }  
	    // Function that prints to screen according to carrier type  
	    private static void printShortest( int[] shortestTimes, int[] shortestPath, 
	    		int endingPoint, int carrierType, int[][] adjacencyMatrix, int[] pathLength) 
	    { 
	    	// Variables that will be used in function
	    	String path;
	    	String[] pathArray;
	    	
	    	// Variable that holds shortest path by calling a recursive function
	    	path = printPath(endingPoint, shortestPath);
	    	
	    	// If carrier type is 1 then simply print to screen
	    	if(carrierType == 1) 
	    	{
		    	System.out.println(shortestTimes[endingPoint]);
		    	System.out.println(path);	
	    	}
	    	// If carrier type is 2 then find heaviest weighted edge and print it's weight
	    	else if(carrierType == 2) 
	    	{
	    		// Temporary variable that holds heaviest weight
	    		int tmp = 0;
	    		
	    		// Split shortest path to get delivery points
	    		pathArray = path.split("->");
	    		
	    		// Iterate over pathArray then look edge's weight between i and i-1
	    		// Uses another temporary variable
	    		for(int i = 1; i < pathArray.length; i++) 
	    		{
	    			int temp = adjacencyMatrix[Integer.parseInt(pathArray[i]) - 1]
	    					[Integer.parseInt(pathArray[i-1]) - 1];
	    			// If new edge's weight(temp) is heavier than previous edge's weight(tmp) swap them
	    			if(temp > tmp)
	    				tmp = temp;
	    		}
	    		// When iteration is over simply print to screen
	    		System.out.println(tmp);
	    	}
	    	// If carrier tpe is 3 then simply print to screen
	    	else if(carrierType == 3) 
	    	{
		    	System.out.println(pathLength[endingPoint]);
		    	System.out.println(path);	
	    	}
	    } 
	    // Function to return shortest path from starting point to end point 
	    private static String printPath(int endingPoint, int[] shortestPath) 
	    { 
	    	// Base case
	    	// If it is starting point then return
	    	if (endingPoint == NO_PREV_POINT) 
		    { 
		      	return Integer.toString(endingPoint + 2); 
		    } 
		    String tmp = printPath(shortestPath[endingPoint], shortestPath); 
		    if((endingPoint + 1) != 1)
		    	tmp = tmp + "->" + Integer.toString(endingPoint + 1);
		    return tmp;	
	    } 
	    // Main function
	    public static void main(String[] args) 
	    { 
	    	// Variable to hold first line values
	    	int carrierType = 0;
	    	int deliveryPoints;
	    	int edgeNumber = 0;
	    	int endingPoint = 0;
	    	int[][] adjacencyMatrix = null;
	    	
	    	// Read file line by line
	    	try 
	    	{
	    		File inputFile = new File(args[0]);
	    	    Scanner myScanner = new Scanner(inputFile);
	    	      
	    	    while (myScanner.hasNextLine()) 
	    	    {
	    	    	String line = myScanner.nextLine();
	    	    	
	    	    	// If white space count is equal 3 than i can say that it is first line
	    	    	int whiteSpaceCount = line.length() - line.replaceAll(" ", "").length();
	    	    	
	    	    	if(whiteSpaceCount == 3) 
	    	    	{
	    	    		// Split first line
	    	    		String[] firstLineValues = line.split(" ");
	    	    		
	    	    		// Assign values to variables according to first line
	    	    		carrierType = Integer.parseInt(firstLineValues[0]);
	    	    		deliveryPoints = Integer.parseInt(firstLineValues[1]);
	    	    		edgeNumber = Integer.parseInt(firstLineValues[2]);
	    	    		endingPoint = Integer.parseInt(firstLineValues[3]);
	    	    		
	    	    		adjacencyMatrix = new int[deliveryPoints][deliveryPoints];
	    	    	}
	    	    		else 
	    	    	{
	    	    		// If there is empty line in input file
	    	    		if(line.equals(""))
	    	    			break;
	    	    		
	    	    		// Split line
	    	    		String[] otherLineValues = line.split(" ");
	    	    		
	    	    		//Assign values to more readable int variables
	    	    		int deliveryPoint1 = Integer.parseInt(otherLineValues[0]);
	    	    		int deliveryPoint2 = Integer.parseInt(otherLineValues[1]);
	    	    		int timeBetween = Integer.parseInt(otherLineValues[2]);
	    	    		
	    	    		// Assign values to adjacency matrix
	    	    		adjacencyMatrix[deliveryPoint1 - 1][deliveryPoint2 - 1] = timeBetween;
	    	    		adjacencyMatrix[deliveryPoint2 - 1][deliveryPoint1 - 1] = timeBetween;
	    	    		
	    	    	}
	    	    }
	    	    myScanner.close();
	    	    } catch (FileNotFoundException e) 
	    		{
	    	    	System.out.println("An error occurred.");
	    	    	e.printStackTrace();
	    	    }
	    	// Call Dijkstra's algorithm with adjacency matrix and carrier type
	    	dijkstrasAlgorithm(adjacencyMatrix, 0, endingPoint - 1, carrierType, edgeNumber);	
	    } 
} 
