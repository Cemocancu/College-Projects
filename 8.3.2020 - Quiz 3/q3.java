//Java program to print union of two sorted or unsorted arrays
import java.util.*;

public class q3
{
    //A recursive binary search method. It returns the location x in a given array
    private static int binarySearch(int[] array, int l, int r, int x)
    {
        if (r >= l)
        {
            int mid = l + (r - l) / 2;

            if (array[mid] == x)
                return mid;

            if (array[mid] > x)
                return binarySearch(array, l, mid - 1, x);

            return binarySearch(array, mid + 1, r, x);
        }
        return -1;
    }
    //Prints the union of two unsorted arrays
    private static void printUnion(int[] array1, int[] array2, int array1Size, int array2Size)
    {
        if(array1Size > array2Size)
        {
            int[] temporaryArray = array1;
            array1 = array2;
            array2 = temporaryArray;

            int temp = array1Size;
            array1Size = array2Size;
            array2Size = temp;
        }

        Arrays.sort(array1);
        for (int i = 0; i < array1Size; i++)
            System.out.print(array1[i] + " ");

        for (int i = 0; i < array2Size; i++)
        {
            if(binarySearch(array1, 0, array1Size - 1, array2[i]) == -1)
                System.out.print(array2[i] + " ");
        }
    }
    //Prints the union of two sorted arrays
    private static void printUnion1(int[] array1, int[] array2, int array1Size, int array2Size)
    {
        if(array1Size > array2Size)
        {
            int[] temporaryArray = array1;
            array1 = array2;
            array2 = temporaryArray;

            int temp = array1Size;
            array1Size = array2Size;
            array2Size = temp;
        }

        for (int i = 0; i < array1Size; i++)
            System.out.print(array1[i] + " ");

        for (int i = 0; i < array2Size; i++)
        {
            if(binarySearch(array1, 0, array1Size - 1, array2[i]) == -1)
                System.out.print(array2[i] + " ");
        }
    }
    public static void main(String[] args)
    {
        //Initialize the unsorted arrays
        int[] array1 = {7, 1, 5, 2, 3, 6};
        int[] array2 = {3, 8, 6, 20, 7};

        //This is for unsorted arrays
        System.out.println("Union of two arrays is: ");
        printUnion(array1, array2, array1.length, array2.length);

        //Initialize the sorted arrays
        int[] arr1 = {1, 2, 3, 5, 6, 7};
        int[] arr2 = {3, 6, 8, 7, 20};

        //This is for sorted arrays
        System.out.println("\nUnion of two sorted arrays is: ");
        printUnion1(arr1, arr2, arr1.length, arr2.length);
    }
}
