package com.bluesnap.android.demoapp.ShopperConfigUITests;

import com.bluesnap.android.demoapp.TestingShopperCheckoutRequirements;
import com.bluesnap.android.demoapp.TestingShopperCreditCard;
import com.bluesnap.androidapi.services.BSPaymentRequestException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by sivani on 01/09/2018.
 */

public class ChoosePaymentMethodFullBillingWithEmailWithShipping extends ChoosePaymentMethodEspressoBasedTester {
    @Before
    public void setup() throws InterruptedException, BSPaymentRequestException, JSONException {
        shopperCheckoutRequirements = new TestingShopperCheckoutRequirements(true, true, true);

    }

    /**
     * This test does a full billing with email and shipping
     * end-to-end choose cc payment flow, for new card.
     *
     * @throws InterruptedException
     * @throws JSONException
     * @throws BSPaymentRequestException
     */
    @Test
    public void choose_new_cc_payment_with_full_billing_with_email_with_shipping_flow() throws InterruptedException, JSONException, BSPaymentRequestException {
        choosePaymentSetup(true, true);

        //choose new card
        chooseNewCardPaymentMethod(TestingShopperCreditCard.MASTERCARD_CREDIT_CARD);
    }

    /**
     * This test does a full billing with email and shipping
     * end-to-end choose cc payment flow, for existing card.
     *
     * @throws InterruptedException
     * @throws JSONException
     * @throws BSPaymentRequestException
     */
    @Test
    public void choose_exists_cc_payment_with_full_billing_with_email_with_shipping_flow() throws InterruptedException, JSONException, BSPaymentRequestException {
        choosePaymentSetup(true, true);

        uIAutoTestingBlueSnapService.setExistingCard(true);

        //choose first credit card
        chooseExistingCardPaymentMethod(TestingShopperCreditCard.VISA_CREDIT_CARD);
    }

    /**
     * This test does a full billing with email and shipping
     * end-to-end choose cc payment flow, for new card for shopper without cc.
     *
     * @throws InterruptedException
     * @throws JSONException
     * @throws BSPaymentRequestException
     */
    @Test
    public void choose_new_cc_payment_for_shopper_without_cc_flow() throws InterruptedException, JSONException, BSPaymentRequestException {
        choosePaymentSetup(true, false);

        //choose first credit card
        chooseNewCardPaymentMethod(TestingShopperCreditCard.MASTERCARD_CREDIT_CARD);
    }

    /**
     * This test does a full billing with email and shipping
     * end-to-end choose paypal payment flow, for shopper with cc.
     *
     * @throws InterruptedException
     * @throws JSONException
     * @throws BSPaymentRequestException
     */
    @Test
    public void choose_paypal_payment_for_shopper_with_cc() throws InterruptedException, JSONException, BSPaymentRequestException {
        choosePaymentSetup(true, true);

        //choose first credit card
        choosePayPalPaymentMethod();
    }

    /**
     * This test does a full billing with email and shipping
     * end-to-end choose paypal payment flow, for shopper without cc.
     *
     * @throws InterruptedException
     * @throws JSONException
     * @throws BSPaymentRequestException
     */
    @Test
    public void choose_paypal_payment_for_shopper_without_cc() throws InterruptedException, JSONException, BSPaymentRequestException {
        choosePaymentSetup(true, false);

        //choose first credit card
        choosePayPalPaymentMethod();
    }


}
