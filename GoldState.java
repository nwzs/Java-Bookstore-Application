package phasefinal;

import java.util.List;

public class GoldState implements CustomerState {
    
    
    @Override
    public void buyBooks(Customer customer, List<Book> books, boolean redeem) {
        double totalCost = calculateTotalCost(books);
        double costAfterRedemption = totalCost;
        
        if(redeem) {
            costAfterRedemption = applyRedemption(customer, totalCost);
        }
        
        int earnedPoints = (int)(costAfterRedemption * 10);
        customer.setPoints(customer.getPoints() + earnedPoints);
        
        updateStatus(customer);
    }
    
    @Override 
    public void updateStatus(Customer customer) {
        if (customer.getPoints() < 1000){
            customer.setState(new SilverState());
        }
    }
    
    @Override
    public String getStatusName() {
        return "Gold";
    }
    
    private double calculateTotalCost(List<Book> books) {
        return books.stream().mapToDouble(Book::getPrice).sum();
    }
    
    private double applyRedemption(Customer customer, double totalCost) {
        int pointsToRedeem = customer.getPoints();
        double discount = pointsToRedeem / 100.0;
        double costAfterRedemption = Math.max(0, totalCost - discount);
        
        int pointsUsed = (int)((totalCost - costAfterRedemption) * 100);
        customer.setPoints(customer.getPoints() - pointsUsed);

        
        return costAfterRedemption;
    }
    
}
