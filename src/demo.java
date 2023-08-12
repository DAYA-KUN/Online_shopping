import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Product {
    int productId;
    String name;
    String unit;
    double price;

    Product(int productId, String name, String unit, double price) {
        this.productId = productId;
        this.name = name;
        this.unit = unit;
        this.price = price;
    }
}

class Discount {
    int discountId;
    List<Integer> productIds;
    String name;
    String effectiveStartDate;
    String effectiveEndDate;

    Discount(int discountId, List<Integer> productIds, String name, String effectiveStartDate, String effectiveEndDate) {
        this.discountId = discountId;
        this.productIds = productIds;
        this.name = name;
        this.effectiveStartDate = effectiveStartDate;
        this.effectiveEndDate = effectiveEndDate;
    }
}

class ShoppingCart {
    List<Product> products;
    Map<Integer, Integer> productQuantities;

        void displayCartContents() {
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-10s | %-10s%n",
                          "Product ID", "Product Name", "Unit", "Price", "Quantity", "Subtotal");
        System.out.println("-------------------------------------------------------");

        for (Product product : products) {
            int quantity = getProductQuantity(product.productId);
            double subtotal = product.price * quantity;

            System.out.printf("%-10d | %-20s | %-10s | %-10.2f | %-10d | %-10.2f%n",
                              product.productId, product.name, product.unit, product.price, quantity, subtotal);
        }

        System.out.println("-------------------------------------------------------");
    }

    ShoppingCart() {
        products = new ArrayList<>();
        productQuantities = new HashMap<>();
    }

    void addProduct(Product product) {
        products.add(product);
        productQuantities.put(product.productId, productQuantities.getOrDefault(product.productId, 0) + 1);
    }

    void displayProducts() {
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-10s%n",
                          "Product ID", "Product Name", "Unit", "Price", "Discounts");
        System.out.println("-------------------------------------------------------");

        for (Product product : products) {
            List<Discount> applicableDiscounts = getEligibleDiscounts(product.productId);
            String discountInfo = "";
            if (!applicableDiscounts.isEmpty()) {
                StringBuilder discountsBuilder = new StringBuilder();
                for (Discount discount : applicableDiscounts) {
                    discountsBuilder.append("ID: ").append(discount.discountId).append(", ");
                }
                discountInfo = discountsBuilder.toString();
            } else {
                discountInfo = "None";
            }

            System.out.printf("%-10d | %-20s | %-10s | %-10.2f | %-10s%n",
                              product.productId, product.name, product.unit, product.price, discountInfo);
        }

        System.out.println("-------------------------------------------------------");
    }

     double calculateTotalPrice() {
        double totalPrice = 0;
        for (Product product : products) {
            double productPrice = product.price;
            for (Discount discount : getEligibleDiscounts(product.productId)) {
                if (discount.effectiveEndDate == null || isDiscountValid(discount.effectiveEndDate)) {
                    if (discount.productIds.contains(product.productId)) {
                        int quantity = getProductQuantity(product.productId);
                        int discountProductCount = discount.productIds.size();
                        int eligibleQuantity = quantity / discountProductCount;
                        int remainingQuantity = quantity % discountProductCount;
                        double discountedPrice = (eligibleQuantity * (discountProductCount - 1) + remainingQuantity) * product.price;
                        productPrice = Math.min(productPrice, discountedPrice);
                    }
                }
            }
            totalPrice += productPrice;
        }
        return totalPrice;
    }

    private List<Discount> getEligibleDiscounts(int productId) {
        List<Discount> eligibleDiscounts = new ArrayList<>();
        for (Discount discount : getDiscounts()) {
            if (discount.productIds.contains(productId)) {
                eligibleDiscounts.add(discount);
            }
        }
        return eligibleDiscounts;
    }

    private boolean isDiscountValid(String effectiveEndDate){
        return true;
    }

    private int getProductQuantity(int productId) {
        return productQuantities.getOrDefault(productId, 0);
    }

    private List<Discount> getDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        List<Integer> productIds1 = new ArrayList<>();
        productIds1.add(1);
        Discount discount1 = new Discount(1, productIds1, "Buy 1 Get 1 Free", "2023-08-02", "2023-08-10");
        discounts.add(discount1);

        List<Integer> productIds2 = new ArrayList<>();
        productIds2.add(2);
        productIds2.add(3);
        Discount discount2 = new Discount(2, productIds2, "Buy 2 Get 1 Free", "2023-08-02", null);
        discounts.add(discount2);

        return discounts;
    }
}

public class demo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();
        Product product1 = new Product(1, "Banana", "kg", 100.00);
        Product product2 = new Product(2, "Orange", "kg", 230.00);
        Product product3 = new Product(3, "Apple", "kg", 330.00);

        
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product3);

        
        
        int option;
        do {
            System.out.println("1. Show Products");
            System.out.println("2. Show Shopping Cart and Total Price");
            System.out.println("3. Add Product to Shopping Cart");
            System.out.println("4. Exit");
            System.out.print("Enter option: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("-------------------------------------------------------");
                    System.out.printf("%-10s | %-20s | %-10s | %-10s%n",
                                      "Product ID", "Product Name", "Unit", "Price");
                    System.out.println("-------------------------------------------------------");
                    System.out.printf("%-10d | %-20s | %-10s | %-10.2f%n",
                                      product1.productId, product1.name, product1.unit, product1.price);
                    System.out.printf("%-10d | %-20s | %-10s | %-10.2f%n",
                                      product2.productId, product2.name, product2.unit, product2.price);
                    System.out.printf("%-10d | %-20s | %-10s | %-10.2f%n",
                                      product3.productId, product3.name, product3.unit, product3.price);
                    System.out.println("-------------------------------------------------------");
                    break;
                case 2:
                    cart.displayCartContents();
                    double totalPrice = cart.calculateTotalPrice();
                    System.out.println("Shopping Cart Total Price: " + totalPrice);
                    break;
                case 3:
                     System.out.println("Enter the product ID: ");
                    int productId = scanner.nextInt();
                    if (productId == 1) {
                        cart.addProduct(product1);
                    } else if (productId == 2) {
                        cart.addProduct(product2);
                    } else if (productId == 3) {
                        cart.addProduct(product3);
                    } else {
                        System.out.println("Invalid product ID.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 4);

        scanner.close();
    }
}
