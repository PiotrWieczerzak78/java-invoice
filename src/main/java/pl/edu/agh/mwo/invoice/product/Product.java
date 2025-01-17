package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        if( name == null ){
            throw new IllegalArgumentException("Product name cannot be null");
        }
        if(price ==null){
            throw new IllegalArgumentException("Product price cannot be null");
        }
        if(name.isEmpty()){
            throw new IllegalArgumentException("Product name cannot be empty");
        }
//        if (price.compareTo(BigDecimal.ZERO) < 0){
//            throw new IllegalArgumentException("Product price cannot be below zero");
//        }
        if (price.signum() == -1){
            throw new IllegalArgumentException("Product price cannot be below zero");
        }
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getTaxPercent() {
        return this.taxPercent;
    }

    public BigDecimal getPriceWithTax() {

        return this.price.multiply(this.taxPercent.add(BigDecimal.valueOf(1.0))) ;
    }
}
