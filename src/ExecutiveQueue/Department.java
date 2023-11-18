package ExecutiveQueue;
import java.util.*;
public class Department {
    private String departmentName;
    private double basePay=40000.00;
    private double seniorityBonus=5000.00;
    private int counter;
    private Queue<Employee> seniority;
    //Created temp and tempEmp to make it easier to cycle through and remove objects from queue
    private Queue<Employee> temp;
    private Employee tempEmp;
    public Department(String s){
        departmentName=s;
        seniority=new LinkedList<>();
        temp=new LinkedList<>();
        tempEmp=new Employee();
        counter=0;
    }
    private void assignTemp(Employee emp){
        tempEmp=emp;
    }
    public String getDepartmentName(){
        return departmentName;
    }
    public void add(Employee emp){
        seniority.offer(emp);
        counter++;
    }
    public Employee remove(int n){
        int i=0;
        while(i<counter){
            if(n==i){
                assignTemp(seniority.poll());
            }
            else{
                temp.offer(seniority.poll());
            }
            i++;
        }
        counter--;
        seniority=temp;
        return tempEmp;
    }
    public double getEmployeePay(int n){
        return basePay+((counter-n-1)*seniorityBonus);

    }
    //returns string with specific employees name based on index chosen
    public String getEmployeeName(int n){
        ;
        for(int i=0;i<seniority.size();i++){
            if(i==n){
                tempEmp=seniority.peek();
            }
            temp.offer(seniority.poll());
        }
        seniority=temp;
        return tempEmp.getName();
    }
    //Prints names of all employees in department
    public void printNames(){

        System.out.println(getDepartmentName()+" Seniority List:");
        for(int i=0;i<counter;i++){
            System.out.println((i+1)+". "+seniority.peek().getName());
            temp.offer(seniority.poll());
        }
        seniority=temp;
    }
    //Cycles through Employees, displaying name and payroll amount with bonuses
    public void getDepartmentPayroll(){
        Queue<Employee> temp=new LinkedList<>();
        System.out.println("\n"+departmentName+" Payroll\n");
        for(int i=0;i<counter;i++){
            System.out.println(i+1+" "+seniority.peek().getName()+" "+String.format("$%,.2f",getEmployeePay(i)));
            temp.offer(seniority.poll());
        }
        seniority=temp;
    }
    public int size(){
        return counter;
    }
}
