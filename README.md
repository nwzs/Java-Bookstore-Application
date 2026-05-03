# 📚 BookStore Application

A Java desktop application for managing a bookstore, built with object-oriented design principles and the **State design pattern**. Supports two user roles — customers and an owner — each with their own set of permissions and workflows.

---

## Tech Stack

**Language:** Java  
**UI Framework:** JavaFX  
**Design Patterns:** State Pattern, Singleton Pattern  
**Persistence:** File I/O (BufferedReader/BufferedWriter)

---

## Features

### Customer
- Login with credentials
- Browse and select books for purchase
- Buy books using **money** or **reward points**
- Automatically earn points on purchases
- Status upgrades: **Silver → Gold** based on points accumulated

### Owner
- Login with admin credentials
- Add and remove books from the system
- Add and remove customer accounts
- View all customers and inventory

---

## Design Patterns Used

### State Pattern
Customer loyalty status is managed using the State design pattern via a `CustomerState` interface implemented by `SilverState` and `GoldState`. Each state defines its own purchasing and point-earning behaviour, making it easy to add new tiers (e.g. Bronze, Diamond) without modifying existing classes.

```
CustomerState (interface)
├── SilverState  →  upgrades to GoldState at 1000 points
└── GoldState    →  downgrades to SilverState below 1000 points
```

### Singleton Pattern
`BookstoreSystem` uses a thread-safe singleton to ensure a single shared instance manages all books, customers, and authentication across the application.

---

## Project Structure

```
phasefinal/
├── Main.java               # Entry point
├── BookstoreSystem.java    # Singleton system manager (auth, inventory, customers)
├── User.java               # Abstract base class for all users
├── Owner.java              # Owner role with admin permissions
├── Customer.java           # Customer role with points and state
├── CustomerState.java      # State interface
├── SilverState.java        # Silver tier behaviour
├── GoldState.java          # Gold tier behaviour
├── Book.java               # Book model
└── FileHandler.java        # File I/O for persisting books and customers
```

---

## How to Run

1. Clone the repository
2. Open in an IDE with JavaFX configured (e.g. IntelliJ IDEA or Eclipse)
3. Run `Main.java`
4. Login as **Owner** (`admin` / `admin`) or as any existing customer

> Books and customers are loaded from `book.txt` and `customer.txt` on startup and saved on exit.

---

## UML Class Diagram

The system consists of 12 classes and 1 interface. `Customer` and `Owner` both extend the abstract `User` class. The `BookstoreSystem` singleton coordinates all interactions between the GUI, business logic, and file persistence layers.

---

*Built as a final project for COE528 – Object Oriented Engineering Analysis and Design at Toronto Metropolitan University (2025)*
