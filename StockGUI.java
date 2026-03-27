import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.*;

class Stock {
    String name;
    double price;

    Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }

    void updatePrice() {
        price += (Math.random() * 10 - 5); // dynamic price
        if(price < 1) price = 1;
    }
}

class User {
    double balance = 1000;
    HashMap<String, Integer> portfolio = new HashMap<>();
    HashMap<String, Double> invested = new HashMap<>();
    ArrayList<String> history = new ArrayList<>();
}

public class StockGUI {

    static User user = new User();
    static ArrayList<Stock> stocks = new ArrayList<>();

    public static void main(String[] args) {

        // sample stocks
        stocks.add(new Stock("Apple",150));
        stocks.add(new Stock("Tesla",200));
        stocks.add(new Stock("Google",180));

        JFrame frame = new JFrame("Stock Trading System");
        frame.setSize(900,600);
        frame.setLayout(new BorderLayout());

        // ===== MARKET TABLE =====
        String cols[] = {"Stock","Price"};
        DefaultTableModel model = new DefaultTableModel(cols,0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        // ===== UPDATE MARKET =====
        JButton refreshBtn = new JButton("Refresh Prices");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for(Stock s:stocks){
                s.updatePrice();
                model.addRow(new Object[]{s.name, (int)s.price});
            }
        });

        // initial load
        refreshBtn.doClick();

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Market Data",JLabel.CENTER),BorderLayout.NORTH);
        leftPanel.add(scroll,BorderLayout.CENTER);
        leftPanel.add(refreshBtn,BorderLayout.SOUTH);

        // ===== CENTER PANEL =====
        JPanel center = new JPanel(new GridLayout(8,1,5,5));

        JTextField stockField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField balanceField = new JTextField();

        JButton buyBtn = new JButton("Buy");
        JButton sellBtn = new JButton("Sell");
        JButton addBalanceBtn = new JButton("Add Balance");
        JButton searchBtn = new JButton("Search Stock");

        center.add(new JLabel("Stock Name"));
        center.add(stockField);
        center.add(new JLabel("Quantity"));
        center.add(qtyField);
        center.add(buyBtn);
        center.add(sellBtn);
        center.add(new JLabel("Add Balance"));
        center.add(balanceField);
        center.add(addBalanceBtn);
        center.add(searchBtn);

        // ===== RIGHT PANEL (PORTFOLIO) =====
        JTextArea portfolioArea = new JTextArea();
        JScrollPane portfolioScroll = new JScrollPane(portfolioArea);

        JLabel balanceLabel = new JLabel("Balance: " + user.balance);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(balanceLabel,BorderLayout.NORTH);
        rightPanel.add(portfolioScroll,BorderLayout.CENTER);

        // ===== FUNCTIONS =====

        Runnable updatePortfolio = () -> {
            String text = "";
            double totalValue = 0;
            double investedTotal = 0;

            for(String s:user.portfolio.keySet()){
                int qty = user.portfolio.get(s);
                double price = 0;

                for(Stock st:stocks){
                    if(st.name.equalsIgnoreCase(s)) price = st.price;
                }

                double value = qty * price;
                double invested = user.invested.getOrDefault(s,0.0);

                text += s + " : " + qty + " shares = " + (int)value + "\n";

                totalValue += value;
                investedTotal += invested;
            }

            double profit = totalValue - investedTotal;

            text += "\nTotal Value: " + (int)totalValue;
            text += "\nProfit/Loss: " + (int)profit;

            portfolioArea.setText(text);
            balanceLabel.setText("Balance: " + (int)user.balance);
        };

        // ===== BUY =====
        buyBtn.addActionListener(e -> {
            try{
                String name = stockField.getText();
                int qty = Integer.parseInt(qtyField.getText());

                if(qty <= 0) throw new Exception();

                for(Stock s:stocks){
                    if(s.name.equalsIgnoreCase(name)){
                        double cost = s.price * qty;

                        if(user.balance >= cost){
                            user.balance -= cost;
                            user.portfolio.put(name,
                                    user.portfolio.getOrDefault(name,0)+qty);

                            user.invested.put(name,
                                    user.invested.getOrDefault(name,0.0)+cost);

                            user.history.add("Bought "+qty+" "+name);
                        } else {
                            JOptionPane.showMessageDialog(frame,"Not enough balance");
                        }
                    }
                }

                updatePortfolio.run();

            }catch(Exception ex){
                JOptionPane.showMessageDialog(frame,"Invalid input");
            }
        });

        // ===== SELL =====
        sellBtn.addActionListener(e -> {
            try{
                String name = stockField.getText();
                int qty = Integer.parseInt(qtyField.getText());

                int owned = user.portfolio.getOrDefault(name,0);

                if(owned >= qty){
                    for(Stock s:stocks){
                        if(s.name.equalsIgnoreCase(name)){
                            user.balance += s.price * qty;
                            user.portfolio.put(name, owned-qty);
                            user.history.add("Sold "+qty+" "+name);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,"Not enough shares");
                }

                updatePortfolio.run();

            }catch(Exception ex){
                JOptionPane.showMessageDialog(frame,"Invalid input");
            }
        });

        // ===== ADD BALANCE =====
        addBalanceBtn.addActionListener(e -> {
            try{
                double amount = Double.parseDouble(balanceField.getText());
                if(amount <= 0) throw new Exception();

                user.balance += amount;
                updatePortfolio.run();

            }catch(Exception ex){
                JOptionPane.showMessageDialog(frame,"Invalid amount");
            }
        });

        // ===== SEARCH =====
        searchBtn.addActionListener(e -> {
            String name = stockField.getText();

            for(Stock s:stocks){
                if(s.name.equalsIgnoreCase(name)){
                    JOptionPane.showMessageDialog(frame,
                            "Price of "+name+" = "+(int)s.price);
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame,"Stock not found");
        });

        // ===== SAVE DATA =====
        JButton saveBtn = new JButton("Save Data");
        saveBtn.addActionListener(e -> {
            try{
                FileWriter fw = new FileWriter("portfolio.txt");
                fw.write(portfolioArea.getText());
                fw.close();
                JOptionPane.showMessageDialog(frame,"Saved!");
            }catch(Exception ex){
                JOptionPane.showMessageDialog(frame,"Error saving file");
            }
        });

        rightPanel.add(saveBtn,BorderLayout.SOUTH);

        // ===== ADD PANELS =====
        frame.add(leftPanel,BorderLayout.WEST);
        frame.add(center,BorderLayout.CENTER);
        frame.add(rightPanel,BorderLayout.EAST);

        frame.setVisible(true);
    }
}