package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    private static int nextNumber = 0;
    private final int number = ++nextNumber;

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        int existingQuantity= 0;
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        for(Map.Entry<Product, Integer> prod : products.entrySet()) {
            if(prod.getKey().getName().equals(product.getName()) && prod.getKey().getPrice().equals(product.getPrice())&& prod.getKey().getTaxPercent().equals(product.getTaxPercent())){
                existingQuantity = (int)prod.getValue();
                products.remove(prod.getKey());
                break;
            }
        }
        products.put(product,existingQuantity+quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<String> stringFormat(int number) {
        ArrayList<String> invoiceList = new ArrayList<>();
        invoiceList.add(String.valueOf(number));
        String invoiceLine="";
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            BigDecimal priceWithTax = product.getPriceWithTax().multiply(quantity);
            invoiceLine= product.getName()+","+String.valueOf(quantity)+","+String.valueOf(priceWithTax);
            invoiceList.add(invoiceLine);
        }
        invoiceList.add("Liczba pozycji: "+String.valueOf(products.size()));;
        return invoiceList;
    }
}
