package classes;

import java.util.ArrayList;

public class Product{

    public int id;
    public String name, price;
    ArrayList<User> usersWithProduct = new ArrayList<User>();

    public Product(int id, String name, Float price){
        this.id = id;
        this.name = name;
        this.price = String.valueOf(price);
    }

    public void addCustomer(User user){
        usersWithProduct.add(user);
    }

}