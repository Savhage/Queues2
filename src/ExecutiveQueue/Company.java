
//Random Comment
package ExecutiveQueue;
import java.util.*;
public class Company {
    Scanner input=new Scanner(System.in);
    ArrayList<Department> company;
    public Company(){
        company=new ArrayList<>();
        Department newDep=new Department("Unemployed");
        company.add(newDep);
    }
    //Menu that handles navigation for application
    public void menu()throws InputMismatchException{
        boolean check=true;
        int choice=0;
            try {
                while(check){
                System.out.println("\n1. Add Department" +
                        "\n2. Hire New Executive" +
                        "\n3. Add Executive to Department" +
                        "\n4. Remove Employee from Department" +
                        "\n5. Change Executive's Department" +
                        "\n6. Calculate Payroll" +
                        "\n7. Get Specific Employees Payroll" +
                        "\n8. Exit\n");

                System.out.print("Please Make Your Selection: ");

                    choice = input.nextInt();
                    if (choice == 1) {
                        add();
                    } else if (choice == 2) {
                        hire();
                    } else if (choice == 3) {
                        join();
                    } else if (choice == 4) {
                        quit();
                    } else if (choice == 5) {
                        change();
                    } else if (choice == 6) {
                        payroll();
                    } else if (choice == 7) {
                        salary();
                    } else if (choice == 8) {
                        check = false;
                    } else {
                        error();
                    }
                }
            }
            catch(InputMismatchException e){
                input.nextLine();
                error();
            }

        System.out.println("Program Closed");
    }
    //Used to get user back to menu if they have made a mistake with inputs
    private void error(){
        System.out.println("You have not made a valid selection!\n");
        menu();
    }
    //Displays name of all departments. If no departments have been made
    private void displayDepartments(){
        if(company.size()<2){
            System.out.println("You have not created any departments for the company");
            menu();

        }
        for(int i=1;i<company.size();i++){
            System.out.println(i+" "+company.get(i).getDepartmentName());
        }

    }
    //Displays the names of executives in department by calling method from department class
    private void displayExecutives(int dep){
        company.get(dep).printNames();

    }
    //allows selection of department for multiple uses
    private int getDepartment(){
        displayDepartments();
        System.out.print("Select Department Number: ");
        int choice=(input.nextInt());
        if(choice>=company.size()){
            error();
        }
        return choice;
    }
    //displays and selects employees in department. If selected department is empty, returns user to menu
    private int getExecutive(int dep){
        if(company.get(dep).size()==0){
            System.out.println("The selected department does not have any employees");
            menu();
        }
        displayExecutives(dep);
        System.out.print("Select Employee by Seniority Number: ");
        int choice=(input.nextInt()-1);
        if(choice>=company.get(dep).size()){
            error();
        }
        return choice;
    }
    private void add(){
        input.nextLine();
        System.out.print("Please enter the name of the new Department: ");
        String name=input.nextLine();
        Department newDep=new Department(name);
        company.add(newDep);
    }
    private void hire(){
        input.nextLine();
        System.out.print("Please enter the name of the newly hired Executive: ");
        String name=input.nextLine();
        Employee newEmp=new Employee(name);
        company.get(0).add(newEmp);
    }
    //Moves employee from unemployed pool to specified department. If unemployed pool is empty it will display menu
    private void join(){
        if(company.get(0).size()==0){
            System.out.println("There is no one in the unemployed pool.");
            menu();
        }
        System.out.println("Which Executive from the unemployed pool do you wish to join a new department?");
        int ChoiceE=getExecutive(0);
        Employee temp=remove(0,ChoiceE);
        System.out.println("Which Department do you want the Executive to join");
        int choiceD2=getDepartment();
        company.get(choiceD2).add(temp);
        System.out.println(temp.getName()+" has been added to "+ company.get(choiceD2).getDepartmentName());
    }
    //removes employee from specific department. return for remove is not attached to anything
    private void quit(){
        System.out.println("Which department does the employee you wish to remove work?");
        int choiceD=getDepartment();
        System.out.println("Which employee do you want to remove?");
        int choiceE=getExecutive(choiceD);
        remove(choiceD,choiceE);
        System.out.println("Employee has been removed from company!");
    }
    //Removes employee from one department and prompts which department they will go
    private void change(){
        System.out.println("Which department do you want to remove an Executive");
        int choiceD=getDepartment();
        System.out.println("Which employee do you wish to move");
        int ChoiceE=getExecutive(choiceD);
        Employee temp=remove(choiceD,ChoiceE);
        System.out.println("Which Department do you want the Executive transferred to");
        int choiceD2=getDepartment();
        company.get(choiceD2).add(temp);
        System.out.println(temp.getName()+" has been added to "+ company.get(choiceD2).getDepartmentName());
    }
    //displays salary for specific Employee by first asking for department
    private void salary(){
        System.out.println("Which department does the Executive belong to?");
        int choiceD=getDepartment();
        System.out.println("Which Employee's salary do you wish to see?");
        int choiceE=getExecutive(choiceD);
        System.out.println(company.get(choiceD).getEmployeeName(choiceE)+" makes "+
                String.format("$%,.2f",company.get(choiceD).getEmployeePay(choiceE))+ " a year.");
    }
    //Displays payroll for all departments that have at least 1 employee
    private void payroll(){
        for (int i=1;i<company.size();i++) {
            if(company.get(i).size()>0) {
                company.get(i).getDepartmentPayroll();
            }
        }
    }
    //Removes employee and returns it for use elsewhere
    private Employee remove(int dep,int emp){
        return company.get(dep).remove(emp);
    }
}
