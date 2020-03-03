// This class keeps track of the global token counters...
public class token {

    int loopCount;
    int priorityCount;
    int MSSLoc;
    int maxNumMSS;

    token(int maxNumMSS){

        loopCount = 0;
        priorityCount = 0;
        MSSLoc = 0; // Start token at MSS #0
        this.maxNumMSS = maxNumMSS - 1;

    }

    void incPriorCnt(){
        priorityCount++;
    }

    void incMSSLoc(){

        MSSLoc++;

        if(MSSLoc > maxNumMSS){
            MSSLoc = 0;
            loopCount++;
        }

    }

}
