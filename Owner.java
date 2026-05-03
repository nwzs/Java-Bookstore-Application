package phasefinal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Owner extends User {
    public static List<Book> books = new ArrayList<>(); // Made public static
    private final List<Customer> customers = new ArrayList<>(); // Internal customer list

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    public Owner() {
        super(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Override
    public boolean login(String inputUsername, String inputPassword) {
        return ADMIN_USERNAME.equals(inputUsername) && ADMIN_PASSWORD.equals(inputPassword);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void deleteCustomer(Customer customer) {
        customers.remove(customer);
    }

    public void restockArrays() throws IOException {
        books = FileHandler.readBook();
        customers.clear();
        customers.addAll(FileHandler.readCustomer());
    }
}

