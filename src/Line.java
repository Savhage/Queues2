import java.util.*;
public class Line {
    private int internalClock;
    private int numServed;
    private int waiting;
    private Queue<Customer> lane;
    private Queue<Integer> waitTimes;

    public Line(){
        internalClock=0;
        numServed=0;
        waiting=0;
        lane=new LinkedList<>();
        waitTimes=new LinkedList<>();
    }
    public void addCus(Customer cus){
        lane.offer(cus);
        waiting++;
    }
    public void serveCus(int clock){
        Customer cus=lane.poll();
        internalClock+=cus.getServeTime();
        waiting--;
        numServed++;
        waitTimes.add(clock-cus.getStartTime());
    }
    public int getNumServed(){
        return numServed;
    }
    public int getWaiting(){
        return waiting;
    }
    public int getAverageWait(){
        int total=0;
        for(int i=0;i<waitTimes.size();i++){
            total+=waitTimes.poll();
        }
        return total/numServed;
    }
}
