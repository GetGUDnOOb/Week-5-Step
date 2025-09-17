import java.time.LocalDateTime;
import java.util.*;

public final class ECommerceSystem {

    // ✅ a. Immutable Product Class
    public static final class Product {
        private final String productId;
        private final String name;
        private final String category;
        private final String manufacturer;
        private final double basePrice;
        private final double weight;
        private final String[] features;
        private final Map<String, String> specifications;

        private Product(String productId, String name, String category, String manufacturer,
                        double basePrice, double weight, String[] features, Map<String, String> specifications) {
            this.productId = productId;
            this.name = name;
            this.category = category;
            this.manufacturer = manufacturer;
            this.basePrice = basePrice;
            this.weight = weight;
            this.features = Arrays.copyOf(features, features.length);
            this.specifications = Map.copyOf(specifications);
        }

        // Factory Methods
        public static Product createElectronics(String id, String name, String manufacturer,
                                                double price, double weight,
                                                String[] features, Map<String, String> specs) {
            return new Product(id, name, "Electronics", manufacturer, price, weight, features, specs);
        }

        public static Product createClothing(String id, String name, String manufacturer,
                                             double price, double weight,
                                             String[] features, Map<String, String> specs) {
            return new Product(id, name, "Clothing", manufacturer, price, weight, features, specs);
        }

        public static Product createBooks(String id, String name, String manufacturer,
                                          double price, double weight,
                                          String[] features, Map<String, String> specs) {
            return new Product(id, name, "Books", manufacturer, price, weight, features, specs);
        }

        public String getProductId() { return productId; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getManufacturer() { return manufacturer; }
        public double getBasePrice() { return basePrice; }
        public double getWeight() { return weight; }

        public String[] getFeatures() {
            return Arrays.copyOf(features, features.length);
        }

        public Map<String, String> getSpecifications() {
            return Map.copyOf(specifications);
        }

        public final double calculateTax(String region) {
            switch (region.toLowerCase()) {
                case "us": return basePrice * 0.07;
                case "eu": return basePrice * 0.2;
                case "asia": return basePrice * 0.1;
                default: return basePrice * 0.05;
            }
        }

        @Override
        public String toString() {
            return name + " [" + category + "] - $" + basePrice;
        }
    }

    // ✅ b. Customer class with privacy tiers
    public static class Customer {
        private final String customerId;
        private final String email;
        private String name;
        private String phoneNumber;
        private String preferredLanguage;
        private final String accountCreationDate;

        public Customer(String customerId, String email) {
            this.customerId = customerId;
            this.email = email;
            this.accountCreationDate = LocalDateTime.now().toString();
        }

        public String getCustomerId() { return customerId; }
        public String getEmail() { return email; }
        public String getAccountCreationDate() { return accountCreationDate; }

        public void setName(String name) { this.name = name; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }

        String getCreditRating() {
            return "Good"; // placeholder for internal logic
        }

        public String getPublicProfile() {
            return "Customer: " + name + " | Language: " + preferredLanguage;
        }

        @Override
        public String toString() {
            return "Customer{" + "customerId='" + customerId + '\'' + ", email='" + email + '\'' + '}';
        }
    }

    // ✅ c. ShoppingCart with access control
    public static class ShoppingCart {
        private final String cartId;
        private final String customerId;
        private final List<Product> items = new ArrayList<>();
        private double totalAmount = 0;
        private int itemCount = 0;

        public ShoppingCart(String cartId, String customerId) {
            this.cartId = cartId;
            this.customerId = customerId;
        }

        public boolean addItem(Object product, int quantity) {
            if (product instanceof Product && quantity > 0) {
                Product p = (Product) product;
                for (int i = 0; i < quantity; i++) {
                    items.add(p);
                    totalAmount += p.getBasePrice();
                    itemCount++;
                }
                return true;
            }
            return false;
        }

        private double calculateDiscount() {
            if (itemCount > 5) return totalAmount * 0.10;
            return 0;
        }

        String getCartSummary() {
            return "Cart ID: " + cartId + ", Items: " + itemCount + ", Total: $" + (totalAmount - calculateDiscount());
        }
    }

    // ✅ d. Constructor chaining for customer types
    public static class OrderCustomer extends Customer {
        private final String accountType;

        // Guest
        public OrderCustomer(String email) {
            super("GUEST-" + System.currentTimeMillis(), email);
            this.accountType = "Guest";
        }

        // Registered
        public OrderCustomer(String id, String email, String name, String phone) {
            super(id, email);
            setName(name);
            setPhoneNumber(phone);
            this.accountType = "Registered";
        }

        // Premium
        public OrderCustomer(String id, String email, String name, String phone, String lang) {
            super(id, email);
            setName(name);
            setPhoneNumber(phone);
            setPreferredLanguage(lang);
            this.accountType = "Premium";
        }

        // Corporate
        public OrderCustomer(String id, String email, String name, String companyTaxId) {
            super(id, email);
            setName(name + " (Corporate)");
            this.accountType = "Corporate";
        }

        public String getAccountType() {
            return accountType;
        }
    }

    // ✅ e. Order processing classes
    public static class Order {
        private final String orderId;
        private final LocalDateTime orderTime;

        public Order(String orderId) {
            this.orderId = orderId;
            this.orderTime = LocalDateTime.now();
        }

        public String getOrderId() {
            return orderId;
        }

        public LocalDateTime getOrderTime() {
            return orderTime;
        }

        @Override
        public String toString() {
            return "Order ID: " + orderId + " at " + orderTime;
        }
    }

    public static class PaymentProcessor {
        private final String processorId;
        private final String securityKey;

        public PaymentProcessor(String processorId, String securityKey) {
            this.processorId = processorId;
            this.securityKey = securityKey;
        }

        public boolean processPayment(double amount) {
            return amount > 0; // Simulated logic
        }
    }

    public static class ShippingCalculator {
        private final Map<String, Double> shippingRates;

        public ShippingCalculator(Map<String, Double> rates) {
            this.shippingRates = Map.copyOf(rates);
        }

        public double calculateShipping(String region, double weight) {
            return shippingRates.getOrDefault(region, 10.0) * weight;
        }
    }

    // ✅ f. Final ECommerceSystem class
    private static final Map<String, Object> productCatalog = new HashMap<>();

    public static boolean processOrder(Object order, Object customer) {
        if (order instanceof Order && customer instanceof Customer) {
            System.out.println("Order processed: " + order);
            return true;
        }
        return false;
    }

    public static void addToCatalog(String id, Product product) {
        productCatalog.put(id, product);
    }

    public static void showCatalog() {
        productCatalog.forEach((id, product) -> System.out.println(id + ": " + product));
    }

    // ✅ Main method for demo
    public static void main(String[] args) {
        // Create sample products
        Product laptop = Product.createElectronics(
                "E1001", "Laptop Pro", "TechBrand", 1200.00, 2.5,
                new String[]{"SSD", "16GB RAM"},
                Map.of("CPU", "i7", "OS", "Windows 11")
        );

        Product shirt = Product.createClothing(
                "C2001", "Casual Shirt", "ClothCo", 35.00, 0.5,
                new String[]{"Cotton", "Slim Fit"},
                Map.of("Size", "M", "Color", "Blue")
        );

        addToCatalog(laptop.getProductId(), laptop);
        addToCatalog(shirt.getProductId(), shirt);

        showCatalog();

        // Customer
        OrderCustomer customer = new OrderCustomer("U1001", "john@example.com", "John", "123456789", "EN");

        // Cart
        ShoppingCart cart = new ShoppingCart("CART001", customer.getCustomerId());
        cart.addItem(laptop, 1);
        cart.addItem(shirt, 2);

        System.out.println(cart.getCartSummary());

        // Order
        Order order = new Order("ORD001");

        // Process order
        processOrder(order, customer);

        // Tax check
        System.out.println("Tax on Laptop (US): $" + laptop.calculateTax("US"));
    }
}
