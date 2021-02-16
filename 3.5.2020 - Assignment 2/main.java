import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    // Employee class to store employee information
    static class Employee{
        String eCode, nRIC, phone;
    }

    // Linked list with type
    static class LinkedList{
        Employee employee; // Object
        LinkedList next; // Next reference

        // Constructor
        public LinkedList(Employee employee, LinkedList next){
            this.employee = employee;
            this.next = next;
        }
    }

    // Hash table class for this assignment
    static class MyHashTable{
        private LinkedList[] linkedLists; // Private table member
        int tableSize; // Size of hash table

        // Hash function taken from pdf
        private int hash1(int key){
            return key % tableSize;
        }

        // Second hash function taken from pdf
        public int doubleHashFunction(int key) { return 1 + (key % (tableSize - 1)); }

        public int collisionDoubleHashing(int key, int i){ return (hash1(key) + i * doubleHashFunction(key)) % tableSize; }

        // Put function for separate chaining
        public void put(int key, Employee employee){
            int indexOfLinkedList = hash1(key); // Get index from hash1
            LinkedList headNode = linkedLists[indexOfLinkedList]; // Create head node for reference

            // To check if head node is already taken
            if (headNode != null){
                // If taken we put new employee to end of linked list
                while(headNode.next != null){
                    headNode = headNode.next;
                }

                headNode.next = new LinkedList(employee, null);
                return; // End function when done
            }

            // New node to put in linked list
            LinkedList newNode = new LinkedList(employee, null); // To be added linked list
            linkedLists[indexOfLinkedList] = newNode; // Add employee to empty cell
        }

        // Put function for linear probing
        public void putLinear(int key, Employee employee){
            int indexOfLinkedList = hash1(key); // Get index from hash1
            LinkedList newNode = new LinkedList(employee, null); // To be added linked list

            //If there is already a different employee in that position
            while (linkedLists[indexOfLinkedList] != null){
                indexOfLinkedList = indexOfLinkedList + 1;
            }

            linkedLists[indexOfLinkedList] = newNode; // Add employee to empty cell
        }

        // Put function for double hashing
        public void putHashing(int key, Employee employee){
            int indexOfLinkedList = hash1(key); // Get index from hash1
            LinkedList newNode = new LinkedList(employee, null); // To be added linked list
            int i = 1; // i value to use in hash2 taken from pdf so we can always find a null slot

            // If there is collision
            if(linkedLists[indexOfLinkedList] != null){
                while(linkedLists[indexOfLinkedList] != null){
                    indexOfLinkedList = collisionDoubleHashing(key, i); // When collision occurs call function
                    i++;
                }
                linkedLists[indexOfLinkedList] = newNode;
                return; // End function early
            }

            linkedLists[indexOfLinkedList] = newNode; // Add employee to empty cell
        }

        // Get function for separate chaining
        // Returns number of comparisons
        // If key not found returns 0
        public int get(int key){
            int comparison = 0; // Number of comparison made
            int indexOfLinkedList = hash1(key); // Get index from hash1
            LinkedList headNode = linkedLists[indexOfLinkedList]; // Create head node for reference

            // Loop to search with given phone for employee
            while(headNode != null){
                comparison++; // Increment comparison every loop cycle
                // When found return comparison
                if (Integer.parseInt(headNode.employee.phone) == key) {
                    return comparison;
                }
                headNode = headNode.next;
            }
            // If phone not found return 0
            return 0;
        }

        // Get function for linear probing
        // Returns number of comparisons
        // If key not found returns 0
        public int getLinear(int key){
            int comparison = 0; // Number of comparison made
            int indexOfLinkedList = hash1(key); // Get index from hash1
            LinkedList headNode = linkedLists[indexOfLinkedList]; // Create head node for reference

            // To check given cell is not null
            if (headNode != null){
                comparison++; // If given key is in first cell then return comparison as 1
                // To check if given cell phone number is equal to key
                while (Integer.parseInt(linkedLists[indexOfLinkedList].employee.phone ) != key){
                    indexOfLinkedList = indexOfLinkedList + 1;
                    comparison++;
                }
                return comparison;
            }
            // If phone not found return 0
            return 0;
        }

        // Get function for linear probing
        // Returns number of comparisons
        // If key not found returns 0
        public int getHashing(int key){
            int comparison = 0; // Number of comparison made
            int indexOfLinkedList = hash1(key); // Get index from hash1
            int i = 1; // i value to use in hash2 taken from pdf

            // To check given cell is not null
            if(linkedLists[indexOfLinkedList] != null){
                comparison++; // If given key is in first cell then return comparison as 1
                // To check if given cell phone number is equal to key
                while(Integer.parseInt(linkedLists[indexOfLinkedList].employee.phone ) != key){
                    indexOfLinkedList = collisionDoubleHashing(key, i);
                    comparison++;
                }
                return comparison;
            }
            // If phone not found return 0
            return 0;
        }

        // Constructor
        public MyHashTable(int tablesize) {
            linkedLists = new LinkedList[tablesize]; // Create linked list
            this.tableSize = tablesize;

            // Create empty linked list chains
            for (int i = 0; i < tablesize; i++)
                linkedLists[i] = null;
        }
    }

    public static void main(String[] args){
        ArrayList<Employee> employees = new ArrayList<>();// To hold employees in a single array
        int indexOfEmployees = 0; // Using it to change every Employee in employees
        int forCounter = 0; // To put each split string to employees
        int lineCounter = 0; // To count how many lines are there to calculate table size
        String[] splitLoadFactor = args[1].split("="); // Split LF to get load factor
        int loadFactor = Integer.parseInt(splitLoadFactor[1]);
        String[] splitLoadFactor2 = args[2].split("="); // Split LF2 to get load factor
        double loadFactor2 = Double.parseDouble(splitLoadFactor2[1]);

        // Read file line by line
        try {
            File file = new File(args[0]); // Get file name from command-line
            Scanner scan = new Scanner(file); // Scanner to get lines each loop
            while (scan.hasNextLine()) {
                lineCounter++;
                String line = scan.nextLine(); // Next line
                // Since first line is just section names we must avoid that line
                //So we only look when lineCounter is bigger than 1
                if(lineCounter > 1){
                    String[] splitLine = line.split(" "); // Split string regex is whitespaces
                    employees.add(new Employee()); // Create new Employee
                    for (String str : splitLine){
                        if (forCounter == 0){
                            employees.get(indexOfEmployees).eCode = str;
                            forCounter = 1;
                        }else if(forCounter == 1){
                            employees.get(indexOfEmployees).nRIC = str;
                            forCounter = 2;
                        }else{
                            employees.get(indexOfEmployees).phone = str;
                            forCounter = 0;
                        }
                    }
                    indexOfEmployees++;
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!"); // When given file is wrong
            e.printStackTrace();
        }

        // Write file line by line
        try {
            // Write part 1 of assignment to output.txt
            int tableSize = (lineCounter - 1) / loadFactor; // Calculate table size for part 1
            MyHashTable hashTable = new MyHashTable(tableSize); // Create our hash table for separate chaining

            // Put every employee from employees to hashTable
            for (Employee employee : employees){
                hashTable.put(Integer.parseInt(employee.phone), employee);
            }

            LinkedList forEmployee; // Employee to use within loop to get all the phones from chained linked lists
            String[] splitTextFileName = args[0].split("\\."); // To get file name without file extension
            FileWriter writer = new FileWriter("output.txt", false); // FileWriter class to write

            // Write lines
            writer.write(splitTextFileName[0] + "," + args[1] + "," + args[2] + "," + args[3] + "\r\n");
            writer.write("PART1\r\n");
            for(int i = 0; i < tableSize;i++){
                writer.write("[Chain " + i + "]: ");
                if(hashTable.linkedLists[i] != null){
                    forEmployee = hashTable.linkedLists[i];
                    writer.write(forEmployee.employee.phone);
                    while(forEmployee.next != null){
                        forEmployee = forEmployee.next;
                        writer.write("---->" + forEmployee.employee.phone);
                    }
                    writer.write("\r\n");
                }else{
                    writer.write("Null\r\n");
                }
            }

            // Write part 2 linear probing of assignment to output.txt
            int tableSize1 = (int) ((lineCounter - 1) /  loadFactor2); // Calculate table size for part 2
            MyHashTable hashTable2 = new MyHashTable((int) tableSize1); // Create our hash table for linear probing

            // Put every employee from employees to hashTable2
            for (Employee employee : employees){
                hashTable2.putLinear(Integer.parseInt(employee.phone), employee);
            }

            writer.write("PART2\r\nHashtable for Linear Probing\r\n");
            for(int i = 0; i < tableSize1;i++){
                writer.write("[" + i + "]--->");
                if(hashTable2.linkedLists[i] != null){
                    writer.write(hashTable2.linkedLists[i].employee.phone + "\r\n");
                }else{
                    writer.write("null\r\n");
                }
            }

            // Write part 2 linear probing of assignment to output.txt
            MyHashTable hashTable3 = new MyHashTable(tableSize1); // Create our hash table for double hashing

            // Put every employee from employees to hashTable2
            for (Employee employee : employees){
                hashTable3.putHashing(Integer.parseInt(employee.phone), employee);
            }

            writer.write("Hashtable for Double Hashing\r\n");
            for(int i = 0; i < tableSize1;i++){
                writer.write("[" + i + "]--->");
                if(hashTable3.linkedLists[i] != null){
                    writer.write(hashTable3.linkedLists[i].employee.phone + "\r\n");
                }else{
                    writer.write("null\r\n");
                }
            }

            // Write founding key argument to output file
            // Calculate CPU time for separate chaining
            double startTime = System.nanoTime();
            int comparison = hashTable.get(Integer.parseInt(args[3]));// To store how many comparison made for separate chaining
            double endTime = System.nanoTime();

            // Write for separate chaining
            writer.write("SEPARATE CHAINING:\r\n");
            writer.write("Key found with " + comparison + " comparisons\r\n");
            writer.write("CPU time taken to search = " + (endTime - startTime) + " ns\r\n");

            // Calculate CPU time for linear probing
            startTime = System.nanoTime();
            comparison = hashTable2.getLinear(Integer.parseInt(args[3])); // To store how many comparison made for linear probing
            endTime = System.nanoTime();

            // Write for linear probing
            writer.write("LINEAR PROBING:\r\n");
            writer.write("Key found with " + comparison + " comparisons\r\n");
            writer.write("CPU time taken to search = " + (endTime - startTime) + " ns\r\n");

            // Calculate CPU time for double hashing
            startTime = System.nanoTime();
            comparison = hashTable3.getHashing(Integer.parseInt(args[3])); // To store how many comparison made for linear probing
            endTime = System.nanoTime();

            // Write for double hashing
            writer.write("DOUBLE HASHING:\r\n");
            writer.write("Key found with " + comparison + " comparisons\r\n");
            writer.write("CPU time taken to search = " + (endTime - startTime) + " ns\r\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
