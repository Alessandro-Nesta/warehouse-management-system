import java.io.BufferedWriter;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;

public class WarehouseManager {
    private Map <Integer , Product> productMap = new HashMap<>();

    public void addProduct(Product product){
        if(productMap.containsKey(product.getId())){
            throw new DuplicateProductException("该ID已存在！");
        }
        productMap.put(product.getId() , product);
    }

    public Product findProductById(int id){
        if(!productMap.containsKey(id)){
            throw new ProductNotFoundException("抱歉！未找到该商品！");
        }
        return productMap.get(id);
    }

    public void removeProduct(int id){
        findProductById(id);
        productMap.remove(id);
    }

    public void showAllProducts(){
        if(productMap.size() == 0){
           System.out.println("当前仓库没有商品！");
        }
        else {
            for (Product product : productMap.values()) {
                System.out.println(product);
            }
        }
    }

    public void increaseStock(int id , int amount){
        if(amount <= 0){
            throw new IllegalArgumentException("入库数量必须大于0！");
        }
        Product product = findProductById(id);
        product.setStock(product.getStock() + amount);
    }

    public void decreaseStock(int id , int amount){
        if(amount <= 0){
            throw new IllegalArgumentException("出库数量必须大于0！");
        }
        Product product = findProductById(id);
        if(product.getStock() < amount){
            throw new InsufficientStockException("库存不足！");
        }
        product.setStock(product.getStock() - amount);
    }

    public void showProductsSortedById(){
        List<Product> productList = new ArrayList<>();
        for(Product product : productMap.values()){
            productList.add(product);
        }
        Collections.sort(productList);
        for(int i = 0 ; i < productList.size() ; i++){
            System.out.println(productList.get(i));
        }
    }

    public void showProductsSortedByPrice(){
        Comparator<Product> comparator = (product1 , product2) ->
                Double.compare(product1.getPrice() , product2.getPrice());
        List<Product> productList = new ArrayList<>();
        for(Product product : productMap.values()){
            productList.add(product);
        }
        Collections.sort(productList , comparator);
        for(int i = 0 ; i < productList.size() ; i++){
            System.out.println(productList.get(i));
        }
    }

    public void showLowStockProducts(int threshold){
        if(threshold < 0){
            throw new IllegalArgumentException("阈值不能小于0！");
        }
        productMap.values().stream()
                .filter(product -> product.getStock() < threshold)
                .forEach(System.out::println);
    }

    public long countLowStockProducts(int threshold){
        if(threshold < 0){
            throw new IllegalArgumentException("阈值不能小于0！");
        }
       return productMap.values().stream()
               .filter(product -> product.getStock() < threshold)
               .count();
    }

    public boolean hasCategory(Category category){
        return productMap.values().stream()
                .anyMatch(product -> product.getCategory() == category);
    }

    public Optional<Product> findFirstByCategory(Category category){
        return productMap.values().stream()
                .filter(product -> product.getCategory() == category)
                .findFirst();
    }

    public void saveToFile(Path path) throws IOException{
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(Product product : productMap.values()){
                writer.write(product.getId()
                        + "|" + product.getName()
                        + "|" + product.getPrice()
                        + "|" + product.getStock()
                        + "|" + product.getCategory());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(Path path) throws IOException{
        try(BufferedReader reader = Files.newBufferedReader(path)){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split("\\|");
                if(parts.length != 5){
                    System.out.println("该条数据格式有误！已跳过！");
                    continue;
                }
                try{
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int stock = Integer.parseInt(parts[3]);
                    Category category = Category.valueOf(parts[4]);
                    Product product = new Product(id,name,price,stock,category);
                    addProduct(product);
                }catch (IllegalArgumentException e){
                    System.out.println("该条数据格式有误！已跳过！");
                }catch (DuplicateProductException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
