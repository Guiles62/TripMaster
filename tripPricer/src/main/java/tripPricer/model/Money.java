package tripPricer.model;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class Money {

    private CurrencyUnit currency = Monetary.getCurrency("USD");
    private org.javamoney.moneta.Money lowerPricePoint = org.javamoney.moneta.Money.of(0, currency);
    private org.javamoney.moneta.Money highPricePoint = org.javamoney.moneta.Money.of(Integer.MAX_VALUE, currency);

    public Money() {
    }

    public CurrencyUnit getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyUnit currency) {
        this.currency = currency;
    }

    public org.javamoney.moneta.Money getLowerPricePoint() {
        return lowerPricePoint;
    }

    public void setLowerPricePoint(org.javamoney.moneta.Money lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    public org.javamoney.moneta.Money getHighPricePoint() {
        return highPricePoint;
    }

    public void setHighPricePoint(org.javamoney.moneta.Money highPricePoint) {
        this.highPricePoint = highPricePoint;
    }
}
