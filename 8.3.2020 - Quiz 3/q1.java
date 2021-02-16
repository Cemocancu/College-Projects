//Java program to implement merge sort for linked lists. I used the methods from the class slides.
//I only changed the arrays to linked list.
import java.util.*;

public class q1
{
    //Merge method to merge the two half of the linked list
    private static void merge(LinkedList<Integer> linkedList, LinkedList<Integer> aux, int lo, int mid, int hi)
    {
        //Copy linked list to aux
        for(int k = lo; k <= hi; k++)
            aux.set(k,linkedList.get(k));

        int i = lo, j = mid+1;
        for(int k = lo; k <= hi; k++)
        {
            if(i > mid) linkedList.set(k, aux.get(j++));
            else if(j > hi) linkedList.set(k, aux.get(i++));
            else if(aux.get(j) < aux.get(i)) linkedList.set(k, aux.get(j++));
            else linkedList.set(k, aux.get(i++));
        }
    }
    //Sort method to sort the two half of the linked list
    private static void sort(LinkedList<Integer> linkedList, LinkedList<Integer> aux, int lo, int hi)
    {
        if(hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(linkedList, aux, lo , mid);
        sort(linkedList, aux, mid+1, hi);
        merge(linkedList, aux, lo, mid, hi);
    }

    public static void main(String[] args)
    {
        //Creating the main linked list
        LinkedList<Integer> linkedList = new LinkedList<>();
        //Creating the aux linked list
        LinkedList<Integer> aux = new LinkedList<>();

        //Adding elements to the lists
        linkedList.add(1);
        aux.add(1);
        linkedList.add(7);
        aux.add(7);
        linkedList.add(2);
        aux.add(2);
        linkedList.add(6);
        aux.add(6);
        linkedList.add(3);
        aux.add(3);
        linkedList.add(5);
        aux.add(5);
        linkedList.add(4);
        aux.add(4);

        //Linked list elements before merge sort
        System.out.println("Linked list: " + linkedList);

        //Merge sorting the linked list
        sort(linkedList, aux, 0, linkedList.size() - 1);

        //Linked list elements after merge sort
        System.out.println("New linked list: " + linkedList);
    }
}
