package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testInvoiceHasNumberGreaterThan0() {
        int number = invoice.getNumber();
        Assert.assertThat(number, Matchers.greaterThan(0));
    }

    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertNotEquals(number1, number2);
    }

    @Test
    public void testInvoiceDoesNotChangeItsNumber() {
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }

    @Test
    public void testTheFirstInvoiceNumberIsLowerThanTheSecond() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertThat(number1, Matchers.lessThan(number2));
    }

    @Test
    public void testInvoiceWithNoLine(){
        ArrayList<String> invoiceList = new ArrayList<>();
        int number = new Invoice().getNumber();
        String numberAsString = String.valueOf(number);
        invoiceList = invoice.stringFormat(number);
        Assert.assertThat( numberAsString, Matchers.comparesEqualTo(invoiceList.get(0)));
        Assert.assertThat("Liczba pozycji: 0", Matchers.comparesEqualTo(invoiceList.get(1)));
    }

    @Test
    public void testInvoiceWithOneLineAndNoTaxes(){
        ArrayList<String> invoiceList = new ArrayList<>();
        int number = new Invoice().getNumber();
        String numberAsString = String.valueOf(number);
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoiceList = invoice.stringFormat(number);
        Assert.assertThat( numberAsString, Matchers.comparesEqualTo(invoiceList.get(0)));
        Assert.assertThat("Chleb,2,10", Matchers.comparesEqualTo(invoiceList.get(1)));
        Assert.assertThat("Liczba pozycji: 1", Matchers.comparesEqualTo(invoiceList.get(2)));
    }

    @Test
    public void testInvoiceWithMoreLinesAndDifferentTaxes(){
        ArrayList<String> invoiceList = new ArrayList<>();
        int number = new Invoice().getNumber();
        String numberAsString = String.valueOf(number);
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        invoiceList = invoice.stringFormat(number);
        Assert.assertThat( numberAsString, Matchers.comparesEqualTo(invoiceList.get(0)));

        Assert.assertThat("Chleb,2,10", Matchers.comparesEqualTo(invoiceList.get(1)));
        Assert.assertThat("Chedar,3,32.40", Matchers.comparesEqualTo(invoiceList.get(2)));
        Assert.assertThat("Pinezka,1000,12.3000", Matchers.comparesEqualTo(invoiceList.get(3)));
        Assert.assertThat("Liczba pozycji: 3", Matchers.comparesEqualTo(invoiceList.get(4)));
    }

    @Test
    public void testInvoiceWithSameProductsAsOneLine(){
        ArrayList<String> invoiceList = new ArrayList<>();
        int number = new Invoice().getNumber();
        String numberAsString = String.valueOf(number);
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 3);
        invoiceList = invoice.stringFormat(number);
        Assert.assertThat( numberAsString, Matchers.comparesEqualTo(invoiceList.get(0)));
        Assert.assertThat("Chleb,5,25", Matchers.comparesEqualTo(invoiceList.get(1)));
        Assert.assertThat("Liczba pozycji: 1", Matchers.comparesEqualTo(invoiceList.get(2)));
    }
    @Test
    public void testInvoiceWithSameProductsAs4Times(){
        ArrayList<String> invoiceList = new ArrayList<>();
        int number = new Invoice().getNumber();
        String numberAsString = String.valueOf(number);
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 3);
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 4);
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 5);
        invoiceList = invoice.stringFormat(number);
        Assert.assertThat( numberAsString, Matchers.comparesEqualTo(invoiceList.get(0)));
        Assert.assertThat("Chleb,14,70", Matchers.comparesEqualTo(invoiceList.get(1)));
        Assert.assertThat("Liczba pozycji: 1", Matchers.comparesEqualTo(invoiceList.get(2)));
    }

    @Test
    public void testInvoiceWithMoreDulicateLinesAndDifferentTaxes(){
        ArrayList<String> invoiceList = new ArrayList<>();
        int number = new Invoice().getNumber();
        String numberAsString = String.valueOf(number);
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        invoiceList = invoice.stringFormat(number);
        Assert.assertThat( numberAsString, Matchers.comparesEqualTo(invoiceList.get(0)));
        Assert.assertThat("Chedar,6,64.80", Matchers.comparesEqualTo(invoiceList.get(1)));
        Assert.assertThat("Chleb,4,20", Matchers.comparesEqualTo(invoiceList.get(2)));

        Assert.assertThat("Pinezka,2000,24.6000", Matchers.comparesEqualTo(invoiceList.get(3)));


        Assert.assertThat("Liczba pozycji: 3", Matchers.comparesEqualTo(invoiceList.get(4)));
    }
}
