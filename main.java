import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        boolean isDiscountValid() {
        LocalDate startDate = LocalDate.parse(effectiveStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = effectiveEndDate == null ? null : LocalDate.parse(effectiveEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate today = LocalDate.now();

        return today.isEqual(startDate) || (today.isAfter(startDate) && (endDate == null || today.isBefore(endDate)));
    }
}

class ShoppingCart {
    List<Product> products;
    Map<Integer, Integer> productQuantities;
    List<Discount> discounts;

    ShoppingCart() {
        products = new ArrayList<>();
        productQuantities = new HashMap<>();
        discounts = new ArrayList<>();
    }

     void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    void addProduct(Product product, int quantity) {
    if (quantity <= 0) {
        System.out.println("Invalid quantity.");
        return;
    }

    products.add(product);
    productQuantities.put(product.productId, productQuantities.getOrDefault(product.productId, 0) + quantity);
}
  
      void displayProducts() {
         if (products.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-10s%n",
                          "Product ID", "Product Name", "Unit", "Price", "Discounts");
        System.out.println("------------------------------------------------------------------------");

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

        System.out.println("------------------------------------------------------------------------");
    }
     void displayCartContents() {

        if (products.isEmpty()) {
        System.out.println("Your cart is empty.");
        return;
    }
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-20s | %-10s%n",
                          "Product ID", "Product Name", "Unit", "Price", "Discounts", "Quantity");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Product product : products) {
            List<Discount> applicableDiscounts = getEligibleDiscounts(product.productId);
            String discountInfo = "";
            if (!applicableDiscounts.isEmpty()) {
                StringBuilder discountsBuilder = new StringBuilder();
                for (Discount discount : applicableDiscounts) {
                    discountsBuilder.append("ID: ").append(discount.discountId).append(", ");
                    discountsBuilder.append("Valid: ").append(discount.isDiscountValid() ? "Yes" : "No").append(", ");
                }
                discountInfo = discountsBuilder.toString();
            } else {
                discountInfo = "None";
            }

            int productQuantity = getProductQuantity(product.productId);

            System.out.printf("%-10d | %-20s | %-10s | %-10.2f | %-20s | %-10d%n",
                                      product.productId, product.name, product.unit, product.price, discountInfo, productQuantity);
        }

        System.out.println("----------------------------------------------------------------------------------------------");
        double totalPrice = calculateTotalPrice();
        System.out.println("Total Price: " + totalPrice);
    }
    double calculateTotalPrice() {
        double totalPrice = 0;
        for (Product product : products) {
            double productPrice = product.price;
            for (Discount discount : getEligibleDiscounts(product.productId)) {
                if (discount.isDiscountValid()) {
                    if (discount.effectiveEndDate == null || isDiscountValid(discount.effectiveEndDate)) {
                        if (discount.productIds.contains(product.productId)) {
                            int quantity = getProductQuantity(product.productId);
                            int discountProductCount = discount.productIds.size();
                            int eligibleQuantity = quantity / discountProductCount;
                            int remainingQuantity = quantity % discountProductCount;
                            double discountedPrice = (eligibleQuantity * (discountProductCount - 1) + remainingQuantity)
                                    * product.price;
                            productPrice = Math.min(productPrice, discountedPrice);
                        }
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

    private boolean isDiscountValid(String effectiveEndDate) {
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

public class main {
    public static void main (String[] args) {
        Product product1 = new Product(1, "Banana", "kg", 100.00);
        Product product2 = new Product(2, "Orange", "kg", 230.00);
        Product product3 = new Product(3, "Apple", "kg", 330.00);
        Product product4 = new Product(4, "Grapes", "Kg", 230.00);

        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(product1, 0);
        cart.addProduct(product2, 0);
        cart.addProduct(product3, 0);
        cart.addProduct(product4, 0);
        List<Integer> productIds1 = new ArrayList<>();
        productIds1.add(1);
        Discount discount1 = new Discount(1, productIds1, "Buy 1 Get 1 Free", "2023-08-02", "2023-08-10");

        List<Integer> productIds2 = new ArrayList<>();
        productIds2.add(2);
        productIds2.add(3);
        Discount discount2 = new Discount(2, productIds2, "Buy 2 Get 1 Free", "2023-08-02", null);

        cart.addDiscount(discount1);
        cart.addDiscount(discount2);

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
                    cart.displayCartContents();
                    break;
                    case 3:
                    System.out.print("Enter the product ID: ");
                    int productId = scanner.nextInt();
                    System.out.print("Enter the product quantity: ");
                    int quantity = scanner.nextInt();
                    Product selectedProduct = null;
                
                    if (productId == 1) {
                        selectedProduct = product1;
                    } else if (productId == 2) {
                        selectedProduct = product2;
                    } else if (productId == 3) {
                        selectedProduct = product3;
                    } else if (productId == 4) {
                        selectedProduct = product4;
                    } else {
                        System.out.println("Invalid product ID.");
                        break;
                    }
                
                    cart.addProduct(selectedProduct, quantity);
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