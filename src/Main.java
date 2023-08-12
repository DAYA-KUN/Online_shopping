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

    ShoppingCart() {
        products = new ArrayList<>();
        productQuantities = new HashMap<>();
    }

    void addProduct(Product product) {
        products.add(product);
        productQuantities.put(product.productId, productQuantities.getOrDefault(product.productId, 0) + 1);
    }

  
    void displayProducts() {
        for (Product product : products) {
            System.out.println(product.name + " - " + product.price + " per " + product.unit);
            List<Discount> applicableDiscounts = getEligibleDiscounts(product.productId);
            if (!applicableDiscounts.isEmpty()) {
                System.out.println("Applicable Discounts:");
                for (Discount discount : applicableDiscounts) {
                    System.out.println("Discount ID: " + discount.discountId);
                    System.out.println("Discount Name: " + discount.name);
                    System.out.println("Effective Start Date: " + discount.effectiveStartDate);
                    System.out.println("Effective End Date: " + (discount.effectiveEndDate != null ? discount.effectiveEndDate : "N/A"));
                }
            } else {
                System.out.println("No Discounts Applicable for this Product.");
            }
        }
    }

    double calculateTotalPrice() {
        double totalPrice = 0;
        // Calculate total price after applying discounts
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
        // Implement logic to filter eligible discounts for the given product based on effective dates
        for (Discount discount : getDiscounts()) {
            if (discount.productIds.contains(productId)) {
                eligibleDiscounts.add(discount);
            }
        }
        return eligibleDiscounts;
    }

    private boolean isDiscountValid(String effectiveEndDate) {
        // Implement logic to check if the discount is still valid based on the effective end date
        // For simplicity, let's assume all discounts are still valid
        // In a real-world scenario, you would need to compare the current date with the effective end date
        return true;
    }

    private int getProductQuantity(int productId) {
        // Implement logic to get the quantity of the given product in the shopping cart
        return productQuantities.getOrDefault(productId, 0);
    }

    private List<Discount> getDiscounts() {
        // Implement logic to fetch all available discounts from the database or any data source
        // For simplicity, let's assume we have hardcoded the discounts here
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

public class Main {
    public static void main(String[] args) {
        Product product1 = new Product(1, "Banana", "kg", 100.00);
        Product product2 = new Product(2, "Orange", "kg", 230.00);
        Product product3 = new Product(3, "Apple", "kg", 330.00);

        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product3);

        Scanner scanner = new Scanner(System.in);
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
                    cart.displayProducts();
                    break;
                case 2:
                    double totalPrice = cart.calculateTotalPrice();
                    System.out.println("Shopping Cart Total Price: " + totalPrice);
                    break;
                case 3:
                    System.out.println("Enter the product ID: ");
                    int productId = scanner.nextInt();
                    // You can implement product lookup based on the entered product ID
                    // For simplicity, let's assume the product exists and add it to the cart
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
