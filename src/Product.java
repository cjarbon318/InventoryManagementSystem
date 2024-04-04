

public class Product {
    String Product;
    String Supplier;
    Float Quantity;
    Double Price;
    
    public String getProduct() {
        return Product;
    }

    public void setProdut(String produt) {
        Product = produt;
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

    public Product(String product, String supplier, Float quantity, Double price) {
        Product = product;
        Supplier = supplier;
        Quantity = quantity;
        Price = price;
    }
}

 
    

    