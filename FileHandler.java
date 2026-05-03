package phasefinal;

import java.util.ArrayList;

import java.io.*;

import java.io.BufferedReader;


public class FileHandler {
    
    
    private static final String bookFileName = "book.txt";
    
    private static final String customerFileName = "customer.txt";
    
    
    //read the books from file
    
    
    public static ArrayList<Book> readBook(){
        
     ArrayList<Book> bookList = new ArrayList<>();
     
     BufferedReader bookReader = null;
     
     try{
         
         bookReader = new BufferedReader(new FileReader(bookFileName));
         
         String row = bookReader.readLine();
         
         //if the line is not null it will read the book name and price
         
         while(row != null){
             
             
             String[] bookParts = row.split(" ");
             
             if(bookParts.length == 2){
                 
                 String bookName = bookParts[0].trim();
                 //converts the string bookPrice to a double 
                 double bookPrice = Double.parseDouble(bookParts[1].trim());
                 
                 bookList.add(new Book(bookName, bookPrice));
                 
             }
             //read te next line
             row = bookReader.readLine();
         }
         
         
         
     }catch(IOException e){
         
         e.printStackTrace();
         
         
         //close the book reader if it is not null at the end
     }finally{
         
         
         if(bookReader != null){
             
             
             try{
                 
                 bookReader.close();
                 
             }catch(IOException e){
                 
                 e.printStackTrace();
                 
             }
         }
     }
        
     return bookList;
    }
    
    
    
    //write the book after it has been read
    public static void writeBook(ArrayList<Book> bookList){
        
        
        BufferedWriter bookWriter = null;
        
        try{
            
            bookWriter = new BufferedWriter(new FileWriter(bookFileName));
            
            for(Book book : bookList){
                
                bookWriter.write(book.getName() + " " + book.getPrice());
                
                bookWriter.newLine();
                
            }
            
            
        }catch(IOException e){
            
            e.printStackTrace();
            
            
        }finally{
            
            if(bookWriter != null){
                
                try{
                    
                    bookWriter.close();
                }catch(IOException e){
                    
                    e.printStackTrace();
                }
                       
            }
            
        }
        
        
    }
    
    //read the customers so that they can be added or deleted in the system
    public static ArrayList<Customer> readCustomer(){
        
        
        ArrayList<Customer> customerList = new ArrayList<>();
        
        BufferedReader customerReader = null;
        
        //if the line is not null we will read the customers.
        try{
            
            customerReader = new BufferedReader(new FileReader(customerFileName));
            
            String row = customerReader.readLine();
            
            
            while(row != null){
                
                
           
                
                
                //create an array called customerParts where we can seperate caracteristics about the customer
                String[] customerParts = row.split(" ");
                
                if(customerParts.length == 3){
                    
                    
                    //first part of string
                    String customerName = customerParts[0].trim();
                    //second part of string
                    String customerPassword = customerParts[1].trim();
                    
                    
                    
                    
                    //everytime we read a new customer, it will be created
                    
                    Customer customer = new Customer(customerName, customerPassword);
                    
                    //converts the string customerPoints to an integer
                    int customerPoints = Integer.parseInt(customerParts[2].trim());
                    
                    customer.setPoints(customerPoints);
                    
                    customer.updateStatus();
                    
                 
                    
                    
                    //add customer to list
                    
                    customerList.add(customer);
                }
                //read the next line
                row = customerReader.readLine();
                
                
            }
            
        }catch(IOException e){
            
            e.printStackTrace();
            
         //close the customer reader if it is not null   
        }finally{
            
            if(customerReader != null){
                try{
                    
                    customerReader.close();
                }catch(IOException e){

                    e.printStackTrace();
                }
            }
        }
        
        
        return customerList;
        
    }
    
    public static void writeCustomer(ArrayList<Customer> customerList){
        
        BufferedWriter customerWriter = null;
        
         try{
            
            customerWriter = new BufferedWriter(new FileWriter(customerFileName));
            
            for(Customer customer : customerList){
                //gets username and password from the user class
                customerWriter.write(customer.getUsername() + " " + customer.getPassword() + " " + customer.getPoints());
                
                customerWriter.newLine();
                
            }
        
        }catch(IOException e){


            e.printStackTrace();
            
            
        //close the customerwriter
        }finally{
             
             if(customerWriter != null){
                 
                 try{
                     
                     customerWriter.close();
                     
                 }catch(IOException e){
                     
                     e.printStackTrace();
                 }
                 
                 
             }
         }
        
    }
    
    
    
    
}
