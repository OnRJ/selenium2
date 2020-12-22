package model;

public class Product {
    private String size;

    public static Builder newEntity() { return new Product().new Builder(); }

    public String getSize() {
        return size;
    }

    public class Builder {
        private Builder() {}
        public Builder withSize(String size) { Product.this.size = size; return this; }
        public Product build() {return Product.this; }
    }
}
