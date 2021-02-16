//Java program three -way merge sort an array
//Overall running time complexity of this program is O(n. log3(n))
import java.util.*;

public class q4 {
    // Function for three way merge sort process
    public static void threeWayMergeSort(Integer[] array)
    {
        //Creating clone of given array
        Integer[] cloneArray = new Integer[array.length];
        //Copying elements of given array to the clone array
        System.arraycopy(array, 0, cloneArray, 0, cloneArray.length);
        //Calling sorting method
        threeWayMergeSort1(cloneArray, 0, array.length, array);
        //Transfer back elements of clone array to the original array
        System.arraycopy(cloneArray, 0, array, 0, cloneArray.length);
    }
    //Performing the merge sort algorithm on the given array
    public static void threeWayMergeSort1(Integer[] array, int low, int high, Integer[] array1)
    {
        //Base case
        if (high - low < 2)
            return;
        //Splitting array to 3 parts
        int mid1 = low + ((high - low) / 3);
        int mid2 = low + 2 * ((high - low) / 3) + 1;
        //Sorting 3 arrays recursively
        threeWayMergeSort1(array1, low, mid1, array);
        threeWayMergeSort1(array1, mid1, mid2, array);
        threeWayMergeSort1(array1, mid2, high, array);
        //Merging the sorted arrays
        merge(array1, low, mid1, mid2, high, array);
    }
    // Merge the sorted arrays
    public static void merge(Integer[] array, int low, int mid1, int mid2, int high, Integer[] array1)
    {
        int i = low, j = mid1, k = mid2, l = low;

        while ((i < mid1) && (j < mid2) && (k < high))
        {
            if (array[i].compareTo(array[j]) < 0)
            {
                if (array[i].compareTo(array[k]) < 0)
                    array1[l++] = array[i++];
                else
                    array1[l++] = array[k++];
            }
            else
            {
                if (array[j].compareTo(array[k]) < 0)
                    array1[l++] = array[j++];
                else
                    array1[l++] = array[k++];
            }
        }

        while ((i < mid1) && (j < mid2))
        {
            if (array[i].compareTo(array[j]) < 0)
                array1[l++] = array[i++];
            else
                array1[l++] = array[j++];
        }

        while ((j < mid2) && (k < high))
        {
            if (array[j].compareTo(array[k]) < 0)
                array1[l++] = array[j++];

            else
                array1[l++] = array[k++];
        }

        while ((i < mid1) && (k < high))
        {
            if (array[i].compareTo(array[k]) < 0)
                array1[l++] = array[i++];
            else
                array1[l++] = array[k++];
        }

        while (i < mid1)
            array1[l++] = array[i++];

        while (j < mid2)
            array1[l++] = array[j++];

        while (k < high)
            array1[l++] = array[k++];
    }

    public static void main(String[] args)
    {
        //Our hard coded array
        Integer[] array = new Integer[] {-9, -2, 3, 167, 25, -1, -20, 0, 1, 0};

        System.out.println("Before three way merge sort: " + Arrays.toString(array));
        threeWayMergeSort(array);
        //Print the three way merge sorted array
        System.out.println("After three way merge sort: " + Arrays.toString(array));
    }
}
