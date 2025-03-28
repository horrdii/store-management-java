package classes;

import java.util.ArrayList;

public class User{

    public int id;
    public String firstName, secondName, amountOfMoney; 
    // public String strProducts;
    ArrayList<Pair> products = new ArrayList<Pair>();

    public User(int id, String firstName, String secondName, float amountOfMoney){
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.amountOfMoney = String.valueOf(amountOfMoney);
    } 

    public void addProduct(Product product, int count){
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).product == product){
                products.get(i).count += count;
                return;
            }
        }
        products.add(new Pair(product, count));
    }

    public void removeProduct(Product product){
        
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).product == product){
                products.remove(i);
                return;
            }
        }    
    
    }

    public String getProductsForSql(){
        String str = "";
        for(Pair p : products){
            str += p.product.name + " " + String.valueOf(p.count) + " "; 
        }
        return str;
    }

    public String getProducts(){

        if(products.size() == 0)
            return "";

        String str = "";
        for(Pair p : products){
            str += p.product.name + " - " + String.valueOf(p.count) + " "; 
        }
        return str;
    }

}

class Pair{

    public Product product;
    public Integer count;

    public Pair(Product p, Integer c){
        product = p;
        count = c;
    }

}