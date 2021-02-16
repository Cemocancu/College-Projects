//Java program to detect all the items in the array appearing more than m / n times.
//I hard coded the n number so if you sir/madam want to change it you have to do it manually.
import java.util.Arrays;

public class q2a {
    //Merge method to merge the two half of the array
    private static void merge(int[] array, int[] aux, int lo, int mid, int hi)
    {
        //Copy array to aux
        if (hi + 1 - lo >= 0) System.arraycopy(array, lo, aux, lo, hi + 1 - lo);

        int i = lo, j = mid+1;
        for(int k = lo; k <= hi; k++)
        {
            if(i > mid) array[k] = aux[j++];
            else if(j > hi) array[k] = aux[i++];
            else if(aux[j] < aux[i]) array[k] = aux[j++];
            else array[k] = aux[i++];
        }
    }
    //Sort method to sort the two half of the array
    private static void sort(int[] array, int[] aux, int lo, int hi)
    {
        if(hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(array, aux, lo , mid);
        sort(array, aux, mid+1, hi);
        merge(array, aux, lo, mid, hi);
    }
    //Check method to check if a given array has the given value
    private static boolean check(int[] array, int toCheckValue)
    {
        boolean test = false;
        for (int element : array) {
            if (element == toCheckValue) {
                test = true;
                break;
            }
        }

        return test;
    }
    //Addx method to add a value to an array
    public static int[] addX(int n, int[] array, int x)
    {
        int i;
        int[] newArray = new int[n + 1];

        for (i = 0; i < n; i++)
            newArray[i] = array[i];

        newArray[n] = x;

        return newArray;
    }

    public static void main(String[] args)
    {
        //Getting the value of n
        int n = 3;
        //Creating the main array
        int[] array = {5, 2, 7, 2, 1, 3 ,5 ,1, 3, 5, 5 , 1, 1, 3};
        //Creating the aux array
        int[] aux = {5, 2, 7, 2, 1, 3 ,5 ,1, 3, 5, 5 , 1, 1, 3};
        //Calculating the m divided by n
        int result = array.length / n;
        //Creating a separate array to hold the repetitive numbers
        int[] repeatedNumbers = new int[0];

        //Array elements before merge sort
        System.out.println("Array: " + Arrays.toString(array));

        //Merge sorting the array
        sort(array, aux, 0, array.length - 1);

        //Array elements after merge sort
        System.out.println("New array: " + Arrays.toString(array));

        //Searching the array to find the repeated numbers
        for(int i = 0; i < array.length; i++){
            if(i + result - 1 > array.length - 1) break;
            else if(i + result - 1 == -1) break;
            else if(array[i] == array[i + result - 1])
                if (!(check(repeatedNumbers, array[i])))
                    repeatedNumbers = addX(repeatedNumbers.length, repeatedNumbers, array[i]);
        }

        //Print the repeated numbers to the screen
        System.out.println("Repeated numbers: " + Arrays.toString(repeatedNumbers));
    }
}
