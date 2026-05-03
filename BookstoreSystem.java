package phasefinal;

import java.util.List;
import java.util.ArrayList;

public class BookstoreSystem {
    private static BookstoreSystem instance;
    private Owner owner;
    private List<Book> books;
    private List<Customer> customers;
    
    
    private BookstoreSystem() {
        this.owner = new Owner();
       
        this.customers = new ArrayList<>();
        
        this.books = new ArrayList<>();
    }
    
    public static synchronized BookstoreSystem getInstance() {
        if (instance == null) {
            instance = new BookstoreSystem();
        }
        return instance;
    }
    
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    public List<Book> getBooks() {
        return books;
    }
    
    public User authenticateUser(String username, String password) {
        if (owner.login(username, password)) {
            return owner;
        }
        
        for (Customer customer : customers) {
            if (customer.login(username, password)) {
                return customer;
            }
        }
        
        return null;
    }
    
    public void addCustomer(String username, String password) {
        customers.add(new Customer(username, password));
    }
    
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }
    
    public void addBook(String name, double price) {
        books.add(new Book(name, price));
    }
    
    public void removeBook(Book book) {
        books.remove(book);
    }
    
    public double calculatePurchase(User user, List<Book> selectedBooks, boolean redeem) {
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            double totalCost = selectedBooks.stream().mapToDouble(Book::getPrice).sum();
            
            if (redeem) {
                int pointsToRedeem = customer.getPoints();
                double discount = pointsToRedeem / 100.0;
                return Math.max(0, totalCost - discount);
            }
            return totalCost;
        }
        return 0;
    }
}
