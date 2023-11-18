package ShoppingQueue;

import ShoppingQueue.Customer;

import java.util.*;
public class Line {
    private int internalClock;
    private int numServed;
    private int itemTotal;
    private int waiting;
    private int currentFreeTime;
    private int freeTimeTotal;
    private int maxCus;
    private int hourMark;
    private int cusCurrentHour;
    private int itemCurrentHour;
    private Queue<Customer> lane;
    private Queue<Integer> waitTimes;
    private Queue<Integer> freeTimes;
    private Queue<Integer> cusPerHour;
    private Queue<Integer> itemsPerHour;


    public Line(){
        internalClock=0;
        hourMark=0;
        cusCurrentHour=0;
        numServed=0;
        itemTotal=0;
        waiting=0;
        currentFreeTime=0;
        maxCus=0;
        freeTimeTotal=0;
        lane=new LinkedList<>();
        waitTimes=new LinkedList<>();
        freeTimes=new LinkedList<>();
        cusPerHour=new LinkedList<>();
        itemsPerHour=new LinkedList<>();
    }
    public void addCus(Customer cus){
        lane.offer(cus);
        waiting++;
        checkMaxCus();
    }
    /*Removes customer from line, reduces the waiting amount
    **increase the number served, add freetime, check for new Maximum number of customers served
    **
     */
    public void serveCus(int clock){
        Customer cus=lane.poll();
        internalClock+=cus.getServeTime();
        hourMark+=cus.getServeTime();
        waiting--;
        numServed++;
        itemTotal+=cus.getItems();
        cusCurrentHour++;
        itemCurrentHour+=cus.getItems();
        checkHourMark();
        addFreeTime();
        waitTimes.add(clock-cus.getStartTime());
    }
    private void checkMaxCus(){
        if(waiting>maxCus){
            maxCus=waiting;
        }
    }
    public int getMaxCus(){
        return maxCus;
    }
    public int getNumServed(){
        return numServed;
    }
    public int getItemTotal(){
        return itemTotal;
    }
    public int getInternalClock(){
        return internalClock;
    }
    //returns number waiting
    public int getWaiting(){
        return waiting;
    }
    public boolean isEmpty(){
        if(waiting!=0){
            return false;
        }
        return true;
    }
    public double getAvgFreeTime(){
        int total=0;
        int freePeriods=freeTimes.size();
        if(freePeriods==0){
            return 0;
        }
        for(int i=0;i<freePeriods;i++){
            total+=freeTimes.poll();
        }
        return total/freePeriods/60;
    }
    public void increaseClock(){
        internalClock++;
        currentFreeTime++;
        hourMark++;
    }
    private void addFreeTime(){
        if(currentFreeTime!=0){
            freeTimes.offer(currentFreeTime);
            freeTimeTotal+=currentFreeTime;
            currentFreeTime=0;
        }
    }
    public int getTotalFreeTime(){
        return (freeTimeTotal/60);
    }
    //Returns average wait in minutes
    public double getAverageWait(){
        int total=0;
        if(numServed==0){
            return 0;
        }
        for(int i=0;i<waitTimes.size();i++){
            total+=waitTimes.poll();
        }
        return total/numServed/60;
    }
    private void checkHourMark(){
        if(hourMark>=3600){
            cusPerHour.offer(cusCurrentHour);
            itemsPerHour.offer(itemCurrentHour);
            hourMark=0;
            cusCurrentHour=0;
            itemCurrentHour=0;
        }
    }
    public int averageCusPerHour(){

        int total=0;
        int num=cusPerHour.size();
        if(num==0){
            return 0;
        }
        for(int i=0;i<num;i++){
            total+=cusPerHour.poll();
        }
        return total/num;
    }
    public int getAvgItemsPerHour(){
        int total=0;
        int num=itemsPerHour.size();
        if(num==0){
            return 0;
        }
        for(int i=0;i<itemsPerHour.size();i++){
            total+=itemsPerHour.poll();
        }
        return total/num;
    }

}
