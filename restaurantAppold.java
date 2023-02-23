import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileOutputStream;
class hotelTaj{
    static Scanner sc=new Scanner(System.in);
    ArrayList<String>FoodMenu=new ArrayList<>();
    ArrayList<String>OrderDetails=new ArrayList<>();
    ArrayList<Integer>myOrder=new ArrayList<>();
    double billAmount=0.0;
    String Date;
    void dataStore()
    {
        try {
            Scanner scan=new Scanner(new FileReader("menuLists.csv"));
            Scanner scan2=new Scanner(new FileReader("OrderDetails.csv"));
            while((scan.hasNext()))
            {
                String eachLine=scan.nextLine();
                FoodMenu.add(eachLine);
            }
            while((scan2.hasNext()))
            {
                String eachLine=scan2.nextLine();
                OrderDetails.add(eachLine);
            }
        } catch (Exception e) {
            System.out.println("Run time Error is occured");
        }
    }
    void getDate(){
        LocalDateTime DateObj = LocalDateTime.now();   
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-dd-MMM");  
        Date = DateObj.format(myFormatObj);
    }
    void Order(String ord)
    {
        getDate();
        String r=OrderDetails.get(OrderDetails.size()-1);
        String newr=r.substring(0,r.indexOf(","));
        int id=Integer.parseInt(newr)+1;
        String reg="\n"+String.valueOf(id)+","+Date+","+billAmount+",";
        for(int i:myOrder)reg=reg+i+" ";
        reg=reg+","+ord;
        try {
            FileOutputStream fileoutput=new FileOutputStream("OrderDetails.csv",true);
            byte[] text=reg.getBytes();
            fileoutput.write(text);
            fileoutput.close();
            dataStore();
            mainMenu();
        } catch (Exception e) {
            System.out.println("error");
        }
    }
   
    void TakeAnOrder(){
        System.out.println("Enter number of items you want to order:");
        int itemCount=sc.nextInt();
        for(int i=0;i<itemCount;i++)
        {
            System.out.println("Enter FoodItem Id:");
            int item=sc.nextInt();
            myOrder.add(item);
            System.out.println("Enter the quantity:");
            int qua=sc.nextInt();
            myOrder.add(qua);
        }
        System.out.println("Press 'y' to make another order or Press 'a' to see order details");
        char ch=sc.next().charAt(0);
        if(ch=='y'||ch=='Y')
        {
            TakeAnOrder();
        }
        if(ch=='a' ||ch=='A'){
            System.out.println("Your order details:");
            for(int i=0;i<myOrder.size();i=i+2)
            {
                String odr=FoodMenu.get(myOrder.get(i)-1);
                String t[]=odr.split(",");
                billAmount+=Double.parseDouble(t[2]);
                System.out.println("FoodItem Name: "+t[1]+" | "+" Price: "+t[2]+" | "+"Quantity: "+myOrder.get(i+1));
            }
        }
        System.out.println("Press s to confirm Order or press 'n' to cancel the order");
        char c=sc.next().charAt(0);
        if(c=='s'|| c=='S')
        {
            Order("Approved");
        }
        if(c=='n'|| c=='N')
        {
            Order("Cancelled");
        }

    }
    void mainMenu(){
        System.out.println("Press Y to Go to Main Menu"); 
        char ch=sc.next().charAt(0);
        if(ch=='y'||ch=='Y')
        {
            Display();
        }
    }
    void edit()
    {
        System.out.println("Enter Order Id Number to edit: ");
        int orderID=sc.nextInt();
        try {
            BufferedReader br=new BufferedReader(new FileReader("OrderDetails.csv"));
            String line="";
            String otxt="";
            String ntxt="";
            while((line=br.readLine())!=null)
            {
                String check=line.substring(0,line.indexOf(","));
                String status=line.substring(line.lastIndexOf(",")+1);
                if(check.contentEquals(String.valueOf(orderID)))
                {
                    if(status.contentEquals("Approved"))
                    {
                        System.out.println(line.substring(0,line.lastIndexOf(",")+1)+"Cancelled");
                        ntxt+=line.substring(0,line.lastIndexOf(",")+1)+"Cancelled"+"\n";
                    }
                    else if(status.contentEquals("Cancelled"))
                    {
                        System.out.println(line.substring(0,line.lastIndexOf(",")+1)+"Approved");
                        ntxt+=line.substring(0,line.lastIndexOf(",")+1)+"Approved"+"\n";
                    }
                }
                else
                {
                    ntxt+=line+"\n";
                    otxt+=line+"\n";
                }
            }
            String newcnt=otxt.replace(otxt, ntxt);
            FileWriter writeobj=new FileWriter("OrderDetails.csv");
            writeobj.write(newcnt);
            writeobj.close();
            br.close();
            dataStore();
            mainMenu();
        } catch (Exception e) {
                System.out.println("Run time error is occured");
        }
    }
   
    void collection(){
        System.out.println("Enter date to see the collection:");
        sc.nextLine();
        String str=sc.nextLine();
        double bill=0.0;
        int g=0;
        for(String i:OrderDetails)
        {
            String dp[]=i.split(",");
            double cd=Double.parseDouble(dp[2]);
            if(str.equals(dp[1]))
            {
                g++;
                bill+=cd;
                System.out.println(i);
            }
        }
        if(g==0)
        {
            System.out.println("Date not found");
        }
        else{
            System.out.println("Todays total collection: "+bill);
        }
        mainMenu();
    }
    
    void Display()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("   Welcome to Hotel Taj");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("1.Make an New Order\n2.Edit Bill\n3.Collections of a day");
        System.out.println("Enter Your Choice:");

        int ch=sc.nextInt();
        if(ch==1)
        {
            TakeAnOrder();
        }
        else if(ch==2)
        {
            edit();
        }
        else{
            collection();
        }

    }
}
public class restaurantAppold {
    public static void main(String[] args) {
        hotelTaj rstnt =new hotelTaj();
        rstnt.dataStore();
        rstnt.Display();
    }
}