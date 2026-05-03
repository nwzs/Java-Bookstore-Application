package phasefinal;

import java.util.List;

public class Customer extends User {
    private int points;
    private CustomerState state;
    
    public Customer(String username, String password) {
        super(username, password);
        this.points = 0;
        this.state = new SilverState();
    }
    
    @Override
    public boolean login(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }
    
    public void buyBooks(List<Book> selectedBooks, boolean redeem) {
        state.buyBooks(this, selectedBooks, redeem);
    }
    
    public void updateStatus() {
        state.updateStatus(this);
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
    
    public CustomerState getState() {
        return state;
    }
    
    public void setState(CustomerState state) {
        this.state = state;
    }
    
    public String getStatus() {
        return state.getStatusName();
    }
    
    public String getStatusName() {
        return getStatus();
    }
}
