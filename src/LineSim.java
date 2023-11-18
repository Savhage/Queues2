import java.util.*;
public class LineSim {
    int numStandLines;
    int numSuper;
    int numExp;
    int arrivalRate;
    int maxItems;
    int maxSimTime;
    int clock;
    int cusTotal;
    int itemTotal;
    int freeTotal;
    ArrayList<Line> lanes;
    Queue<Double> avgWaitPerHour;
    Scanner input=new Scanner(System.in);
    public LineSim(){
        lanes=new ArrayList<>();
        avgWaitPerHour=new LinkedList<>();
        clock=0;
        cusTotal=0;
        itemTotal=0;
        freeTotal=0;
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

        System.out.print("How many Customer will check out per hour" +
                "\n(Must be more than 60): ");
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
        getAverages();
    }
    private void getSuperData(){
        cusTotal+=lanes.get(0).getNumServed();
        double wait=lanes.get(0).getAverageWait();
        avgWaitPerHour.offer(wait);
        int maxLength=lanes.get(0).getMaxCus();
        int cusPerHour=lanes.get(0).averageCusPerHour();
        itemTotal+=lanes.get(0).getItemTotal();
        int itemsPerHour=lanes.get(0).getAvgItemsPerHour();
        freeTotal+=lanes.get(0).getTotalFreeTime();
        double freeTime=lanes.get(0).getAvgFreeTime();

        System.out.println( "\nSuper Express Lane:"+
                            "\nTotal Customers Served: "+ lanes.get(0).getNumServed()+
                            "\nAverage Wait Time: "+  String.format("%.2f Minutes",wait)+
                            "\nMaximum Length of Line: "+ maxLength+
                            "\nNumber of Customers Served Per Hour: "+ cusPerHour+
                            "\nTotal Items Processed: "+ lanes.get(0).getItemTotal()+
                            "\nNumber of Items Processed Per Hour: "+ itemsPerHour+
                            "\nTotal Free Time: "+ lanes.get(0).getTotalFreeTime()+
                            "\nAverage Free Time Per Hour: "+ String.format("%.2f Minutes\n",freeTime));
    }
    private void getExpressData(int n){
        cusTotal+=lanes.get(n).getNumServed();
        double wait=lanes.get(n).getAverageWait();
        avgWaitPerHour.offer(wait);
        int maxLength=lanes.get(n).getMaxCus();
        int cusPerHour=lanes.get(n).averageCusPerHour();
        itemTotal+=lanes.get(n).getItemTotal();
        int itemsPerHour=lanes.get(n).getAvgItemsPerHour();
        freeTotal+=lanes.get(n).getTotalFreeTime();
        double freeTime=lanes.get(n).getAvgFreeTime();

        System.out.println( "Express Lane:"+n+":"+
                "\nTotal Customers Served: "+ lanes.get(n).getNumServed()+
                "\nAverage Wait Time: "+  String.format("%.2f Minutes",wait)+
                "\nMaximum Length of Line: "+ maxLength+
                "\nNumber of Customers Served Per Hour: "+ cusPerHour+
                "\nTotal Items Processed: "+ lanes.get(n).getItemTotal()+
                "\nNumber of Items Processed Per Hour: "+ itemsPerHour+
                "\nTotal Free Time: "+ lanes.get(n).getTotalFreeTime()+
                "\nAverage Free Time Per Hour: "+ String.format("%.2f Minutes\n",freeTime));
    }
    private void getRegData(int n){
        double wait,freeTime;
        int maxLength, cusPerHour, itemsPerHour;

        for(int i=n;i<lanes.size();i++){
            cusTotal+=lanes.get(i).getNumServed();
            wait=lanes.get(i).getAverageWait();
            avgWaitPerHour.offer(wait);
            maxLength=lanes.get(i).getMaxCus();
            cusPerHour=lanes.get(i).averageCusPerHour();
            itemTotal+=lanes.get(i).getItemTotal();
            itemsPerHour=lanes.get(i).getAvgItemsPerHour();
            freeTotal+=lanes.get(i).getTotalFreeTime();
            freeTime=lanes.get(i).getAvgFreeTime();

            System.out.println( "Regular Lane "+i+":"+
                    "\nTotal Customers Served: "+ lanes.get(i).getNumServed()+
                    "\nAverage Wait Time: "+  String.format("%.2f Minutes",wait)+
                    "\nMaximum Length of Line: "+ maxLength+
                    "\nNumber of Customers Served Per Hour: "+ cusPerHour+
                    "\nTotal Items Processed: "+ lanes.get(i).getItemTotal()+
                    "\nNumber of Items Processed Per Hour: "+ itemsPerHour+
                    "\nTotal Free Time: "+ lanes.get(i).getTotalFreeTime()+
                    "\nAverage Free Time Per Hour: "+ String.format("%.2f Minutes\n",freeTime));
        }
    }
    private void getAverages(){
        double waitTotal=0;


        for(int i=0;i<lanes.size();i++) {
            waitTotal += avgWaitPerHour.poll();
        }
        System.out.println( "Average For All Lanes: "+
                "\nTotal Customers: " + cusTotal+
                "\nAverage Wait Time Per Lane: "+  String.format("%.2f Minutes",(waitTotal/lanes.size()))+
                "\nAverage Number of Customers Served Per Hour: "+ (cusTotal/(maxSimTime/3600))+
                "\nTotal Items Processed: "+ itemTotal+
                "\nAverage Number of Items Processed Per Hour: "+ (itemTotal/(maxSimTime/3600))+
                "\nTotal Minutes of Free Time: "+ freeTotal);
    }
}
