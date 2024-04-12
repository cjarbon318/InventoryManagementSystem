
// The Product class represents a product with its attributes such as name,
//  supplier, quantity, and price.

public class Product {
    String Product;
    String Supplier;
    Float Quantity;
    Double Price;

    // getters and setters

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getSupplier() {
        return Supplier;
    }

    public void setSupplier(String supplier) {
        Supplier = supplier;
    }

    public Float getQuantity() {
        return Quantity;
    }

    public void setQuantity(Float quantity) {
        Quantity = quantity;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    // Constructs a new Product object with the specified attributes.
    // @param product The name of the product.
    // @param supplier The supplier of the product.
    // @param quantity The quantity of the product.
    // @param price The price of the product.

    public Product(String product, String supplier, Float quantity, Double price) {
        Product = product;
        Supplier = supplier;
        Quantity = quantity;
        Price = price;
    }
}
