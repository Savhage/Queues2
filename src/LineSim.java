import java.util.*;
public class LineSim {
    int numStandLines;
    int numSuper;
    int numExp;
    int arrivalRate;
    int maxItems;
    int maxSimTime;
    int clock;
    ArrayList<Line> lanes;
    Queue<Integer> served;
    Queue<Integer> avgCusPerHour;
    Queue<Double> avgWaitPerHour;
    Queue<Integer> avgItemPerHour;
    Queue<Double> avgFreeTimePerHour;
    Scanner input=new Scanner(System.in);
    public LineSim(){
        lanes=new ArrayList<>();
        served=new LinkedList<>();
        avgCusPerHour=new LinkedList<>();
        avgWaitPerHour=new LinkedList<>();
        avgItemPerHour=new LinkedList<>();
        avgFreeTimePerHour=new LinkedList<>();
        clock=0;
    }
    public void simulate(){
        enterData();
        createLanes();
        runSimulation();
        getData();
    }
    //Collect parameters and assign to relevant variable after converting to seconds if necessary
    private void enterData(){
        System.out.print("How long will the simulation run in hours: ");
        int hours=input.nextInt();
        maxSimTime=hours*3600;

        System.out.print("How many Customer will check out per hour: ");
        int perHour=input.nextInt();
        int perMinute=perHour/60;
        arrivalRate=60/perMinute;

        System.out.print("What is the maximum number of items a Customer can have: ");
        maxItems=input.nextInt();

        System.out.print("How many Standard Lines are open: ");
        numStandLines=input.nextInt();

        System.out.print("What is the maximum number of items for the Super Express Lane: ");
        numSuper=input.nextInt();

        System.out.print("What is the maximum number of items for the Express Lane: ");
        numExp=input.nextInt();
    }
    //Starts simulation. Calls addCus(). Serves customer is current clock is equal to lines internal lock and line
    //and line is not empty. If line is empty and clocks are equal than it increments internal clock.
    //Clocks not equal result of serving someone
    private void runSimulation(){
        while(clock<=maxSimTime){
            addCus();
            for(int i=0;i<lanes.size();i++){
                if(!lanes.get(i).isEmpty() && lanes.get(i).getInternalClock()==clock){
                    lanes.get(i).serveCus(clock);
                }
                else if(lanes.get(i).getInternalClock()==clock){
                    lanes.get(i).increaseClock();
                }
            }
            clock++;
        }
        boolean check=true;
        while(check) {
            check = false;
            for (int i = 0; i < lanes.size(); i++) {
                if (!lanes.get(i).isEmpty()) {
                    check = true;
                }
            }
            for (int i = 0; i < lanes.size(); i++) {
                if (!lanes.get(i).isEmpty()) {
                    lanes.get(i).serveCus(clock);
                }
            }
            clock++;
        }
    }
    private void createLanes(){
        for(int i=0;i<(numStandLines+3);i++){
            Line newLine=new Line();
            lanes.add(newLine);
            System.out.println(i);
        }
    }
    //Add Customer if the clock is divisible evenly by the arrival rate
    private void addCus(){
        if(clock%arrivalRate==0){
            Customer newCus=new Customer(clock,maxItems);
            lanes.get(checkLanes(newCus)).addCus(newCus);
        }
    }
    //Checks how many items the new customer has and skips over Super and Express lanes depending on items
    //To change number of super or express lanes just change the starting i value for the second and/or third loop
    private int checkLanes(Customer newCus){
        boolean check=false;
        int compare=9999;
        int holder=0;
        //SuperExpress
        if(newCus.getItems()<numSuper){
            for(int i=0;i<lanes.size();i++){
                if(lanes.get(i).isEmpty()){
                    return i;
                }
                else{
                    if(lanes.get(i).getWaiting()<compare){
                        compare=lanes.get(i).getWaiting();
                        check=true;
                        holder=i;
                    }
                }
            }
            return holder;
        }
        //Skips Super and starts at Express
        else if(newCus.getItems()<numExp){
            for(int i=1;i<lanes.size();i++){
                if(lanes.get(i).isEmpty()){
                    return i;
                }
                else{
                    if(lanes.get(i).getWaiting()<compare){
                        compare=lanes.get(i).getWaiting();
                        check=true;
                        holder=i;
                    }
                }
            }
            return holder;
        }
        //Skips Super and Express
        else{
            for(int i=3;i<lanes.size();i++){
                if(lanes.get(i).isEmpty()){
                    return i;
                }
                else{
                    if(lanes.get(i).getWaiting()<compare){
                        compare=lanes.get(i).getWaiting();
                        check=true;
                        holder=i;
                    }
                }
            }
        }
        return holder;
    }
    private void getData() {
        getSuperData();
        getExpressData(1);
        getExpressData(2);
        getRegData(3);
    }
    private void getSuperData(){
        double wait=lanes.get(0).getAverageWait();
        avgWaitPerHour.offer(wait);
        int maxLength=lanes.get(0).getMaxCus();
        int cusPerHour=lanes.get(0).averageCusPerHour();
        avgCusPerHour.offer(cusPerHour);
        int itemsPerHour=lanes.get(0).getAvgItemsPerHour();
        avgItemPerHour.offer(itemsPerHour);
        double freeTime=lanes.get(0).getAvgFreeTime();
        avgFreeTimePerHour.offer(freeTime);

        System.out.println( "Express Lane:"+
                            "\nAverage Wait Time: "+  String.format("%.2f Minutes",wait)+
                            "\nMaximum Length of Line: "+ maxLength+
                            "\nNumber of Customers Served Per Hour: "+ cusPerHour+
                            "\nNumber of Items Processed Per Hour: "+ itemsPerHour+
                            "\nAverage Free Time Per Hour: "+ String.format("%.2f Minutes\n",freeTime));
    }
    private void getExpressData(int n){
        double wait=lanes.get(n).getAverageWait();
        avgWaitPerHour.offer(wait);
        int maxLength=lanes.get(n).getMaxCus();
        int cusPerHour=lanes.get(n).averageCusPerHour();
        avgCusPerHour.offer(cusPerHour);
        int itemsPerHour=lanes.get(n).getAvgItemsPerHour();
        avgItemPerHour.offer(itemsPerHour);
        double freeTime=lanes.get(n).getAvgFreeTime();
        avgFreeTimePerHour.offer(freeTime);

        System.out.println( "Express Lane "+ n+": "+
                "\nAverage Wait Time: "+  String.format("%.2f Minutes",wait)+
                "\nMaximum Length of Line: "+ maxLength+
                "\nNumber of Customers Served Per Hour: "+ cusPerHour+
                "\nNumber of Items Processed Per Hour: "+ itemsPerHour+
                "\nAverage Free Time Per Hour: "+ String.format("%.2f Minutes\n",freeTime));
    }
    private void getRegData(int n){
        double wait,freeTime;
        int maxLength, cusPerHour, itemsPerHour;

        for(int i=n;i<lanes.size();i++){
            wait=lanes.get(i).getAverageWait();
            avgWaitPerHour.offer(wait);
            maxLength=lanes.get(i).getMaxCus();
            cusPerHour=lanes.get(i).averageCusPerHour();
            avgCusPerHour.offer(cusPerHour);
            itemsPerHour=lanes.get(i).getAvgItemsPerHour();
            avgItemPerHour.offer(itemsPerHour);
            freeTime=lanes.get(i).getAvgFreeTime();
            avgFreeTimePerHour.offer(freeTime);

            System.out.println( "Regular Lane "+i+": "+
                    "\nAverage Wait Time: "+  String.format("%.2f Minutes",wait)+
                    "\nMaximum Length of Line: "+ maxLength+
                    "\nNumber of Customers Served Per Hour: "+ cusPerHour+
                    "\nNumber of Items Processed Per Hour: "+ itemsPerHour+
                    "\nAverage Free Time Per Hour: "+ String.format("%.2f Minutes\n",freeTime));
        }
    }
    private void getAverages(){
        ;
        double freeTimeTotal;
        int itemsTotal, cusTotal;
        for(int i=0;i<lanes.size();i++){
            waitTotal+=avgWaitPerHour.poll();

        }
    }
}
