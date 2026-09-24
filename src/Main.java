import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Optional;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        WarehouseManager manager = new WarehouseManager();
        Path path = Path.of("products.txt");

        if(Files.exists(path)){
            try{
                manager.loadFromFile(path);
                System.out.println("历史商品数据加载成功！");
            } catch (IOException e) {
                System.out.println("历史商品数据加载失败！");
            }
        }
        else{
            System.out.println("暂无历史商品数据！");
        }

        System.out.println("欢迎使用商品管理系统！");
        System.out.println();

        outer:
        while(true){
            System.out.println("  菜  单");
            System.out.println("1. 添加商品");
            System.out.println("2. 查询商品");
            System.out.println("3. 删除商品");
            System.out.println("4. 显示全部商品");
            System.out.println("5. 商品入库");
            System.out.println("6. 商品出库");
            System.out.println("7. 按ID排序显示所有商品");
            System.out.println("8. 按价格排序显示所有商品");
            System.out.println("9. 查询低库存商品");
            System.out.println("10.按类别查询第一个商品");
            System.out.println("0. 退出系统");

            System.out.println();
            System.out.print("请选择您需要进行的业务：");
            int choice;
            try{
                choice = sc.nextInt();
            }catch(InputMismatchException e){
                System.out.println("请输入整数！");
                sc.nextLine();
                continue;
            }

            switch(choice){
                case 1:
                    System.out.println("您选择了添加商品");
                    try{
                        System.out.print("请选择您要添加的商品类型(FOOD/ELECTRONICS/BOOK/DAILY):");
                        String s1 = sc.next();
                        System.out.print("请输入该商品的ID:");
                        int id1 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("请输入该商品的名字:");
                        String name1 = sc.nextLine();
                        System.out.print("请输入该商品的价格:");
                        double price1 = sc.nextDouble();
                        System.out.print("请输入该商品的数量:");
                        int stock1 = sc.nextInt();
                        Product product1 = new Product(id1 , name1 , price1 , stock1 , Category.valueOf(s1));
                        manager.addProduct(product1);
                        System.out.println("商品添加成功！");
                        System.out.println();
                    }catch (DuplicateProductException e){
                        System.out.println(e.getMessage());
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }catch(InputMismatchException e){
                        System.out.println("请输入数字！");
                        sc.nextLine();
                    }
                    break;

                case 2:
                    System.out.println("您选择了查询商品");
                    System.out.print("请输入您要查询的商品ID:");
                    try{
                        int id2 = sc.nextInt();
                        Product product2 = manager.findProductById(id2);
                        System.out.println(product2);
                        System.out.println();
                    }catch (ProductNotFoundException e){
                        System.out.println(e.getMessage());
                    }catch(InputMismatchException e){
                        System.out.println("商品ID必须是整数！");
                        sc.nextLine();
                    }
                    break;

                case 3:
                    System.out.println("您选择了删除商品");
                    System.out.print("请输入您要删除的商品ID:");
                    try{
                        int id3 = sc.nextInt();
                        manager.removeProduct(id3);
                        System.out.println("商品删除成功！");
                        System.out.println();
                    }catch(ProductNotFoundException e){
                        System.out.println(e.getMessage());
                    }catch (InputMismatchException e){
                        System.out.println("商品ID必须是整数！");
                        sc.nextLine();
                    }
                    break;

                case 4:
                    System.out.println("您选择了显示全部商品");
                    manager.showAllProducts();
                    System.out.println();
                    break;

                case 5:
                    System.out.println("您选择了商品入库");
                    try{
                        System.out.print("请输入商品ID:");
                        int id5 = sc.nextInt();
                        System.out.print("请输入入库数量:");
                        int amount5 = sc.nextInt();
                        manager.increaseStock(id5 , amount5);
                        System.out.println("商品入库成功！");
                        System.out.println();
                    }catch(ProductNotFoundException e){
                        System.out.println(e.getMessage());
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }catch (InputMismatchException e){
                        System.out.println("请输入整数！");
                        sc.nextLine();
                    }
                    break;

                case 6:
                    System.out.println("您选择了商品出库");
                    try{
                        System.out.print("请输入商品ID:");
                        int id6 = sc.nextInt();
                        System.out.print("请输入出库数量:");
                        int amount6 = sc.nextInt();
                        manager.decreaseStock(id6 , amount6);
                        System.out.println("商品出库成功！");
                        System.out.println();
                    }catch(InsufficientStockException e){
                        System.out.println(e.getMessage());
                    }catch(ProductNotFoundException e){
                        System.out.println(e.getMessage());
                    }catch(IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }catch(InputMismatchException e){
                        System.out.println("请输入整数！");
                        sc.nextLine();
                    }
                    break;

                case 7:
                    System.out.println("您选择了按ID排序显示所有商品");
                    manager.showProductsSortedById();
                    System.out.println();
                    break;

                case 8:
                    System.out.println("您选择了按价格排序显示所有商品");
                    manager.showProductsSortedByPrice();
                    System.out.println();
                    break;

                case 9:
                    System.out.println("您选择了查询低库存商品");
                    try{
                        System.out.print("请输入库存阈值:");
                        int threshold = sc.nextInt();
                        manager.showLowStockProducts(threshold);
                        long count = manager.countLowStockProducts(threshold);
                        System.out.printf("低于此阈值的库存商品现共有%d个%n",count);
                        System.out.println();
                    }catch (InputMismatchException e){
                        System.out.println("请输入整数！");
                        sc.nextLine();
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 10:
                    System.out.println("您选择了按类别查询第一个商品");
                    System.out.print("请输入您想查询的商品类别(FOOD/ELECTRONICS/BOOK/DAILY):");
                    String s10 = sc.next();
                    try{
                        Optional<Product> product = manager.findFirstByCategory(Category.valueOf(s10));
                        if(product.isPresent()){
                            System.out.println(product.get());
                            System.out.println();
                        }
                        else{
                            System.out.println("该类别暂无商品！");
                            System.out.println();
                        }
                    }catch(IllegalArgumentException e){
                        System.out.println("商品类别输入错误！");
                    }
                    break;

                case 0:
                    System.out.println("您选择了退出系统");
                    try{
                        manager.saveToFile(path);
                        System.out.println("商品数据保存成功！");
                        System.out.println();
                    }catch(IOException e){
                        System.out.println("商品数据保存失败！");
                    }
                    break outer;

                default:
                    System.out.println("无效的菜单选项，请重新输入！");
                    System.out.println();
            }
        }
    }
}
