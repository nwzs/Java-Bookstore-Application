package phasefinal;

import java.util.List;

public interface CustomerState {
    void buyBooks(Customer customer, List<Book> books, boolean redeem);
    void updateStatus(Customer customer);
    String getStatusName();
}
