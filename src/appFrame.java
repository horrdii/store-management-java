import javax.swing.*;


import classes.DBControl;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import classes.*;

public class appFrame{

    //our db
    DBControl db = new DBControl();

    JFrame frame;
    JTextField textField, buyProductAmount;
    JPanel userPanel, productPanel, selectUserPanel, selectProductPanel, addUserPanel, addProductPanel, buyPanel;
    JLabel selectUserLabel, selectProductLabel;
    JLabel userPanelId, userPanelName, userPanelMoney, userPanelProducts, productPanelId, productPanelName, productPanelPrice;
    UsersCB usersBox;
    ProductsCB productsBox;
    JButton addUserButton, removeUserButton, addProductButton, removeProductButton, buyButton;

    //Create producs and users
    JTextField fN, sN, m, n, p;
    JLabel fNL, sNL, mL, nL, pL;

    
    //Show selected user and product
    User currentUser = null;
    Product currentProduct = null;

    double num1=0, num2=0, result = 0;
    char operator;

    appFrame(){

        //db.addUser(new User(db.lastUserIndex, "Ass", "ass", 12f));
        //db.addProduct(new Product(db.lastProductIndex, "ass", 12.0f));

        //Setting font
        setUIFont(new javax.swing.plaf.FontUIResource("Cabria Math", Font.BOLD, 13));

        //Creating window
        frame = new JFrame("Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 550);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        textField = new JTextField();
        textField.setBounds(50, 25, 300, 50);
        textField.setEditable(false);

        usersBox = new UsersCB(db.getUsers());
        usersBox.setSelectedIndex(-1);
        usersBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                currentUser = db.getUsers().get(Integer.parseInt(e.getItem().toString().split(":")[0]) - 1);
                setUserPanel();
            }
        });

        selectUserLabel = new JLabel("Select user");
        selectUserPanel = new JPanel();
        selectUserPanel.add(selectUserLabel);
        selectUserPanel.add(usersBox);
        selectUserPanel.setBounds(30, 50, 300, 50);

        userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(4, 2, 20, 10));
        userPanel.setBounds(30, 100, 300, 300);
        setUserPanel();


        addUserPanel = new JPanel(new GridLayout(4, 2));
        fN = new JTextField(26);
        sN = new JTextField(26);
        m = new JTextField(20);
        fNL = new JLabel("First name: ");
        sNL = new JLabel("Second name: ");
        mL = new JLabel("Money: ");
        addUserPanel.add(fNL);
        addUserPanel.add(fN);
        addUserPanel.add(sNL);
        addUserPanel.add(sN);
        addUserPanel.add(mL);
        addUserPanel.add(m);

        addUserButton = new JButton("Add user");
        addUserButton.setBounds(10, 450, 150, 50 );
        addUserButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, addUserPanel, "Add user", JOptionPane.QUESTION_MESSAGE);

                // if(p. == 1){
                //     JOptionPane.
                //     return;
                // }

                String firstName = fN.getText(), secondName = sN.getText(), money = m.getText();

                if(firstName == ""){
                    //actionPerformed(e, "Enter the first name");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }
                if(secondName == ""){
                    //actionPerformed(e, "Enter the second name");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }
                if(money != "" && money.matches("[a-zA-Z]+")){
                    //actionPerformed(e, "Enter numeric value in money field");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }

                try{
                    Float.parseFloat(money);
                }catch (Exception exc){
                    //actionPerformed(e, "Error occurred. Check your money input.");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }

                User user = new User(db.getLastUserIndex()+1, firstName, secondName, Float.parseFloat(money));

                if( db.addUser(user) != 1){
                    actionPerformed(e, "Error occurred. Check your input.");
                    return;
                }

                usersBox.Update(db.getUsers());;
                currentUser = null;
                setUserPanel();


                fN.setText("");
                sN.setText("");
                m.setText("");
            
            }
            
            public void actionPerformed(ActionEvent e, String problem) {
                JOptionPane.showMessageDialog(null, addUserPanel, problem, JOptionPane.QUESTION_MESSAGE);


                String firstName = fN.getText(), secondName = sN.getText(), money = m.getText();


                if(firstName == ""){
                    //actionPerformed(e, "Enter the first name");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }
                if(secondName == ""){
                    //actionPerformed(e, "Enter the second name");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }
                if(money == "" || money.matches("[a-zA-Z]+")){
                    //actionPerformed(e, "Enter numeric value in money field");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }

                try{
                    Float.parseFloat(money);
                }catch (Exception exc){
                    //actionPerformed(e, "Error occurred. Check your money input.");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }

                User user = new User(db.getLastUserIndex() + 1, firstName, secondName, Float.parseFloat(money));

                if( db.addUser(user) != 1){
                    actionPerformed(e, "Error occurred. Check your input.");
                    return;
                }

                usersBox.Update(db.getUsers());
                currentUser = null;
                setUserPanel();

                fN.setText("");
                sN.setText("");
                m.setText("");
            }
        });

        removeUserButton = new JButton("Remove user");
        removeUserButton.setBounds(170, 450, 150, 50);
        removeUserButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(usersBox.getSelectedItem().equals(null)){
                    return;
                }
                
                if(db.removeUser(currentUser) == 1){
                    usersBox.Update(db.getUsers());
                    currentUser = null;
                    setUserPanel();
                }else{
                    JOptionPane.showMessageDialog(null, "Error", null, JOptionPane.ERROR_MESSAGE);
                }
                
            }

        });


        productsBox = new ProductsCB(db.getProducts());
        productsBox.setSelectedIndex(-1);
        productsBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                currentProduct = db.getProducts().get(Integer.parseInt(e.getItem().toString().split(":")[0]) - 1);
                setProductPanel();
            }
        });

        selectProductLabel = new JLabel("Select product");
        selectProductPanel = new JPanel();
        selectProductPanel.add(selectProductLabel);
        selectProductPanel.add(productsBox);
        selectProductPanel.setBounds(400, 50, 300, 50);

        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(4, 2, 20, 10));
        productPanel.setBounds(400, 100, 300, 300);
        setProductPanel();


        addProductPanel = new JPanel(new GridLayout(2, 2));
        nL = new JLabel("Name: ");
        pL = new JLabel("Price: ");
        n = new JTextField(26);
        p = new JTextField(20);
        addProductPanel.add(nL);
        addProductPanel.add(n);
        addProductPanel.add(pL);
        addProductPanel.add(p);

        addProductButton = new JButton("Add product");
        addProductButton.setBounds(350, 450, 150, 50 );
        addProductButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, addProductPanel, "Add product", JOptionPane.QUESTION_MESSAGE);

                String name = n.getText(), price = p.getText();

                if(name == ""){
                    //actionPerformed(e, "Enter the first name");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }

                if(price == "" || price.matches("[a-zA-Z]+")){
                    //actionPerformed(e, "Enter numeric value in money field");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }

                try{
                    Float.parseFloat(price);
                }catch (Exception exc){
                    //actionPerformed(e, "Error occurred. Check your money input.");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }

                Product product = new Product(db.getLastProductIndex()+1, name, Float.parseFloat(price));

                if( db.addProduct(product) != 1){
                    //actionPerformed(e, "Error occurred. Check your input.");
                    JOptionPane.showMessageDialog(null, "Error. Check your input and try again", null, JOptionPane.ERROR_MESSAGE);

                    return;
                }

                productsBox.Update(db.getProducts());;
                currentProduct = null;
                setProductPanel();

                n.setText("");
                p.setText("");
            
            }
            
            
        });

        removeProductButton = new JButton("Remove product");
        removeProductButton.setBounds(510, 450, 150, 50);
        removeProductButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(productsBox.getSelectedItem().equals(null)){
                    return;
                }

                if(db.removeProduct(currentProduct) == 1){

                    productsBox.Update(db.getProducts());
                    currentProduct = null;
                    setProductPanel();
                    setUserPanel();
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Error", null, JOptionPane.ERROR_MESSAGE);
                }
                
            }

        });


        buyPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        buyProductAmount = new JTextField();

        buyButton = new JButton("Buy");
        buyButton.setBounds(750, 200, 200, 160);
        buyButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                if(currentUser != null && currentProduct != null){

                    buyPanel.removeAll();
                    buyPanel.add(new JLabel( "User " + currentUser.firstName + " " + currentUser.secondName + " buys " + currentProduct.name ));
                    buyPanel.add(new JLabel("Enter amount of product"));
                    buyProductAmount.setText("");
                    buyPanel.add(buyProductAmount);
                    
                    JOptionPane.showMessageDialog(null, buyPanel, "Buy" , JOptionPane.QUESTION_MESSAGE);

                    String amountStr = buyProductAmount.getText();

                    if(amountStr.length() == 0){
                        if( Float.parseFloat(currentProduct.price) <= Float.parseFloat(currentUser.amountOfMoney) ){
                            if(db.updateMoney(currentUser, currentProduct, 1) == -1){
                                JOptionPane.showMessageDialog(frame, "Error");
                            }
                            setUserPanel();
                            return;
                        }else{
                            JOptionPane.showMessageDialog(frame, "Not enough money for this purchase");
                            return;
                        }
                    }

                    if(!amountStr.matches("[0-9]+")){
                        actionPerformed(e, "Enter only integer numbers");
                        return;
                    }

                    int amount = 0;

                    try{
                        amount = Integer.parseInt(amountStr);
                    }catch(Exception exc){
                        JOptionPane.showMessageDialog(frame, "Error");
                        return;
                    }

                    if(Float.parseFloat(currentProduct.price) * amount <= Float.parseFloat(currentUser.amountOfMoney)){
                        if(db.updateMoney(currentUser, currentProduct, amount) == -1){
                            JOptionPane.showMessageDialog(frame, "Error");
                        }
                        setUserPanel();
                        return;
                    }else{
                        JOptionPane.showMessageDialog(frame, "Not enough money for this purchase");
                    }

                }else{
                    JOptionPane.showMessageDialog(frame, "Choose user and product");
                    return;
                }

            }

            public void actionPerformed(ActionEvent e, String Problem){

                JOptionPane.showMessageDialog(null, buyPanel, "Buy" , JOptionPane.QUESTION_MESSAGE);

                    String amountStr = buyProductAmount.getText();

                    if(amountStr.length() == 0){
                        if(Integer.parseInt(currentProduct.price) <= Integer.parseInt(currentUser.amountOfMoney)){
                            if(db.updateMoney(currentUser, currentProduct, 1) == -1){
                                JOptionPane.showMessageDialog(frame, "Error");
                            }
                            setUserPanel();
                            return;
                        }else{
                            JOptionPane.showMessageDialog(frame, "Not enough money for this purchase");
                            return;
                        }
                    }

                    if(!amountStr.matches("[0-9]+")){
                        actionPerformed(e, "Enter only integer numbers");
                        return;
                    }

                    int amount = 0;

                    try{
                        amount = Integer.parseInt(amountStr);
                    }catch(Exception exc){
                        JOptionPane.showMessageDialog(frame, "Error");
                        return;
                    }

                    if(Integer.parseInt(currentProduct.price) * amount <= Integer.parseInt(currentUser.amountOfMoney)){
                        if(db.updateMoney(currentUser, currentProduct, amount) == -1){
                            JOptionPane.showMessageDialog(frame, "Error");
                        }
                        setUserPanel();
                        return;
                    }else{
                        JOptionPane.showMessageDialog(frame, "Not enough money for this purchase");
                    }

            }
            
        });


        frame.add(selectUserPanel);
        frame.add(userPanel);
        frame.add(addUserButton);
        frame.add(removeUserButton);

        frame.add(selectProductPanel);
        frame.add(productPanel);
        frame.add(addProductButton);
        frame.add(removeProductButton);

        frame.add(buyButton);

        frame.setVisible(true);

    }

    // public static void main(String[] args){

    //     appFrame app = new appFrame();
        
    // }

    // @Override
    // public void actionPerformed(ActionEvent e){
    //     return;

    // }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
          Object key = keys.nextElement();
          Object value = UIManager.get (key);
          if (value instanceof javax.swing.plaf.FontUIResource)
            UIManager.put (key, f);
        }
    } 

    void setUserPanel(){
        if(userPanel.getComponents().length > 0 && currentUser != null){
            userPanelId.setText(String.valueOf(currentUser.id));
            userPanelName.setText(currentUser.firstName + " " + currentUser.secondName);
            userPanelMoney.setText(currentUser.amountOfMoney);
            userPanelProducts.setText(currentUser.getProducts());
            return;
        }

        userPanel.removeAll();

        userPanelId = new JLabel("");
        userPanelName = new JLabel("");
        userPanelMoney = new JLabel("");
        userPanelProducts = new JLabel("");

        userPanel.add(new JLabel("ID"));
        userPanel.add(userPanelId);

        userPanel.add(new JLabel("Name"));
        userPanel.add(userPanelName);

        userPanel.add(new JLabel("Money"));
        userPanel.add(userPanelMoney);

        userPanel.add(new JLabel("Products"));
        userPanel.add(userPanelProducts);

    }

    void setProductPanel(){
        
        if(productPanel.getComponents().length > 0 && currentProduct != null){
            productPanelId.setText(String.valueOf(currentProduct.id));
            productPanelName.setText(currentProduct.name);
            productPanelPrice.setText(currentProduct.price);
            return;
        }

        productPanel.removeAll();

        productPanelId = new JLabel("");
        productPanelName = new JLabel("");
        productPanelPrice = new JLabel("");

        productPanel.add(new JLabel("ID"));
        productPanel.add(productPanelId);

        productPanel.add(new JLabel("Name"));
        productPanel.add(productPanelName);

        productPanel.add(new JLabel("Price"));
        productPanel.add(productPanelPrice);

    }

    // @Override
    // public void actionPerformed(ActionEvent e) {
        
    // }

    // @Override
    // public void itemStateChanged(ItemEvent e)
    // {
        
    // }

}

class UsersCB extends JComboBox<String>{
    ArrayList<User> al;

    UsersCB(ArrayList<User> al){
        super();
        this.setModel(new DefaultComboBoxModel<>());
        this.al = al;
        for(User u : al){
            this.addItem(String.valueOf(u.id) + ": " + u.firstName + " " + u.secondName);
        }
    }

    public void Update(ArrayList<User> users){

        try{
            this.al = users;
            DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<String>();
            for(User u : users){
                dcbm.addElement(String.valueOf(u.id) + ": " + u.firstName + " " + u.secondName);
            }
            this.setModel(dcbm);

            this.setSelectedIndex(-1);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Not enough money for this purchase", null, JOptionPane.ERROR_MESSAGE);
        }

    }
   

}

class ProductsCB extends JComboBox<String>{
    ArrayList<Product> al;

    ProductsCB(ArrayList<Product> al){
        super();
        this.al = al;
        for(Product p : al){
            this.addItem(String.valueOf(p.id) + ": " + p.name);
        }
    }

    public void Update(ArrayList<Product> products){

        try{
            this.al = products;
            DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<String>();
            for(Product p : products){
                dcbm.addElement(String.valueOf(p.id) + ": " + p.name);
            }
            this.setModel(dcbm);

            this.setSelectedIndex(-1);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Not enough money for this purchase", null, JOptionPane.ERROR_MESSAGE);
        }

    }

}
