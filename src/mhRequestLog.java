// Class used to organize each MH request...
public class mhRequestLog {

    int [] mhRequest;
    boolean isDeliv;
    int priorityNum;

     mhRequestLog(int[] mhRequest){

        this.mhRequest = mhRequest;
        isDeliv = false;
        priorityNum = 0;

    }

}
