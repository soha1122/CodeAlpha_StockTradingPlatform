import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    ArrayList<String> history = new ArrayList<>();
}

public class StockGUI {

    static User user = new User();
    static ArrayList<Stock> stocks = new ArrayList<>();

    // 💙 NAVY BLUE THEME
    static Color bgColor = new Color(10,25,47);
    static Color panelColor = new Color(17,34,64);
    static Color textColor = Color.WHITE;
    static Color accent = new Color(31,111,235);
    static Color accentHover = new Color(60,140,255);

    public static void main(String[] args) {

        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        loginFrame.setSize((int)(screenSize.width * 0.3),(int)(screenSize.height * 0.4));
        loginFrame.setLocationRelativeTo(null);

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(bgColor);

        JPanel panel = new JPanel(new GridLayout(5,1,10,10));
        panel.setPreferredSize(new Dimension(250,180));
        panel.setBackground(panelColor);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        usernameField.setBackground(new Color(20,35,60));
        usernameField.setForeground(Color.WHITE);

        passwordField.setBackground(new Color(20,35,60));
        passwordField.setForeground(Color.WHITE);

        loginBtn.setBackground(accent);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e){ loginBtn.setBackground(accentHover); }
            public void mouseExited(java.awt.event.MouseEvent e){ loginBtn.setBackground(accent); }
        });

        JLabel l1 = new JLabel("Username"); l1.setForeground(Color.WHITE);
        JLabel l2 = new JLabel("Password"); l2.setForeground(Color.WHITE);

        panel.add(l1);
        panel.add(usernameField);
        panel.add(l2);
        panel.add(passwordField);
        panel.add(loginBtn);

        outerPanel.add(panel);
        loginFrame.add(outerPanel);
        loginFrame.setVisible(true);

        loginBtn.addActionListener(e -> {
            if(usernameField.getText().equals("admin") && new String(passwordField.getPassword()).equals("1234")){
                loginFrame.dispose();
                openMainApp();
            } else {
                JOptionPane.showMessageDialog(loginFrame,"❌ Invalid Login");
            }
        });
    }

    public static void openMainApp(){

        // STOCKS
        stocks.add(new Stock("Apple",150));
        stocks.add(new Stock("Tesla",200));
        stocks.add(new Stock("Google",180));
        stocks.add(new Stock("Amazon",170));
        stocks.add(new Stock("Meta",160));
        stocks.add(new Stock("Netflix",140));
        stocks.add(new Stock("Nvidia",220));
        stocks.add(new Stock("Microsoft",210));
        stocks.add(new Stock("Intel",120));
        stocks.add(new Stock("AMD",130));
        stocks.add(new Stock("Uber",90));
        stocks.add(new Stock("Airbnb",110));
        stocks.add(new Stock("Spotify",125));
        stocks.add(new Stock("PayPal",135));
        stocks.add(new Stock("Adobe",190));
        stocks.add(new Stock("Snapchat",80));
        stocks.add(new Stock("Twitter",95));
        stocks.add(new Stock("Shopify",145));
        stocks.add(new Stock("Zoom",115));
        stocks.add(new Stock("Oracle",155));

        JFrame frame = new JFrame("Stock Trading System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int)(screenSize.width * 0.8),(int)(screenSize.height * 0.8));
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(bgColor);

        // NAVBAR
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        navBar.setBackground(panelColor);

        JButton marketBtn = new JButton("Market Data");
        JButton tradeBtn = new JButton("Buy / Sell");
        JButton portfolioBtn = new JButton("Portfolio");

        JButton[] navButtons = {marketBtn,tradeBtn,portfolioBtn};
        for(JButton b:navButtons){
            b.setBackground(accent);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(accentHover); }
                public void mouseExited(java.awt.event.MouseEvent e){ b.setBackground(accent); }
            });
        }

        navBar.add(marketBtn);
        navBar.add(tradeBtn);
        navBar.add(portfolioBtn);
        frame.add(navBar,BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new CardLayout());

        // MARKET
        JPanel marketPanel = new JPanel(new BorderLayout());
        marketPanel.setBackground(bgColor);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Stock","Price"},0);
        JTable table = new JTable(model);

        table.setBackground(new Color(20,35,60));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.WHITE);
        table.setRowHeight(25);

        JButton refreshBtn = new JButton("Refresh Prices");
        refreshBtn.setBackground(accent);
        refreshBtn.setForeground(Color.WHITE);

        refreshBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e){ refreshBtn.setBackground(accentHover); }
            public void mouseExited(java.awt.event.MouseEvent e){ refreshBtn.setBackground(accent); }
        });

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for(Stock s:stocks){
                s.updatePrice();
                model.addRow(new Object[]{s.name,(int)s.price});
            }
        });

        refreshBtn.doClick();

        JLabel marketTitle = new JLabel("Market Data",JLabel.CENTER);
        marketTitle.setForeground(Color.WHITE);

        marketPanel.add(marketTitle,BorderLayout.NORTH);
        marketPanel.add(new JScrollPane(table),BorderLayout.CENTER);
        marketPanel.add(refreshBtn,BorderLayout.SOUTH);

        // TRADE
        JPanel tradePanel = new JPanel(new BorderLayout(10,10));
        tradePanel.setBackground(bgColor);

        JLabel title = new JLabel("BUY / SELL STOCKS",JLabel.CENTER);
        title.setForeground(Color.WHITE);

        JPanel centerPanel = new JPanel(new GridLayout(1,2,20,10));
        centerPanel.setBackground(bgColor);

        JPanel left = new JPanel(new GridLayout(6,1,10,10));
        left.setBackground(panelColor);

        JComboBox<String> stockBox = new JComboBox<>();
        for(Stock s:stocks) stockBox.addItem(s.name);

        JTextField qtyField = new JTextField("Enter quantity...");
        qtyField.setBackground(new Color(20,35,60));
        qtyField.setForeground(Color.WHITE);

        JButton buyBtn = new JButton("Buy");
        JButton sellBtn = new JButton("Sell");

        buyBtn.setBackground(new Color(76,175,80));
        sellBtn.setBackground(new Color(244,67,54));
        buyBtn.setForeground(Color.WHITE);
        sellBtn.setForeground(Color.WHITE);

        buyBtn.setEnabled(false);
        sellBtn.setEnabled(false);

        JLabel selectLabel = new JLabel("Select Stock"); selectLabel.setForeground(Color.WHITE);
        JLabel qtyLabel = new JLabel("Quantity"); qtyLabel.setForeground(Color.WHITE);

        left.add(selectLabel);
        left.add(stockBox);
        left.add(qtyLabel);
        left.add(qtyField);
        left.add(buyBtn);
        left.add(sellBtn);

        JPanel right = new JPanel(new GridLayout(5,1,10,10));
        right.setBackground(panelColor);

        JLabel priceLabel = new JLabel("Price: "); priceLabel.setForeground(Color.WHITE);
        JLabel balanceLabel = new JLabel("Balance: " + user.balance); balanceLabel.setForeground(Color.WHITE);
        JLabel totalLabel = new JLabel("Total: "); totalLabel.setForeground(Color.WHITE);

        JTextArea historyArea = new JTextArea();
        historyArea.setBackground(new Color(20,35,60));
        historyArea.setForeground(Color.WHITE);

        JLabel transLabel = new JLabel("Recent Transactions"); transLabel.setForeground(Color.WHITE);

        right.add(priceLabel);
        right.add(balanceLabel);
        right.add(totalLabel);
        right.add(transLabel);
        right.add(new JScrollPane(historyArea));

        centerPanel.add(left);
        centerPanel.add(right);

        tradePanel.add(title,BorderLayout.NORTH);
        tradePanel.add(centerPanel,BorderLayout.CENTER);

        // PORTFOLIO
        JPanel portfolioPanel = new JPanel(new BorderLayout());
        portfolioPanel.setBackground(bgColor);

        JPanel topPortfolio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPortfolio.setBackground(panelColor);

        JLabel balanceText = new JLabel("Current Balance: " + user.balance);
        balanceText.setForeground(Color.WHITE);

        JButton addBalanceBtn = new JButton("Add Balance");
        addBalanceBtn.setBackground(accent);
        addBalanceBtn.setForeground(Color.WHITE);

        topPortfolio.add(balanceText);
        topPortfolio.add(addBalanceBtn);

        JTextArea portfolioArea = new JTextArea();
        portfolioArea.setBackground(new Color(20,35,60));
        portfolioArea.setForeground(Color.WHITE);

        JTextArea totalArea = new JTextArea();
        totalArea.setBackground(new Color(20,35,60));
        totalArea.setForeground(Color.WHITE);

        portfolioPanel.add(topPortfolio,BorderLayout.NORTH);
        portfolioPanel.add(new JScrollPane(portfolioArea),BorderLayout.CENTER);
        portfolioPanel.add(totalArea,BorderLayout.SOUTH);

        mainPanel.add(marketPanel,"market");
        mainPanel.add(tradePanel,"trade");
        mainPanel.add(portfolioPanel,"portfolio");

        frame.add(mainPanel,BorderLayout.CENTER);

        CardLayout cl = (CardLayout)mainPanel.getLayout();

        marketBtn.addActionListener(e -> cl.show(mainPanel,"market"));
        tradeBtn.addActionListener(e -> cl.show(mainPanel,"trade"));
        portfolioBtn.addActionListener(e -> cl.show(mainPanel,"portfolio"));

        frame.setVisible(true);
    }
}