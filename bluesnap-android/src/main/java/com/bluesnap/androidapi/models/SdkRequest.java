package com.bluesnap.androidapi.models;

import com.bluesnap.androidapi.services.BSPaymentRequestException;
import com.bluesnap.androidapi.services.TaxCalculator;

/**
 * A Request for payment process in the SDK.
 * A new SdkRequest should be used for each purchase.
 */
public class SdkRequest {

    private PriceDetails priceDetails;
    private boolean allowCurrencyChange = true;
    private ShopperInfoConfig shopperInfoConfig = new ShopperInfoConfig(false, false, false);
    private TaxCalculator taxCalculator;

    private SdkRequest() {
    }

    public SdkRequest(Double amount, String currencyNameCode) {
        priceDetails = new PriceDetails(amount, currencyNameCode, 0D);
    }

    public SdkRequest(Double amount, String currencyNameCode, Double taxAmount, boolean billingRequired, boolean emailRequired, boolean shippingRequired) {

        priceDetails = new PriceDetails(amount, currencyNameCode, taxAmount);
        shopperInfoConfig = new ShopperInfoConfig(shippingRequired, billingRequired, emailRequired);
    }

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }


    public boolean isAllowCurrencyChange() {
        return allowCurrencyChange;
    }

    public void setAllowCurrencyChange(boolean allowCurrencyChange) {
        this.allowCurrencyChange = allowCurrencyChange;
    }

    public boolean verify() throws BSPaymentRequestException {
        priceDetails.verify();
        return true;
    }

    public TaxCalculator getTaxCalculator() {
        return taxCalculator;
    }

    public void setTaxCalculator(TaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
    }


    public boolean isShippingRequired() {
        return shopperInfoConfig.isShippingRequired();
    }

    public boolean isBillingRequired() {
        return shopperInfoConfig.isBillingRequired();
    }

    public boolean isEmailRequired() {
        return shopperInfoConfig.isEmailRequired();
    }

}
