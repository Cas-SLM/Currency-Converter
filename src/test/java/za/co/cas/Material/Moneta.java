//package za.co.cas.Material;
//
//import org.javamoney.moneta.FastMoney;
//import org.javamoney.moneta.Money;
//import org.junit.jupiter.api.Test;
//import javax.money.Monetary;
//import javax.money.CurrencyUnit;
//import javax.money.MonetaryAmount;
//import javax.money.UnknownCurrencyException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class Moneta {
//
//    @Test
//    public void givenCurrencyCode_whenString_thanExist() {
//        CurrencyUnit usd = Monetary.getCurrency("USD");
//
//        assertNotNull(usd);
//        assertEquals(usd.getCurrencyCode(), "USD");
//        assertEquals(usd.getNumericCode(), 840);
//        assertEquals(usd.getDefaultFractionDigits(), 2);
//    }
//
////    @Test(expected = UnknownCurrencyException.class)
////    public void givenCurrencyCode_whenNoExist_thanThrowsError() {
////        Monetary.getCurrency("AAA");
////    }
//
//    @Test
//    public void givenAmounts_whenStringified_thanEquals() {
//
//        CurrencyUnit usd = Monetary.getCurrency("USD");
//        MonetaryAmount fstAmtUSD = Monetary.getDefaultAmountFactory()
//                .setCurrency(usd).setNumber(200).create();
//        Money moneyof = Money.of(12, usd);
//        FastMoney fastmoneyof = FastMoney.of(2, usd);
//
//        assertEquals("USD", usd.toString());
//        assertEquals("USD 200", fstAmtUSD.toString());
//        assertEquals("USD 12", moneyof.toString());
//        assertEquals("USD 2.00000", fastmoneyof.toString());
//    }
//}
