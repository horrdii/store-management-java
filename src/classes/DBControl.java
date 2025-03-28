package classes;

import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DBControl{

    boolean SQLconnected = false;
    Connection connection = null;
    ArrayList<Product> products = new ArrayList<Product>() {};
    ArrayList<User> users = new ArrayList<User>();
    ArrayList<String> productsStr = new ArrayList<String>() {};

    int lastUserIndex = 0, lastProductIndex = 0;


    public int getLastProductIndex() {
        return lastProductIndex;
    }

    public void setLastProductIndex(int lastProductIndex) {
        this.lastProductIndex = lastProductIndex;
    }

    public int getLastUserIndex() {
        return lastUserIndex;
    }

    public void setLastUserIndex(int lastUserIndex) {
        this.lastUserIndex = lastUserIndex;
    }

    public Product getProductFromName(String name){

        for(int i = 0; i < products.size(); i++){

            if(products.get(i).name == name){
                return products.get(i);
            }

        }

        return null;
    }

    public DBControl(){
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306",
                "root", "");

            String t = "CREATE DATABASE IF NOT EXISTS IntelliStore";
            PreparedStatement statement = connection.prepareStatement(t);
            statement.executeUpdate();

            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/IntelliStore",
                "root", "");
            
            SQLconnected = true;

            t = "CREATE TABLE IF NOT EXISTS Products(id int NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50) UNIQUE NOT NULL, price FLOAT NOT NULL)";
            statement = connection.prepareStatement(t);
            statement.executeUpdate();

            t = "CREATE TABLE IF NOT EXISTS Users(id int NOT NULL AUTO_INCREMENT PRIMARY KEY, firstName VARCHAR(26) NOT NULL, secondName VARCHAR(26) NOT NULL, money FLOAT NOT NULL, boughtProducts VARCHAR(300))";
            statement = connection.prepareStatement(t);
            statement.executeUpdate();


            ResultSet result = statement.executeQuery("SELECT * FROM users");
            User u;

            productsStr.clear();

            while(result.next()){
                u = new User(result.getInt("id"), result.getString("firstName"),
                                result.getString("secondName"), result.getFloat("money"));
                users.add(u);
                lastUserIndex++;

                String prodStr = result.getString("boughtProducts");
                if(prodStr == null) productsStr.add("");
                else productsStr.add(prodStr);
            }

            result = statement.executeQuery("SELECT * FROM products");
            Product p;
            while(result.next()){
                p = new Product(result.getInt("id"), result.getString("name"),
                                    result.getFloat("price"));
                products.add(p);
                lastProductIndex++;
            }

            if(productsStr.size() > 1){

                for(int i = 0; i < productsStr.size(); i++){

                    String[] str = productsStr.get(i).split("\\s+");
                    if(str.length > 0){

                        Product iP = getProductFromName( str[0] );

                        if(iP != null)
                            users.get(i).addProduct(iP, Integer.parseInt(str[1]));

                    }
                    
                }

            }

            // for(int i = 0; i < users.size(); i++){

            //     if(users.get(i).strProducts == null) continue;

            //     String[] s = users.get(i).strProducts.split("\\s+");
            //     for(int j = 1; j < s.length; j+=3){

            //         for(Product prod : products){
            //             if(prod.name.equals(s[j])){
            //                 users.get(i).addProduct(prod, Integer.parseInt(s[j+2].substring(0, s[j+2].length()-1)));
            //             }
            //         }

            //     }

            // }


        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }

    protected void finalize(){
        try{
        if(SQLconnected)
            connection.close();
        }catch (SQLException e){
            return;
        }
    }

    public int addUser(User u){
        try{
            String t = "INSERT INTO users( firstName, secondName, money ) VALUES ( '" + u.firstName + "', '" + u.secondName + "', " + Float.parseFloat(u.amountOfMoney) + ");";
            PreparedStatement statement = connection.prepareStatement(t);
            statement.executeUpdate();

            if(!users.contains(u)){
                users.add(u);
                lastUserIndex++;
            }

            return 1;
        }catch(SQLException e){
            return -1;
        }
    }

    public int removeUser(User u){
        try{
            while(users.contains(u)){
                users.remove(u);
            }
            String t = "TRUNCATE TABLE users";
            PreparedStatement statement = connection.prepareStatement(t);
            statement.executeUpdate();
            int size = users.size();

            for(int i = 0; i < size; i++){
                users.get(i).id = i + 1;
                addUser(users.get(i));
            }

            lastUserIndex--;
            return 1;
        } catch(SQLException e){
            return -1;
        }
    }

    public int addProduct(Product p){
        try{
            String t = "INSERT INTO products( Name, price ) VALUES ( '" + p.name + "', " + p.price + ");";
            PreparedStatement statement = connection.prepareStatement(t);
            statement.executeUpdate();

            if(!products.contains(p)){
                products.add(p);
                lastProductIndex++;   
            }

            return 1;
        }catch(SQLException e){
            return -1;
        }
    }

    public int removeProduct(Product p){
        try{
            products.remove(p.id - 1);
            String t = "TRUNCATE TABLE products";
            PreparedStatement statement = connection.prepareStatement(t);
            statement.executeUpdate();

            int size = products.size();
            for(int i = 0; i < size; i++){
                Product product = products.get(i);
                addProduct(product);
                product.id = product.id < p.id ? product.id : --product.id;
            }

            for(User u : p.usersWithProduct){
                u.removeProduct(p);

                t = "UPDATE users SET boughtProducts = '" + u.getProductsForSql() + "' WHERE users.id = '" + String.valueOf(u.id) + "';";
                statement = connection.prepareStatement(t);
                statement.executeUpdate();

            }

            lastProductIndex--;
            return 1;
        } catch(SQLException e){
            return -1;
        }
    }

    public int updateMoney(User u, Product p, int amount){
        try{
            String t = "UPDATE users SET money = " + String.valueOf(Float.parseFloat(u.amountOfMoney) - Float.parseFloat(p.price) * amount) + " WHERE users.id = " + String.valueOf(u.id) + ";";
            PreparedStatement statement = connection.prepareStatement(t);
            statement.executeUpdate();
            //UPDATE users SET boughtProducts = boughtProducts + ' bak' + ' 1' WHERE users.id = 1;
            t = "UPDATE users SET boughtProducts = '" + u.getProductsForSql() + "' WHERE users.id = '" + String.valueOf(u.id) + "';";
            statement = connection.prepareStatement(t);
            statement.executeUpdate();

            users.get(u.id-1).amountOfMoney = String.valueOf( Float.parseFloat(users.get(u.id-1).amountOfMoney) - Float.parseFloat(p.price) * amount);
            users.get(u.id-1).addProduct(p, amount);

            p.addCustomer(u);

            return 1;
        } catch(SQLException e){
            return -1;
        }
    }

    public ArrayList<Product> getProducts(){
        return products;

    }

    public ArrayList<User> getUsers(){
        return users;

    }

    public String[] getUsernames(){
        String[] usernames = new String[users.size()];
        for(int i = 0; i < users.size(); i++){
            User user = users.get(i);
            usernames[i] = user.firstName + ' ' + user.secondName; 
        }
        return usernames;
    }

    public String[] getProductNames(){
        String[] productNames = new String[products.size()];
        for(int i = 0; i < products.size(); i++){
            Product product = products.get(i);
            productNames[i] = product.name; 
        }
        return productNames;
    }

}