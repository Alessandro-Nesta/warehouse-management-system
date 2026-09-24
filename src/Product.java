public class Product implements Comparable<Product>{
    private int id;
    private String name;
    private double price;
    private int stock;
    private Category category;

    public Product(int id , String name , double price , int stock , Category category){
        this.id = id;
        this.name = name;
        setPrice(price);
        setStock(stock);
        this.category = category;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public int getStock(){
        return this.stock;
    }

    public Category getCategory(){
        return this.category;
    }

    public void setPrice(double price){
        if(price <= 0){
            throw new IllegalArgumentException("价格不合法！");
        }
        this.price = price;
    }

    public void setStock(int stock){
        if(stock < 0){
            throw new IllegalArgumentException("库存不合法！");
        }
        this.stock = stock;
    }

    @Override
    public String toString(){
        return "ID:" + id + " 类别:" + category + " 名称:" + name + " 价格:" + price + " 库存:" + stock;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return  true;
        }

        if(!(obj instanceof Product)){
            return false;
        }

        Product other = (Product) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public int compareTo(Product other){
        if(this.getId() < other.getId()){
            return -1;
        }

        if(this.getId() > other.getId()){
            return 1;
        }
        return 0;
    }
}
