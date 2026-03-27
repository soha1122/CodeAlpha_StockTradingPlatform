import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        price += (Math.random() * 10 - 5);
        if(price < 1) price = 1;
    }
}

class User {
    double balance = 1000;
    HashMap<String, Integer> portfolio = new HashMap<>();
    HashMap<String, Double> invested = new HashMap<>();
}

public class StockGUI {

    static User user = new User();
    static ArrayList<Stock> stocks = new ArrayList<>();

    public static void main(String[] args) {

        // ===== MORE STOCKS ADDED =====
        stocks.add(new Stock("Apple",150));
        stocks.add(new Stock("Tesla",200));
        stocks.add(new Stock("Google",180));
        stocks.add(new Stock("Amazon",170));
        stocks.add(new Stock("Meta",160));
        stocks.add(new Stock("Netflix",140));
        stocks.add(new Stock("Nvidia",220));

        JFrame frame = new JFrame("Stock Trading System");
        frame.setSize(900,600);
        frame.setLayout(new BorderLayout());

        // ===== NAVIGATION BAR =====
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        JButton marketBtn = new JButton("Market Data");
        JButton tradeBtn = new JButton("Buy / Sell");
        JButton portfolioBtn = new JButton("Portfolio");

        navBar.add(marketBtn);
        navBar.add(tradeBtn);
        navBar.add(portfolioBtn);

        frame.add(navBar,BorderLayout.NORTH);

        // ===== MAIN PANEL (CARD LAYOUT) =====
        JPanel mainPanel = new JPanel(new CardLayout());

        // ================= PAGE 1: MARKET =================
        JPanel marketPanel = new JPanel(new BorderLayout());

        String cols[] = {"Stock","Price"};
        DefaultTableModel model = new DefaultTableModel(cols,0);
        JTable table = new JTable(model);

        JButton refreshBtn = new JButton("Refresh Prices");

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for(Stock s:stocks){
                s.updatePrice();
                model.addRow(new Object[]{s.name, (int)s.price});
            }
        });

        refreshBtn.doClick();

        marketPanel.add(new JLabel("Market Data",JLabel.CENTER),BorderLayout.NORTH);
        marketPanel.add(new JScrollPane(table),BorderLayout.CENTER);
        marketPanel.add(refreshBtn,BorderLayout.SOUTH);

        // ================= PAGE 2: BUY/SELL =================
        JPanel tradePanel = new JPanel(new GridLayout(8,1,5,5));

        JTextField stockField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField balanceField = new JTextField();

        JButton buyBtn = new JButton("Buy");
        JButton sellBtn = new JButton("Sell");
        JButton addBalanceBtn = new JButton("Add Balance");
        JButton searchBtn = new JButton("Search Stock");

        tradePanel.add(new JLabel("Stock Name"));
        tradePanel.add(stockField);
        tradePanel.add(new JLabel("Quantity"));
        tradePanel.add(qtyField);
        tradePanel.add(buyBtn);
        tradePanel.add(sellBtn);
        tradePanel.add(new JLabel("Add Balance"));
        tradePanel.add(balanceField);
        tradePanel.add(addBalanceBtn);
        tradePanel.add(searchBtn);

        // ================= PAGE 3: PORTFOLIO =================
        JPanel portfolioPanel = new JPanel(new BorderLayout());

        JTextArea portfolioArea = new JTextArea();
        JLabel balanceLabel = new JLabel("Balance: " + user.balance);

        JButton saveBtn = new JButton("Save Data");

        portfolioPanel.add(balanceLabel,BorderLayout.NORTH);
        portfolioPanel.add(new JScrollPane(portfolioArea),BorderLayout.CENTER);
        portfolioPanel.add(saveBtn,BorderLayout.SOUTH);

        // ===== FUNCTION =====
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

                text += s + " : " + qty + " = " + (int)value + "\n";

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

        // ===== SAVE =====
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

        // ===== ADD PAGES =====
        mainPanel.add(marketPanel,"market");
        mainPanel.add(tradePanel,"trade");
        mainPanel.add(portfolioPanel,"portfolio");

        frame.add(mainPanel,BorderLayout.CENTER);

        CardLayout cl = (CardLayout)(mainPanel.getLayout());

        marketBtn.addActionListener(e -> cl.show(mainPanel,"market"));
        tradeBtn.addActionListener(e -> cl.show(mainPanel,"trade"));
        portfolioBtn.addActionListener(e -> cl.show(mainPanel,"portfolio"));

        frame.setVisible(true);
    }
}