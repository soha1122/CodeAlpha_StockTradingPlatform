import java.util.*;

class Stock {
    String name;
    double price;

    Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class User {
    double balance;
    HashMap<String, Integer> portfolio = new HashMap<>();

    User(double balance) {
        this.balance = balance;
    }

    void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;

        if (balance >= cost) {
            balance -= cost;
            portfolio.put(stock.name, portfolio.getOrDefault(stock.name, 0) + quantity);
            System.out.println("Bought " + quantity + " shares of " + stock.name);
        } else {
            System.out.println("Not enough balance!");
        }
    }

    void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.name, 0);

        if (owned >= quantity) {
            balance += stock.price * quantity;
            portfolio.put(stock.name, owned - quantity);
            System.out.println("Sold " + quantity + " shares of " + stock.name);
        } else {
            System.out.println("Not enough shares!");
        }
    }

    void showPortfolio() {
        System.out.println("\nPortfolio:");
        for (String stock : portfolio.keySet()) {
            System.out.println(stock + " : " + portfolio.get(stock));
        }
        System.out.println("Balance: " + balance);
    }
}

public class StockTradingApp {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Sample stocks
        Stock s1 = new Stock("Apple", 150);
        Stock s2 = new Stock("Tesla", 200);

        User user = new User(1000);

        while (true) {
            System.out.println("\n1. Buy\n2. Sell\n3. View Portfolio\n4. Exit");
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.println("1. Apple\n2. Tesla");
                int c = sc.nextInt();

                System.out.print("Quantity: ");
                int qty = sc.nextInt();

                if (c == 1) user.buyStock(s1, qty);
                else user.buyStock(s2, qty);

            } else if (choice == 2) {
                System.out.println("1. Apple\n2. Tesla");
                int c = sc.nextInt();

                System.out.print("Quantity: ");
                int qty = sc.nextInt();

                if (c == 1) user.sellStock(s1, qty);
                else user.sellStock(s2, qty);

            } else if (choice == 3) {
                user.showPortfolio();
            } else {
                break;
            }
        }
    }
}