import java.util.Random;
public class Customer {
    int items;
    int startTime;
    int perItem=5;
    Random r=new Random();
    public Customer(int c, int p){
        startTime=c;
        items=r.nextInt(1,p+1);
    }
    public int getServeTime(){
        return items*perItem;
    }
    public int getStartTime(){
        return startTime;
    }
}
