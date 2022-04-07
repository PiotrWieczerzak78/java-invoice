package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    private final BigDecimal excise;

    protected Product(String name, BigDecimal price, BigDecimal tax, BigDecimal excise) {
        BigDecimal zero = new BigDecimal(0);
        if (name == null || name.equals("") || price == null || tax == null || excise == null
                || tax.compareTo(zero) < 0 || price.compareTo(zero) < 0 || excise.compareTo(zero) < 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
        this.excise = excise;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getExcise() {
        return excise;
    }

    public BigDecimal getPriceWithTax() {
        return price.multiply(taxPercent).add(price);
    }
    public BigDecimal getPriceWithTaxAndExcise() {
        return price.multiply(taxPercent).add(price).add(excise);
    }
}
