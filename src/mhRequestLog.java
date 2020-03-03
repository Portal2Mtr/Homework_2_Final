// Class used to organize each MH request...
public class mhRequestLog implements Comparable<mhRequestLog> {

    int [] mhRequest; // h, h_count
    boolean isDeliv;
    int priorityNum;

     mhRequestLog(int[] mhRequest){

        this.mhRequest = mhRequest;
        isDeliv = false;
        priorityNum = 0;

    }

    public int compareTo(mhRequestLog compareRequest){

         int compareNum = compareRequest.priorityNum;

         // Ascending order
         return this.priorityNum - compareNum;

         //Descending order
//        return compareNum - this.priorityNum;

    }

}
